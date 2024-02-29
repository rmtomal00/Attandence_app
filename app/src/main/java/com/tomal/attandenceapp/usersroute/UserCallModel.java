package com.tomal.attandenceapp.usersroute;

public class UserCallModel {
    String token;

    public UserCallModel(String token) {
        this.token = token;
    }

    public UserCallModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
