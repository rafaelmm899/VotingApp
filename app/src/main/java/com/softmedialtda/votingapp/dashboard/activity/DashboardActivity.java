package com.softmedialtda.votingapp.dashboard.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.softmedialtda.votingapp.R;
import com.softmedialtda.votingapp.dashboard.domain.Voting;
import com.softmedialtda.votingapp.login.activity.LoginActivity;
import com.softmedialtda.votingapp.login.domain.User;
import com.softmedialtda.votingapp.voting.activity.VotingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.softmedialtda.votingapp.util.Common.getCurrentVotes;
import static com.softmedialtda.votingapp.util.Connection.sendPost;
import static com.softmedialtda.votingapp.util.Constants.DOMAIN;

/**
 * Created by Agustin on 19/6/2018.
 */

public class DashboardActivity extends AppCompatActivity {
    User user;
    String url = DOMAIN+"voting";
    Voting voting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        user = (User) getIntent().getSerializableExtra("user");
        LinearLayout votingButton = (LinearLayout) findViewById(R.id.votingButton);
        votingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, VotingActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
    }

    private class HttpVotingAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("")){
                try {
                    JSONArray response = new JSONArray(s);
                    JSONObject data = response.getJSONObject(0);
                    voting = getCurrentVotes(data);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject paramaters = new JSONObject();
            try {
                paramaters.accumulate("ID_INSTITUCION", user.getIdInstitution());
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return sendPost(url,paramaters);
        }
    }
}


