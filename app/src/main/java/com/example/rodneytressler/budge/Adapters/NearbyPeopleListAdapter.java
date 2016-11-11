package com.example.rodneytressler.budge.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rodneytressler.budge.Models.User;
import com.example.rodneytressler.budget.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jacobwilliams on 11/11/16.
 */

public class NearbyPeopleListAdapter extends RecyclerView.Adapter<NearbyPeopleListAdapter.UserHolder> {
    public ArrayList<User> users;
    private Context context;
    public NearbyPeopleListAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }
    @Override
    public NearbyPeopleListAdapter.UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = LayoutInflater.from(context).inflate(R.layout.nearby_user_item, parent, false);
        return new NearbyPeopleListAdapter.UserHolder(inflateView);
    }
    @Override
    public void onBindViewHolder(NearbyPeopleListAdapter.UserHolder holder, int position) {
        User user = users.get(position);
        holder.bindUser(user);
    }
    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }
    class UserHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.nearby_username)
        TextView nearbyUsername;
        public UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        //        Lets put our data in our UI
        public void bindUser(final User user){
            nearbyUsername.setText(user.getUserName());
        }
    }
}
