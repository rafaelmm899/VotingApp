package com.softmedialtda.votingapp.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.softmedialtda.votingapp.R;
import com.softmedialtda.votingapp.dashboard.activity.DashboardActivity;
import com.softmedialtda.votingapp.login.domain.User;

import org.json.JSONException;
import org.json.JSONObject;
import android.view.View.OnClickListener;
import static com.softmedialtda.votingapp.util.Connection.*;
import static com.softmedialtda.votingapp.util.Constants.*;
import static com.softmedialtda.votingapp.util.Common.*;

public class LoginActivity extends Activity implements OnClickListener {

    String url = DOMAIN+"login";
    String username, password;
    EditText usernameEditText,passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }

    public void userLoggedIn(JSONObject response){
        User user = convertJsonObjectToUserObject(response);
        if (user.getId() == 0){
            showMessage(getResources().getString(R.string.errorUserObjectIsNull),getBaseContext());
        }else{
            showMessage(getResources().getString(R.string.userLoggedIn),getBaseContext());
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }

    }

    private class LoginAsyncTask extends AsyncTask<String,Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("")) {
                try {
                    JSONObject response = new JSONObject(s);
                    String typeResponse = response.getString("RESPONSE").trim();
                    if (!typeResponse.equals("")){
                        switch (typeResponse){
                            case "2":
                                //usuario logueado satisfactoriamente
                                userLoggedIn(response);
                                break;
                            case "3":
                                showMessage(getResources().getString(R.string.invalidUsernameOrPassword),getBaseContext());

                                break;
                            case "4":
                                showMessage(getResources().getString(R.string.requiredFields),getBaseContext());

                                break;
                        }
                    }else{
                        showMessage(getResources().getString(R.string.authenticationError),getBaseContext());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                showMessage(getResources().getString(R.string.authenticationError),getBaseContext());

            }
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject paramaters = new JSONObject();
            try {
                paramaters.accumulate("username", username);
                paramaters.accumulate("password", password);
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return sendPost(url,paramaters);
        }
    }

    @Override
    public void onClick(View v) {
        if (!usernameEditText.getText().toString().trim().equals("")&&!passwordEditText.getText().toString().trim().equals("")) {
            switch (v.getId()) {
                case R.id.enterLogin:
                    username = usernameEditText.getText().toString().toUpperCase();
                    password = passwordEditText.getText().toString();
                    new LoginAsyncTask().execute(url);
                    break;
            }
        }
    }
}
