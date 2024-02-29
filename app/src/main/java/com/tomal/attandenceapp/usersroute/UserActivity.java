package com.tomal.attandenceapp.usersroute;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tomal.attandenceapp.R;
import com.tomal.attandenceapp.adapters.usersAttandence.UserAttandenceAdapter;
import com.tomal.attandenceapp.adapters.usersAttandence.UserAttandenceAdapterModel;
import com.tomal.attandenceapp.common.CommonMethod;
import com.tomal.attandenceapp.common.SharePref;
import com.tomal.attandenceapp.databinding.ActivityUserBinding;
import com.tomal.attandenceapp.login.LoginActivity;
import com.tomal.attandenceapp.picturesendtoserver.PictureActivity;
import com.tomal.attandenceapp.retrofit.userattandencelist.RetroMethod;

import org.json.JSONException;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;

    private UserCallApi userCallApi;
    UserAttandenceAdapter a;
    String path = "";
    RetroMethod retro;
    ArrayList<UserAttandenceAdapterModel> list = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        SharePref sharePref = new SharePref(this);
        String token = sharePref.getToken();
        UserCallModel model = new UserCallModel(token);
        String s = new Gson().toJson(model);
        userCallApi = new ViewModelProvider(this).get(UserCallApi.class);
        retro = new ViewModelProvider(this).get(RetroMethod.class);

        userCallApi.getUserData().observe(this, data ->{
            if (data.isError()){
                new CommonMethod().showAlertUser("Error", "Token Invalid or Expire", this, LoginActivity.class, false);
            }else {
                Glide.with(this).load(data.getData().getPhoto()).into(binding.profileImage);
                binding.name.setText(data.getData().getUserName());
                binding.email.setText(data.getData().getUserMail());
                binding.phone.setText(data.getData().getUserPhone());
                path = data.getData().getPhotoPath();
                System.out.printf(path);
                retro.getData(this, binding.email.getText().toString());

                if (data.getData().isGetAttandence()){
                    binding.attandence.setVisibility(View.VISIBLE);
                }else {
                    binding.attandence.setVisibility(View.GONE);
                }
            }
        });
        binding.attandence.setOnClickListener(v -> {
            Intent i = new Intent(this, PictureActivity.class);
            i.putExtra("path", path);
            i.putExtra("email", binding.email.getText().toString());
            i.putExtra("name", binding.name.getText().toString());
            startActivity(i);
        });
        a = new UserAttandenceAdapter(list);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(a);
        a.notifyDataSetChanged();

        retro.getMutableLiveData().observe(this, data ->{
            list.addAll(data);
            a.notifyDataSetChanged();
        });

        try {
            userCallApi.callApi(this, s);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}