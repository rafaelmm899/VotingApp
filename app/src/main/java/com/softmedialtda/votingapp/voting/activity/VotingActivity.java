package com.softmedialtda.votingapp.voting.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.ViewGroup;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softmedialtda.votingapp.*;
import com.softmedialtda.votingapp.dashboard.activity.DashboardActivity;
import com.softmedialtda.votingapp.dashboard.domain.Voting;
import com.softmedialtda.votingapp.login.domain.User;
import com.softmedialtda.votingapp.util.MyDividerItemDecoration;
import com.softmedialtda.votingapp.voting.domain.Candidate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.softmedialtda.votingapp.util.Common.addBorderToCircularBitmap;
import static com.softmedialtda.votingapp.util.Common.addShadowToCircularBitmap;
import static com.softmedialtda.votingapp.util.Common.getCircularBitmap;
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
    TextView votingName;
    LinearLayout votingDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        votingName = (TextView) findViewById(R.id.VotingName);
        votingDescription = (LinearLayout) findViewById(R.id.votingDescription);

        votingDescription.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.candidateTitle));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();

        marginLayoutParams.setMargins(0,200,0,0);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        
        user = (User) getIntent().getSerializableExtra("user");
        voting = (Voting) getIntent().getSerializableExtra("voting");
        context = this;
        mContext = this;
        if (voting.getId() == 0 || !voting.isActive()){
            new AlertDialog.Builder(context).setMessage(R.string.notElectionDay).setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intentBack = new Intent(VotingActivity.this, DashboardActivity.class);
                    intentBack.putExtra("user",user);
                    startActivity(intentBack);
                }
            }).show();
        }else {
            votingName.setText(voting.getName());
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

        final Dialog MyDialog = new Dialog(context);

        MyDialog.setContentView(R.layout.vote_dialog_alert);
        ImageView dialogImageView = (ImageView) MyDialog.findViewById(R.id.dialogImageView);
        Button yesVote = (Button)MyDialog.findViewById(R.id.yesVote);
        Button noVote = (Button)MyDialog.findViewById(R.id.noVote);

        yesVote.setEnabled(true);
        noVote.setEnabled(true);

        if (candidate.getImage().equals("")){
            dialogImageView.setImageResource(R.mipmap.photodefault);
        }else{
            byte[] decodedString = Base64.decode(candidate.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            decodedByte = getCircularBitmap(decodedByte);
            decodedByte = addBorderToCircularBitmap(decodedByte, 15, Color.WHITE);
            decodedByte = addShadowToCircularBitmap(decodedByte, 4, Color.LTGRAY);

            dialogImageView.setImageBitmap(decodedByte);
        }

        yesVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpVoteAsyncTask().execute(String.valueOf(candidate.getId()));
            }
        });
        noVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();


        /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        }).show();*/
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
        /*if (voting.getUserVoted() == 1){
            new AlertDialog.Builder(context).setTitle(R.string.userHasAlreadyVoted).setNeutralButton(R.string.ok,null).show();
        }else{*/
            confirmDialog(candidate);
        //}
    }

}
