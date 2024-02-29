package com.tomal.attandenceapp.usersroute;

import androidx.lifecycle.ViewModel;

public class UserData extends ViewModel {
    private String message;
    private int code;
    private boolean error;
    private UserDataDetails data;

    // Constructors, getters, and setters

    // Default constructor
    public UserData() {
    }

    // Parameterized constructor
    public UserData(String message, int code, boolean error, UserDataDetails data) {
        this.message = message;
        this.code = code;
        this.error = error;
        this.data = data;
    }

    // Getters and Setters for message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getters and Setters for code
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    // Getters and Setters for error
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    // Getters and Setters for data
    public UserDataDetails getData() {
        return data;
    }

    public void setData(UserDataDetails data) {
        this.data = data;
    }
}

class UserDataDetails {
    private String id;
    private String userName;
    private String userMail;
    private String userPhone;
    private String birthDate;
    private String photo;
    private String photoPath;
    private boolean getAttandence;
    // Constructors, getters, and setters

    // Default constructor
    public UserDataDetails() {
    }

    // Parameterized constructor
    public UserDataDetails(String id, String userName, String userMail, String userPhone,
                           String birthDate, String photo, String photoPath, boolean getAttandence) {
        this.id = id;
        this.userName = userName;
        this.userMail = userMail;
        this.userPhone = userPhone;
        this.birthDate = birthDate;
        this.photo = photo;
        this.photoPath = photoPath;
        this.getAttandence = getAttandence;
    }

    public boolean isGetAttandence() {
        return getAttandence;
    }

    public void setGetAttandence(boolean getAttandence) {
        this.getAttandence = getAttandence;
    }

    // Getters and Setters for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getters and Setters for userName
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getters and Setters for userMail
    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    // Getters and Setters for userPhone
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    // Getters and Setters for birthDate
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    // Getters and Setters for photo
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    // Getters and Setters for photoPath
    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
