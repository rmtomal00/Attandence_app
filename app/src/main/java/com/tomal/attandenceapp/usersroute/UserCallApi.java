package com.tomal.attandenceapp.usersroute;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.tomal.attandenceapp.BuildConfig;
import com.tomal.attandenceapp.adapters.usersAttandence.UserAttandenceAdapterModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserCallApi extends ViewModel {

    private MutableLiveData<UserData> userData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<UserAttandenceAdapterModel>> userData1 = new MutableLiveData<>();


    private String URL = BuildConfig.url;


    public void callApi(Context context, String userJson) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL + "/user/get-user-info", new JSONObject(userJson), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                userData.setValue(new Gson().fromJson(jsonObject.toString(), UserData.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError.networkResponse != null){
                    userData.setValue(new Gson().fromJson(new String(volleyError.networkResponse.data), UserData.class));
                }else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        queue.add(request);
    }

    public MutableLiveData<UserData> getUserData() {
        return userData;
    }

    public void callData(Context context, String userJson) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL + "/user/get-data", new JSONObject(userJson), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println((String.valueOf(volleyError)));
                //userData.setValue(new Gson().fromJson(new String(volleyError.networkResponse.data), UserData.class));
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        queue.add(request);
    }

}
