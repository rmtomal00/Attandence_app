package com.tomal.attandenceapp.register;

import androidx.lifecycle.ViewModel;

public class Response extends ViewModel {

    private String status;
    private String message;
    private boolean error;

    public Response(String status, String message, boolean error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }
    public Response(){
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
