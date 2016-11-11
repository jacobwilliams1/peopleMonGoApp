package com.example.rodneytressler.budge.Network;

import com.example.rodneytressler.budge.PeopleMonGo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jacobwilliams on 11/7/16.
 */

public class RestClient {
    private ApiService apiService;

        public RestClient() {
            GsonBuilder builder = new GsonBuilder();
            //sets the format the date will be in.
            builder.setDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
            Gson gson = builder.create();

            HttpLoggingInterceptor log = new HttpLoggingInterceptor();
            log.setLevel(HttpLoggingInterceptor.Level.BODY);


            // the world's smallest monitor
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    //denotes the time.
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(new SessionRequestInterceptor())
                    .addInterceptor(log)
                    .build();

            //sets up the URLs
            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl(PeopleMonGo.API_BASE_URL)
                    .client(okHttpClient)
                    //converts the json classes.
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            apiService = restAdapter.create(ApiService.class);
        }

        public ApiService getApiService(){
            return apiService;
        }
}