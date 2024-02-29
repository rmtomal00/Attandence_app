package com.tomal.attandenceapp.common;

import android.content.Context;

public class SharePref {

    private final Context context;

    public SharePref(Context context) {
        this.context = context;
    }

    public void saveToken(String token){
        context.getSharedPreferences("token",Context.MODE_PRIVATE).edit().putString("token",token).apply();
    }
    public String getToken(){
        return context.getSharedPreferences("token",Context.MODE_PRIVATE).getString("token","");
    }
    public void clearSharedPreferences() {
        context.getSharedPreferences("token",Context.MODE_PRIVATE).edit().clear().apply();
    }
}
