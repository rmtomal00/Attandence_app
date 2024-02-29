package com.tomal.attandenceapp.picturesendtoserver;

public class PicModel {

    String message;
    int code;
    boolean error;

    public PicModel(String message, int code, boolean error) {
        this.message = message;
        this.code = code;
        this.error = error;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
