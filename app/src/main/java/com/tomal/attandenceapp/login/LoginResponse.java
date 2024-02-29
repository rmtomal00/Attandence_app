package com.tomal.attandenceapp.login;

import org.json.JSONObject;

import java.util.Map;

public class LoginResponse {
    private int status;

    private boolean error;
    private String message;
    private Data data;

    public LoginResponse(int status, boolean error, String message, Data data) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}

class Data{
    String token;

    public Data(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
