package com.tomal.attandenceapp.picturesendtoserver;

import static com.tomal.attandenceapp.BuildConfig.url;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.tomal.attandenceapp.R;
import com.tomal.attandenceapp.common.DialogProgress;
import com.tomal.attandenceapp.databinding.ActivityPictureBinding;
import com.tomal.attandenceapp.register.RegisterAccount;
import com.tomal.attandenceapp.usersroute.UserActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PictureActivity extends AppCompatActivity {

    private ActivityPictureBinding binding;

    private ActivityResultLauncher<Intent> launcher;
    private boolean isImageUpload = false;

    Bitmap imageBitmap;

    String name, email, path;
    DialogProgress progress;

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPictureBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progress = new DialogProgress(this);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        path = intent.getStringExtra("path");

        System.out.println(name +" "+ email +" " +path);
        binding.image.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start the activity with camera_intent, and request pic id
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                launcher.launch(takePictureIntent);
            }
        });

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        isImageUpload = true;
                        Bundle extras = result.getData().getExtras();
                        imageBitmap = (Bitmap) extras.get("data");
                        Log.d("TAG", "onCreate: " + imageBitmap);
                        binding.image.setImageBitmap(imageBitmap);
                    }
                });

        binding.verify.setOnClickListener(v -> {

            if (isImageUpload) {
                try {
                    imageUpload();
                    progress.showDialog("Please Wait Image in processing", false, "Loading");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else {
                Toast.makeText(this, "Image not upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void imageUpload() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos); // Adjust quality as needed
        byte[] imageData = baos.toByteArray();

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "image.jpg",
                        RequestBody.create(imageData, MediaType.parse("image/jpeg")))
                .addFormDataPart("email",email)
                .addFormDataPart("pat", path)
                .addFormDataPart("username", name)
                .build();
        Request request = new Request.Builder()
                .url(url + "/file/facecheck")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("onFailure: " + e.getMessage());
                new Handler(getMainLooper()).post(() -> {
                    progress.hideDialog();
                    alert("Verification Status", "Sorry Unable to verify you", false);
                });
                System.out.println(e.getCause());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    PicModel picModel = new Gson().fromJson(response.body().string(), PicModel.class);
                    new Handler(getMainLooper()).post(() -> {
                        progress.hideDialog();
                        alert("Verification Status", "Congratulations!\nYour verification is "+ picModel.getMessage(), true);
                    });
                }else {
                    new Handler(getMainLooper()).post(() -> {
                        progress.hideDialog();
                        alert("Verification Status", "Sorry Unable to verify you", false);
                    });
                }
            }
        });
    }

    private void alert(String title, String message, boolean isVerify){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title );
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialog, which) -> {
            Intent intent = new Intent(PictureActivity.this, UserActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            dialog.dismiss();
        });

        if(!isVerify){
            builder.setNegativeButton("Try Again", (dialog, which) -> {
                dialog.dismiss();
            });
        }
        builder.create().show();
    }
}