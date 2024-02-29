package com.tomal.attandenceapp.login;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.tomal.attandenceapp.R;
import com.tomal.attandenceapp.common.DialogProgress;
import com.tomal.attandenceapp.common.SharePref;
import com.tomal.attandenceapp.databinding.ActivityLoginBinding;
import com.tomal.attandenceapp.register.RegisterAccount;
import com.tomal.attandenceapp.usersroute.UserActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    LoginApiCall apiCall;
    private ActivityLoginBinding binding;
    private SharePref sharePref;
    private DialogProgress dialogProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dialogProgress = new DialogProgress(this);
        sharePref = new SharePref(this);
        apiCall = new ViewModelProvider(this).get(LoginApiCall.class);

        if (sharePref.getToken()!=null && !TextUtils.isEmpty(sharePref.getToken())){
            System.out.println(sharePref.getToken());
            Toast.makeText(this, "Already Login token : "+ sharePref.getToken(), Toast.LENGTH_LONG).show();
            uiChange();
        }

        apiCall.loginModelMutableLiveData.observe(this, obj->{

            LoginResponse response = new Gson().fromJson(obj.toString(), LoginResponse.class);
            System.out.println(response.getMessage());
            System.out.println(response.isError());

            if (!response.isError()){
                System.out.println(response.getMessage());
                String token = response.getData().getToken();
                sharePref.saveToken(token);
                System.out.println(token);
                dialogProgress.hideDialog();
                uiChange();
            }else {
                System.out.println(response.getMessage());
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
                dialogProgress.hideDialog();
            }
            /*boolean err; String message; String token;
            try {
                err = obj.getBoolean("error");
                message = obj.getString("message");
                if (!err){
                    System.out.println(message);
                    JSONObject data = obj.getJSONObject("data") ;
                    token = data.getString("token");
                    sharePref.saveToken(token);
                    System.out.println(token);
                    dialogProgress.hideDialog();
                    uiChange();
                }else {
                    System.out.println(message);
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    dialogProgress.hideDialog();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }*/
        });

        binding.buttonLogin.setOnClickListener(v->{
            if (TextUtils.isEmpty(binding.emailLogin.getText())){
                binding.emailLogin.setError("Enter Email");
                return;
            }
            if (!binding.emailLogin.getText().toString().contains("@")){
                binding.emailLogin.setError("Enter Valid Email");
                return;
            }
            if (TextUtils.isEmpty(binding.passwordLogin.getText())){
                binding.passwordLogin.setError("Enter Password");
                return;
            }
            if (binding.passwordLogin.getText().length()<6){
                binding.passwordLogin.setError("Enter Password with 6 digits");
                return;
            }
            dialogProgress.showDialog("Please wait", false, "Loading");
            String userName = binding.emailLogin.getText().toString();
            String password = binding.passwordLogin.getText().toString();
            LoginModel model = new LoginModel(userName,password);
            Gson gson = new Gson();
            String json = gson.toJson(model);
            System.out.println(json);

            try {
                apiCall.loginRequest(json, this);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        binding.createNewAccountLogin.setOnClickListener(v->{
            startActivity(new Intent(LoginActivity.this, RegisterAccount.class));
        });
    }

    private void uiChange(){
        startActivity(new Intent(LoginActivity.this, UserActivity.class));
        finish();
    }
}