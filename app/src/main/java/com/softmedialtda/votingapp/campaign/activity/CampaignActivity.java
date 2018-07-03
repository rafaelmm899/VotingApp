package com.softmedialtda.votingapp.campaign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.softmedialtda.votingapp.R;
import com.softmedialtda.votingapp.campaign.domain.Publication;
import com.softmedialtda.votingapp.dashboard.domain.Voting;
import com.softmedialtda.votingapp.util.MyDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.softmedialtda.votingapp.util.Common.*;
import static com.softmedialtda.votingapp.util.Connection.sendPost;
import static com.softmedialtda.votingapp.util.Constants.DOMAIN;
import static com.softmedialtda.votingapp.util.Constants.YOUTUBEKEY;

public class CampaignActivity extends AppCompatActivity implements PublicationAdapter.PublicationAdapterListener {
    private RecyclerView recyclerView;
    String url = DOMAIN+"publication";
    Voting voting;
    public  static Context context;
    public static PublicationAdapter.PublicationAdapterListener mContext;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        voting = (Voting) getIntent().getSerializableExtra("voting");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_campaign);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));

        context = this;
        mContext = this;
        new HttpAsyncTaskCampaign().execute(url);
    }

    private class HttpAsyncTaskCampaign extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CampaignActivity.this,
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
                    ArrayList<Publication> list = getListPublication(response);
                    if (list.size() == 0){

                    }else{
                        PublicationAdapter adapter = new PublicationAdapter(context,list,mContext);
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
            String idCandidate = params[0];
            if (!idCandidate.equals("")) {
                try {
                    paramaters.accumulate("IDVOTING", voting.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return sendPost(url, paramaters);
            }else{
                return null;
            }
        }
    }

    @Override
    public void onPublicatedSelected(Publication publication) {
        try {
            String idVideo =  extractVideoIdFromUrl(publication.getLink());
            Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, YOUTUBEKEY, idVideo,0,false,true);
            this.startActivity(intent);
        }catch (Exception e){
            showMessage(getString(R.string.errorPlayingVideo),this);
        }

    }
}
