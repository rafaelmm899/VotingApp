package com.softmedialtda.votingapp.splash.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.softmedialtda.votingapp.R;
import com.softmedialtda.votingapp.login.activity.LoginActivity;

import static com.softmedialtda.votingapp.util.Constants.SPLASH_TIME_OUT;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);

        CircularProgressBar circularProgressBar = (CircularProgressBar)findViewById(R.id.circularProgressbar);
        circularProgressBar.setProgressWithAnimation(100, SPLASH_TIME_OUT);
    }
}
