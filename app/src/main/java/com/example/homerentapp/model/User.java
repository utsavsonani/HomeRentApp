package com.example.homerentapp.model;

public class User {
    private String name;
    private String email;
    private String mobileNumber;
    private String userType;
    private String password;


    public User(String name, String email, String mobileNumber, String userType, String password) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.userType = userType;
        this.password = password;
    }

    // Getters and setters
    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
