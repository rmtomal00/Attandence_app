package com.tomal.attandenceapp.register;

import static com.tomal.attandenceapp.BuildConfig.url;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterApiCall extends ViewModel {

    private final MutableLiveData<com.tomal.attandenceapp.register.Response> responseMutableLiveData = new MutableLiveData<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    public void registerUser(String s) {
        OkHttpClient client = new OkHttpClient();


        // Convert JSON object to string

        // Create request body
        RequestBody requestBody = RequestBody.create(s, MediaType.parse("application/json; charset=utf-8"));

        // Build the request
        Request request = new Request.Builder()
                .url(url + "/auth/signup")
                .post(requestBody)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String s = response.body().string();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        responseMutableLiveData.setValue(new Gson().fromJson(s, com.tomal.attandenceapp.register.Response.class));
                    }
                });
            }
        });
    }

    public void userRegister(Context context, String data) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST, url+"/auth/signup", new JSONObject(data), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("TAG", "onResponse: "+jsonObject);
                responseMutableLiveData.setValue(new Gson().fromJson(new Gson().toJson(jsonObject), com.tomal.attandenceapp.register.Response.class));
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("TAG", "onErrorResponse: "+volleyError.networkResponse.statusCode);
                Log.d("TAG", "onErrorResponse: "+ (new String(volleyError.networkResponse.data)));
                String s = new String(volleyError.networkResponse.data);
                responseMutableLiveData.setValue(new Gson().fromJson(s, com.tomal.attandenceapp.register.Response.class));
                Log.d("TAG", "onErrorResponse: "+ Arrays.toString(volleyError.getStackTrace()));

            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        queue.add(request);
    }

    public LiveData<com.tomal.attandenceapp.register.Response> getRegisterResponse() {
        return responseMutableLiveData;
    }
}
