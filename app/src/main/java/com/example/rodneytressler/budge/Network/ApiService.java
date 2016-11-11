package com.example.rodneytressler.budge.Network;

import com.example.rodneytressler.budge.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by jacobwilliams on 11/7/16.
 */

public interface ApiService {

    @POST("/api/Account/Register")
    Call<Void> register(@Body User user);

    @FormUrlEncoded
    @POST("token")
    Call<User> login(@Field("username") String email, @Field("password") String password, @Field("grant_type") String grantType);

    @POST("/v1/User/CheckIn")
    Call<Void> checkIn(@Body User user);

    @GET("/v1/User/Nearby")
    Call<User[]> nearby(@Query("radiusInMeters")Integer radius);

    @POST("/v1/User/Catch")
    Call<Void> caught(@Body User user);

    @GET("/api/Account/UserInfo")
    Call<User> userInfo();

    @GET ("/v1/User/Caught")
    Call<User[]> caughtUsers();

    @POST("/api/Account/UserInfo")
    Call<Void> updateInfo(@Body User user);



}
