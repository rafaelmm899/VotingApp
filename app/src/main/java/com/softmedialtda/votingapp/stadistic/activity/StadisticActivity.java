package com.softmedialtda.votingapp.stadistic.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
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
import java.util.List;

import static com.softmedialtda.votingapp.util.Common.getListCandidate;
import static com.softmedialtda.votingapp.util.Common.getListCandidateWithNumVote;
import static com.softmedialtda.votingapp.util.Common.setListLegentEntry;
import static com.softmedialtda.votingapp.util.Common.setListPieEntry;
import static com.softmedialtda.votingapp.util.Connection.sendPost;
import static com.softmedialtda.votingapp.util.Constants.COLORS;
import static com.softmedialtda.votingapp.util.Constants.DOMAIN;

public class StadisticActivity extends AppCompatActivity implements CandidateAdapter.CandidateAdapterListener  {
    User user;
    Voting voting;
    String url = DOMAIN+"stadistic";
    public static CandidateAdapter.CandidateAdapterListener mContext;
    public  static Context context;
    private RecyclerView recyclerView;
    private PieChart pieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadistic);

        user = (User) getIntent().getSerializableExtra("user");
        voting = (Voting) getIntent().getSerializableExtra("voting");

        pieChart = (PieChart) findViewById(R.id.graphic);


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

    private Chart getSameChart(Chart chart, String description, int textColor, int background, int animate, ArrayList<LegendEntry> entries){
        chart.getDescription().setText(description);
        chart.getDescription().setTextColor(textColor);
        chart.getDescription().setTextSize(15);
        int height = pieChart.getHeight();
        int width = pieChart.getWidth();
        chart.getDescription().setPosition((width / 2),height);
        chart.setBackgroundColor(background);
        chart.animateY(animate);
        legendGraphic(chart,entries);
        return chart;
    }

    private void legendGraphic(Chart chart, ArrayList<LegendEntry> entries){
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        //legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        //legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);

        legend.setCustom(entries);

    }

    private DataSet getData(DataSet dataSet){
        dataSet.setColors(COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10);
        return dataSet;
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


                    List<PieEntry> entries = setListPieEntry(candidates,numVoters);
                    ArrayList<LegendEntry> legendEntries = setListLegentEntry(candidates);
                    /*PieDataSet set = new PieDataSet(entries, "Election Results");
                    PieData data = new PieData(set);
                    pieChart.setData(data);
                    pieChart.invalidate(); // refresh*/


                    pieChart = (PieChart) getSameChart(pieChart,voting.getName(), Color.BLACK,Color.GRAY,3000, legendEntries);
                    pieChart.setHoleRadius(64);
                    pieChart.setCenterText("Total de votantes "+String.valueOf(numVoters));
                    pieChart.setTransparentCircleRadius(8);
                    PieDataSet set = (PieDataSet)getData(new PieDataSet(entries, ""));

                    set.setSliceSpace(2);
                    set.setValueFormatter(new PercentFormatter());
                    set.setColors(COLORS);
                    PieData data = new PieData(set);
                    pieChart.setData(data);
                    pieChart.invalidate();


                }catch (JSONException e){

                }
            }
        }
    }
}
