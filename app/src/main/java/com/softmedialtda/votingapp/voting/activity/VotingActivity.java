package com.softmedialtda.votingapp.voting.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import com.softmedialtda.votingapp.*;
import com.softmedialtda.votingapp.dashboard.activity.DashboardActivity;
import com.softmedialtda.votingapp.dashboard.domain.Voting;
import com.softmedialtda.votingapp.login.domain.User;
import com.softmedialtda.votingapp.util.MyDividerItemDecoration;
import com.softmedialtda.votingapp.voting.domain.Candidate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.softmedialtda.votingapp.util.Common.getListCandidate;
import static com.softmedialtda.votingapp.util.Connection.sendPost;
import static com.softmedialtda.votingapp.util.Constants.DOMAIN;

public class VotingActivity extends AppCompatActivity implements CandidateAdapter.CandidateAdapterListener {
    private SearchView searchView;
    String url = DOMAIN+"candidate";
    String urlVote = DOMAIN+"vote";
    User user;
    Voting voting;
    public static CandidateAdapter.CandidateAdapterListener mContext;
    public  static Context context;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));


        user = (User) getIntent().getSerializableExtra("user");
        voting = (Voting) getIntent().getSerializableExtra("voting");
        context = this;
        mContext = this;
        if (voting.getId() == 0){
            new AlertDialog.Builder(context).setTitle(R.string.notElectionDay).setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intentBack = new Intent(VotingActivity.this, DashboardActivity.class);
                    intentBack.putExtra("user",user);
                    startActivity(intentBack);
                }
            }).show();
        }else {
            new HttpAsyncTask().execute(url);
        }

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VotingActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getResources().getString(R.string.pleaseWhait));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("")){
                try{
                    JSONArray response = new JSONArray(s);
                    ArrayList<Candidate> list = getListCandidate(response);
                    if (list.size() == 0){

                    }else{
                        CandidateAdapter adapter = new CandidateAdapter(context,list,mContext);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
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
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return sendPost(url,paramaters);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_search:
                return true;
            case android.R.id.home:
                Intent intentBack = new Intent(VotingActivity.this, DashboardActivity.class);
                intentBack.putExtra("user",user);
                startActivity(intentBack);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    private void confirmDialog(final Candidate candidate){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.questionVote).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new HttpVoteAsyncTask().execute(String.valueOf(candidate.getId()));
            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    private class HttpVoteAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("")){
                if (s.equals("1")){
                    new AlertDialog.Builder(context).setTitle(R.string.votoCast).setNeutralButton(R.string.ok,null).show();
                }else{
                    new AlertDialog.Builder(context).setTitle(R.string.error).setNeutralButton(R.string.ok,null).show();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject paramaters = new JSONObject();
            String idCandidate = params[0];
            if (!idCandidate.equals("")) {
                try {
                    paramaters.accumulate("IDVOTING", voting.getId());
                    paramaters.accumulate("IDCANDIDATE", idCandidate);
                    paramaters.accumulate("IDALUMNO", user.getId());
                    paramaters.accumulate("IDINSTITUCION", user.getIdInstitution());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return sendPost(urlVote, paramaters);
            }else{
                return null;
            }

        }
    }

    @Override
    public void onCandidateSelected(Candidate candidate){
        if (voting.getUserVoted() == 1){
            new AlertDialog.Builder(context).setTitle(R.string.userHasAlreadyVoted).setNeutralButton(R.string.ok,null).show();
        }else{
            confirmDialog(candidate);
        }
    }

}
