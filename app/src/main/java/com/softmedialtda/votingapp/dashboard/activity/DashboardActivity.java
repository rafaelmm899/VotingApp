package com.softmedialtda.votingapp.dashboard.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.softmedialtda.votingapp.R;
import com.softmedialtda.votingapp.calendar.activity.CalendarActivity;
import com.softmedialtda.votingapp.campaign.activity.CampaignActivity;
import com.softmedialtda.votingapp.dashboard.domain.Voting;
import com.softmedialtda.votingapp.login.activity.LoginActivity;
import com.softmedialtda.votingapp.login.domain.User;
import com.softmedialtda.votingapp.stadistic.activity.StadisticActivity;
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
    ProgressDialog progressDialog;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        user = (User) getIntent().getSerializableExtra("user");
        new HttpVotingAsyncTask().execute(url);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout votingButton = (LinearLayout) findViewById(R.id.votingButton);
        votingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener(v);
            }
        });

        LinearLayout campaignButton = (LinearLayout) findViewById(R.id.campaignButton);
        campaignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener(v);
            }
        });

        LinearLayout stadisticButton = (LinearLayout) findViewById(R.id.stadisticsButton);
        stadisticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener(v);
            }
        });

        LinearLayout calendarButton = (LinearLayout) findViewById(R.id.calendar);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener(v);
            }
        });
        context = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.logout:

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.logout_confirm).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                        user = new User();
                        voting = new Voting();
                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clickListener (View v){
        switch (v.getId()){
            case R.id.votingButton:
                Intent intentVoting = new Intent(DashboardActivity.this, VotingActivity.class);
                intentVoting.putExtra("user",user);
                intentVoting.putExtra("voting",voting);
                startActivity(intentVoting);
                break;
            case R.id.campaignButton:
                Intent intentCampaign = new Intent(DashboardActivity.this, CampaignActivity.class);
                intentCampaign.putExtra("user",user);
                intentCampaign.putExtra("voting",voting);
                startActivity(intentCampaign);
                break;
            case R.id.stadisticsButton:
                Intent intentStadistic = new Intent(DashboardActivity.this, StadisticActivity.class);
                intentStadistic.putExtra("user",user);
                intentStadistic.putExtra("voting",voting);
                startActivity(intentStadistic);
                break;
            case R.id.calendar:
                Intent intentCalendar = new Intent(DashboardActivity.this, CalendarActivity.class);
                intentCalendar.putExtra("user",user);
                startActivity(intentCalendar);
        }

    }

    private class HttpVotingAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DashboardActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getResources().getString(R.string.pleaseWhait));
            progressDialog.show();
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
            progressDialog.hide();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject paramaters = new JSONObject();
            try {
                paramaters.accumulate("ID_INSTITUCION", user.getIdInstitution());
                paramaters.accumulate("IDALUMNO", user.getId());
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return sendPost(url,paramaters);
        }
    }
}


