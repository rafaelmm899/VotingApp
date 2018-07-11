package com.softmedialtda.votingapp.voting.activity;

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

import com.softmedialtda.votingapp.R;
import com.softmedialtda.votingapp.dashboard.domain.Voting;
import com.softmedialtda.votingapp.login.domain.User;
import com.softmedialtda.votingapp.stadistic.activity.StadisticActivity;
import com.softmedialtda.votingapp.util.MyDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.softmedialtda.votingapp.util.Common.getListVoting;
import static com.softmedialtda.votingapp.util.Common.showMessage;
import static com.softmedialtda.votingapp.util.Connection.sendPost;
import static com.softmedialtda.votingapp.util.Constants.DOMAIN;

public class ListVotingActivity extends AppCompatActivity implements VotingAdapter.VotingAdapterListener {

    String url = DOMAIN+"votinglist";
    User user;

    private RecyclerView recyclerView;
    public  static Context context;
    public static VotingAdapter.VotingAdapterListener mContext;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_voting);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));

        context = this;
        mContext = this;

        user = (User) getIntent().getSerializableExtra("user");
        new HttpListVotingAsyncTask().execute(url);
    }

    @Override
    public void onVotingSelected(Voting voting) {
        Intent intentVoting = new Intent(ListVotingActivity.this, StadisticActivity.class);
        intentVoting.putExtra("user",user);
        intentVoting.putExtra("voting",voting);
        startActivity(intentVoting);
    }

    private class HttpListVotingAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            JSONObject paramaters = new JSONObject();
            try {
                paramaters.accumulate("IDINSTITUCION", user.getIdInstitution());
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return sendPost(url,paramaters);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ListVotingActivity.this,
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
                    ArrayList<Voting> list = getListVoting(response);
                    if (list.size() > 0){
                        VotingAdapter adapter = new VotingAdapter(context,list,mContext);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressDialog.hide();
        }
    }
}
