package com.softmedialtda.votingapp.dashboard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.softmedialtda.votingapp.R;
import com.softmedialtda.votingapp.login.activity.LoginActivity;
import com.softmedialtda.votingapp.login.domain.User;
import com.softmedialtda.votingapp.voting.activity.VotingActivity;

/**
 * Created by Agustin on 19/6/2018.
 */

public class DashboardActivity extends AppCompatActivity {
    User user;

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
}


