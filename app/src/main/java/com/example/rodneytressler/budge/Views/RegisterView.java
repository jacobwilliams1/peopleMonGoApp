package com.example.rodneytressler.budge.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rodneytressler.budge.Models.User;
import com.example.rodneytressler.budge.PeopleMonGo;
import com.example.rodneytressler.budge.Models.Account;
import com.example.rodneytressler.budge.Network.RestClient;
import com.example.rodneytressler.budge.Stages.LoginStage;
import com.example.rodneytressler.budget.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jacobwilliams on 11/7/16.
 */

public class RegisterView extends LinearLayout {
    private Context context;

    @Bind(R.id.username_field)
    EditText usernameField;

    @Bind(R.id.email_field)
    EditText emailField;

    @Bind(R.id.password_field)
    EditText passwordField;

    @Bind(R.id.confirm_field)
    EditText confirmField;

    @Bind(R.id.register_button)
    Button registerButton;

    @Bind(R.id.spinner)
    ProgressBar spinner;

    public RegisterView(Context context, AttributeSet attrs) {
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
    public void login() {
        //drop the keyboard off the screen when the user hits the button.
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(emailField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(confirmField.getWindowToken(), 0);

        // gets the value from each text field.
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String confirm = confirmField.getText().toString();
        String email = emailField.getText().toString();

        // if they don't have both fields filled out we throw a toast at them.
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty() || email.isEmpty()) {
            Toast.makeText(context, "Must provide all info", Toast.LENGTH_LONG).show();
            // email address isn't valid.
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Must be valid email", Toast.LENGTH_LONG).show();
            // passwords don;t match!
        } else if (!password.equals(confirm)) {
            Toast.makeText(context, "passwords don't match", Toast.LENGTH_LONG).show();
        } else {
            //if they give us both a username and password, we disable the register button so that it can't be spammed.
            registerButton.setEnabled(false);
            spinner.setVisibility(VISIBLE);
            String grant_type = "password";

            String apiKey = "iOSandroid301november2016";

            String avatarBase64 = "";

            User account = new User(username, email, password, grant_type, apiKey, avatarBase64);
            RestClient restClient = new RestClient();
            restClient.getApiService().register(account).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
//                        Account regUser = response.body();
//                        UserStore.getInstance().setToken(regUser.getToken());
//                        UserStore.getInstance().setTokenExpiration(regUser.getExpiration());

                        Flow flow = PeopleMonGo.getMainFlow();
                        History newHistory = History.single(new LoginStage());
                        flow.setHistory(newHistory, Flow.Direction.BACKWARD);
                    } else {
                        resetView();
                        Toast.makeText(context, "registration failed", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    resetView();
                    Toast.makeText(context, "registration failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    // so if we time out or log out- we can see the buttons again.
    private void resetView() {
        registerButton.setEnabled(true);
        spinner.setVisibility(GONE);
    }

}