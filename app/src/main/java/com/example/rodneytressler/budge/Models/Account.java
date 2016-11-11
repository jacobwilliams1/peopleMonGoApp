package com.example.rodneytressler.budge.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by jacobwilliams on 11/7/16.
 */

public class Account {
    @SerializedName("email")
    private String Email;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("avatarBase64")
    private String avatarBase64 = "";

    @SerializedName("ApiKey")
    private String apiKey = "iOSandroid301november2016";

    @SerializedName("password")
    private String password;

    @SerializedName("access_token")
    private String access_token;

    @SerializedName(".expires")
    private Date expiration;

    @SerializedName("grant_type")
    private String grantType;

    public Account() {
    }


    public Account(String email, String password) {
        Email = email;
        this.password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatarBase64() {
        return avatarBase64;
    }

    public void setAvatarBase64(String avatarBase64) {
        this.avatarBase64 = avatarBase64;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String token) {
        this.access_token = token;
    }

    public Date getExpires() {
        return expiration;
    }

    public void setExpires(Date expiration) {
        this.expiration = expiration;
    }
}
