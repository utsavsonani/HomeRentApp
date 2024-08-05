package com.example.homerentapp.util;

import android.app.Application;

public class HomeRentApi extends Application {
    private String userName;
    private String userId;
    private String phoneNumber;
    private String userType;
    private static HomeRentApi instance;

    public HomeRentApi() {
        
    }

    public static HomeRentApi getInstance() {
        if(instance == null)
            instance = new HomeRentApi();
        return instance;
    }



    public HomeRentApi(String userName, String userId, String phoneNumber, String userType) {
        this.userName = userName;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }




    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
