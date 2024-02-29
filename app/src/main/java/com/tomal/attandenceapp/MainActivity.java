package com.tomal.attandenceapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tomal.attandenceapp.databinding.ActivityMainBinding;
import com.tomal.attandenceapp.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BiometricPrompt biometricPrompt;
    private boolean isPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            isPermission = true;
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.USE_BIOMETRIC, Manifest.permission.CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isPermission){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 3000);




    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setupBio() {
        biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButton("Use account password", getMainExecutor(), (dialog, which) -> {
                    Toast.makeText(MainActivity.this, "Authentication cancelled", Toast.LENGTH_SHORT).show();
                }).build();

        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                System.out.println(result.getCryptoObject());
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                System.out.println("Failed Verify");
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                System.out.println("Error Verify: "+errString);
            }
        };

        biometricPrompt.authenticate( new CancellationSignal(),getMainExecutor(), callback);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== 1 && grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
           Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            isPermission = true;
        }else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            isPermission = false;
        }
    }
}