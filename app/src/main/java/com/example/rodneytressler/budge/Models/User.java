package com.example.rodneytressler.budge.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by jacobwilliams on 11/7/16.
 */

public class User {
    @SerializedName("email")
    private String Email;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("AvatarBase64")
    private String avatarBase64;

    @SerializedName("ApiKey")
    private String apiKey;

    @SerializedName("password")
    private String password;

    @SerializedName(".expires")
    private Date expires;

    @SerializedName("grant_type")
    private String grantType;

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("Longitude")
    private Double longitude;

    @SerializedName("Latitude")
    private Double latitude;

    @SerializedName("UserName")
    private String userName;

    @SerializedName("CaughtUserId")
    private String caughtUserId;

    @SerializedName("UserId")
    private String id;

    @SerializedName("RadiusInMeters")
    private Float radius;

    @SerializedName("Created")
    private String created;

    public User(String fullName, String avatarBase64) {
        this.fullName=fullName;
        this.avatarBase64 = avatarBase64;

    }

    public User(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public User(String caughtUserId, float radius) {
        this.caughtUserId = caughtUserId;
        this.radius = radius;
    }

    public User(String username, String password, String grantType) {
        this.grantType = grantType;
        this.Email = username;
        this.password = password;
    }
    public User(String userId, String userName, String avatarBase64, Double longitude, Double latitude, String created) {
        this.id = userId;
        this.userName = userName;
        this.created = created;
        this.avatarBase64 = avatarBase64;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public User(String fullName, String email, String password, String grantType, String apiKey, String avatarBase64) {
        this.fullName = fullName;
        this.Email = email;
        this.password = password;
        this.grantType = grantType;
        this.apiKey = apiKey;
        this.avatarBase64 = avatarBase64;

    }


    public void setFullName(String fullName) {
        this.fullName = fullName;

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

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
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

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getGrant_type() {
        return grantType;
    }

    public void setGrant_type(String grant_type) {
        this.grantType = grant_type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public String getCaughtUserId() {
        return caughtUserId;
    }

    public void setCaughtUserId(String caughtUserId) {
        this.caughtUserId = caughtUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
