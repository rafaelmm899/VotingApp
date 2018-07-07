package com.softmedialtda.votingapp.stadistic.activity;

import android.content.Context;
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
import com.softmedialtda.votingapp.util.MyDividerItemDecoration;
import com.softmedialtda.votingapp.voting.activity.CandidateAdapter;
import com.softmedialtda.votingapp.voting.domain.Candidate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.softmedialtda.votingapp.util.Common.getListCandidate;
import static com.softmedialtda.votingapp.util.Common.getListCandidateWithNumVote;
import static com.softmedialtda.votingapp.util.Connection.sendPost;
import static com.softmedialtda.votingapp.util.Constants.DOMAIN;

public class StadisticActivity extends AppCompatActivity implements CandidateAdapter.CandidateAdapterListener  {
    User user;
    Voting voting;
    String url = DOMAIN+"stadistic";
    public static CandidateAdapter.CandidateAdapterListener mContext;
    public  static Context context;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadistic);

        user = (User) getIntent().getSerializableExtra("user");
        voting = (Voting) getIntent().getSerializableExtra("voting");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        new HttpStadisticsAsyncTask().execute(url);
    }

    @Override
    public void onCandidateSelected(Candidate contact) {

    }

    private class HttpStadisticsAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            JSONObject paramaters = new JSONObject();
            try {
                paramaters.accumulate("IDINSTITUCION", user.getIdInstitution());
                paramaters.accumulate("IDVOTING", voting.getId());
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return sendPost(url,paramaters);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("")){
                try{
                    JSONObject response = new JSONObject(s);


                    ArrayList<Candidate> candidates = getListCandidateWithNumVote(response.getJSONArray("CANDIDATE"));
                    if (candidates.size() > 0){
                        CandidateAdapter adapter = new CandidateAdapter(context,candidates,mContext);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    int numStudents = response.getInt("NUMSTUDENTS");
                    int numVoters = response.getInt("NUMVOTERS");

                }catch (JSONException e){

                }
            }
        }
    }
}
