package com.example.rodneytressler.budge.Views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.rodneytressler.budge.Adapters.NearbyPeopleListAdapter;
import com.example.rodneytressler.budge.Models.User;
import com.example.rodneytressler.budge.Network.RestClient;
import com.example.rodneytressler.budget.R;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jacobwilliams on 11/11/16.
 */

public class NearbyPeopleListView extends LinearLayout {
    private Context context;
    private NearbyPeopleListAdapter nearbyAdapter;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    //    private CaughtPeopleListAdapter caught;
    public NearbyPeopleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        nearbyAdapter = new NearbyPeopleListAdapter(new ArrayList<User>(), context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(nearbyAdapter);
        listCaughtPeople();
    }
    private void listCaughtPeople(){
        RestClient restClient = new RestClient();
        restClient.getApiService().nearby(500).enqueue(new Callback<User[]>() {
            @Override
            public void onResponse(Call<User[]> call, Response<User[]> response) {
                // Is the server response between 200 to 299
                if (response.isSuccessful()){
                    nearbyAdapter.users = new ArrayList<>(Arrays.asList(response.body()));
                    for (User user  : nearbyAdapter.users) {
                        nearbyAdapter.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(context,"Get User Info Failed" + ": " + response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<User[]> call, Throwable t) {
                Toast.makeText(context,"Get User Info Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}