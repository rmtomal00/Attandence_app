package com.tomal.attandenceapp.common;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogProgress {

    private final Context context;


    public DialogProgress(Context context) {
        this.context = context;
    }

    private ProgressDialog progressBar;

    public void showDialog( String message, boolean isCancelable, String title){
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(isCancelable);
        progressBar.setMessage(message);
        progressBar.setTitle(title);
        progressBar.create();
        progressBar.show();
    }

    public void hideDialog(){
        if(progressBar!=null){
            progressBar.dismiss();
        }
    }
}
