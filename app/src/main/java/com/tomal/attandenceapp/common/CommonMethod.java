package com.tomal.attandenceapp.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import org.json.JSONObject;

import java.util.Map;

public class CommonMethod {

    public JSONObject mapToJson(Map<String, Object> map){
        return new JSONObject(map);
    }

    public void showAlert(String title, String message, Context context, Class c, boolean isSuccess){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        String key = "Yes";
        if (!isSuccess){
            key = "No";
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        builder.setNegativeButton(key, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(context, c);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void showAlertUser(String title, String message, Context context, Class c, boolean isSuccess){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(context, c);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
                SharePref pref = new SharePref(context);
                pref.clearSharedPreferences();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
