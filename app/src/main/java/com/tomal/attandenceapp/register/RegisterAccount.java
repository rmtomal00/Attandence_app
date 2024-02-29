package com.tomal.attandenceapp.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.tomal.attandenceapp.BuildConfig;
import com.tomal.attandenceapp.R;
import com.tomal.attandenceapp.common.CommonMethod;
import com.tomal.attandenceapp.common.DialogProgress;
import com.tomal.attandenceapp.databinding.ActivityRegisterAccountBinding;
import com.tomal.attandenceapp.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterAccount extends AppCompatActivity {

    private ActivityRegisterAccountBinding binding;
    int pic_id = 123;
    private ActivityResultLauncher<Intent> launcher;

    final String url = BuildConfig.url;

    Bitmap imageBitmap;
    String photo;
    RegisterApiCall apiCall;
    boolean isImageUpload = false;

    DialogProgress progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterAccountBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progress = new DialogProgress(this);

        apiCall = new ViewModelProvider(this).get(RegisterApiCall.class);
        CommonMethod m = new CommonMethod();
        apiCall.getRegisterResponse().observe(this, registerResponse -> {
            progress.hideDialog();
            if (registerResponse.isError()) {
                Log.d("TAG", "onCreate: " + registerResponse.getMessage());
                m.showAlert("Try Again?", "Your Account creation is unsuccessful due to "+registerResponse.getMessage(),this, LoginActivity.class, false);
            }else m.showAlert("Congratulation!", "Your Account creation is successful. Please check your email to verify your account",this, LoginActivity.class, true);
        });
        binding.userImage.setOnClickListener(v -> {

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
                        binding.userImage.setImageBitmap(imageBitmap);
                    }
                });

        binding.buttonRegister.setOnClickListener(v -> {

            if (TextUtils.isEmpty(binding.registerEmail.getText())){
                binding.registerEmail.setError("Enter Email");
                return;
            }
            if (!binding.registerEmail.getText().toString().contains("@")){
                binding.registerEmail.setError("Enter Valid Email");
                return;
            }
            if (TextUtils.isEmpty(binding.passwordRegister.getText())){
                binding.passwordRegister.setError("Enter Password");
                return;
            }
            if (binding.passwordRegister.getText().length()<6){
                binding.passwordRegister.setError("Enter Password with 6 digits");
                return;
            }
            if (TextUtils.isEmpty(binding.registerUsername.getText())){
                binding.registerUsername.setError("Enter Username");
                return;
            }
            if (TextUtils.isEmpty(binding.registerPhone.getText()) || binding.registerPhone.getText().length()<11 || binding.registerPhone.getText().length() > 11){
                binding.registerPhone.setError("Phone number should be 11 digits");
                return;
            }
            if (!isImageUpload) {
                Toast.makeText(this, "Please Upload Image", Toast.LENGTH_SHORT).show();
                return;
            }
            progress.showDialog("Account is creating", false, "Loading");
            try {
                imageUpload();
            } catch (IOException e) {
                e.printStackTrace();
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
                .build();
        Request request = new Request.Builder()
                .url(url + "/file/upload")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                new Handler(getMainLooper()).post(()->{
                    Toast.makeText(RegisterAccount.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                    progress.hideDialog();
                });
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                JSONObject object;
                try {
                    object = new JSONObject(response.body().string());
                    photo = object.getString("filename");
                    //userRegister();
                    apiCall.userRegister(RegisterAccount.this, getString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    private String getString() {
        UserModel model = new UserModel(binding.registerUsername.getText().toString().trim(),
                binding.registerEmail.getText().toString().trim(),
                binding.passwordRegister.getText().toString().trim(), binding.registerPhone.getText().toString().trim(),
                photo);

        return new Gson().toJson(model);
    }

}