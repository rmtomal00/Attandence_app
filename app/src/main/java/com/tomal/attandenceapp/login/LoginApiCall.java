package com.tomal.attandenceapp.login;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.tomal.attandenceapp.BuildConfig;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginApiCall extends ViewModel {

    Gson gson = new Gson();
    public MutableLiveData<JSONObject> loginModelMutableLiveData = new MutableLiveData<>();
    public void loginRequest(String data, Context context) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = BuildConfig.url + "/auth/login";
        JSONObject jsonObject = new JSONObject(data);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resObj) {
                System.out.println("jsonObject : " + resObj);
                loginModelMutableLiveData.setValue(resObj);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError.networkResponse != null){
                    int code = volleyError.networkResponse.statusCode;
                    String message = volleyError.getMessage();
                    System.out.println("code : " + code);
                    System.out.println("message : " + message);
                    LoginResponse loginResponse = new LoginResponse(code,true,"Login failed due to email or password",null);
                    try {
                        loginModelMutableLiveData.setValue(new JSONObject(gson.toJson(loginResponse)));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);

    }
}
