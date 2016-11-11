package com.example.rodneytressler.budge.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rodneytressler.budge.Models.Account;
import com.example.rodneytressler.budge.PeopleMonGo;
import com.example.rodneytressler.budge.Models.User;
import com.example.rodneytressler.budge.Network.RestClient;
import com.example.rodneytressler.budge.Network.UserStore;
import com.example.rodneytressler.budge.Stages.MapStage;
import com.example.rodneytressler.budge.Stages.RegisterStage;
import com.example.rodneytressler.budget.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rodneytressler.budge.Components.Constants.grantType;

/**
 * Created by jacobwilliams on 11/7/16.
 */

public class LoginView extends LinearLayout {
    private Context context;

    @Bind(R.id.username_field)
    EditText usernameField;

    @Bind(R.id.password_field)
    EditText passwordField;

    @Bind(R.id.login_button)
    Button loginButton;

    @Bind(R.id.register_button)
    Button registerButton;

    @Bind(R.id.spinner)
    ProgressBar spinner;

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    //looking at the life cycle of a view.
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_button)
    public void showRegisterView() {
        Flow flow = PeopleMonGo.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new RegisterStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }

    @OnClick(R.id.login_button)
    public void login() {
        //drop the keyboard off the screen when the user hits the button.
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);

        // gets the username and password.
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        final String grant_type = "password";

        // if they don't have both fields filled out we throw a toast at them.
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Must fill both fields", Toast.LENGTH_LONG).show();
        } else {
            //if they give us both a username and password, we disable the login button so that it can't be spammed.
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
            spinner.setVisibility(VISIBLE);

            RestClient restClient = new RestClient();
            restClient.getApiService().login(username ,password, grantType).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    // now, here's the tricky thing. we have to make sure the password and username is correct
                    // isSuccessful checks if the server goes between 200 and 299.
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "YOU LOGGED IN", Toast.LENGTH_LONG).show();
                        Toast.makeText(context, grant_type, Toast.LENGTH_LONG).show();

                        //if the response is right, we wanna pass it in.


                        User authUser = response.body();
                        UserStore.getInstance().setToken(authUser.getAccess_token());
                        UserStore.getInstance().setTokenExpiration(authUser.getExpires());
                        Toast.makeText(context, UserStore.getInstance().getToken().toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(context, UserStore.getInstance().getTokenExpiration().toString(), Toast.LENGTH_LONG).show();




                        Flow flow = PeopleMonGo.getMainFlow();
                        History newHistory = History.single(new MapStage());
                        flow.setHistory(newHistory, Flow.Direction.REPLACE);

                    } else {
                        // so if the username and password is wrong.
                        resetView();
                        Toast.makeText(context, R.string.login_failed + ": " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // if there's a time out or another error it resets the buttons and then pops up with a try again.
                    resetView();
                    Toast.makeText(context, R.string.login_failed, Toast.LENGTH_LONG).show();
                }
            });
        }

        //

    }

    // so if we time out or log out- we can see the buttons again.
    private void resetView() {
        loginButton.setEnabled(true);
        registerButton.setEnabled(true);
        spinner.setVisibility(GONE);
    }
}

