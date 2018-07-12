package com.softmedialtda.votingapp.calendar.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.softmedialtda.votingapp.R;
import com.softmedialtda.votingapp.dashboard.domain.Voting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agustin on 11/7/2018.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Voting> votingList;
    private List<Voting> votingListFiltered;
    private CalendarAdapterListener listener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Voting voting = votingListFiltered.get(position);
        holder.name.setText(voting.getName());
        holder.dateBegin.setText(voting.getDateBegin());
        holder.dateFinish.setText(voting.getDateFinish());
    }

    @Override
    public int getItemCount() {
        return votingListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    votingListFiltered = votingList;
                } else {
                    List<Voting> filteredList = new ArrayList<>();
                    for (Voting row : votingList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    votingListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = votingListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                votingListFiltered = (ArrayList<Voting>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, dateBegin, dateFinish;
        public MyViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            dateBegin = (TextView) view.findViewById(R.id.dateBegin);
            dateFinish = (TextView) view.findViewById(R.id.dateFinish);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCalendarSelected(votingListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public CalendarAdapter(Context context, List<Voting> votingList, CalendarAdapterListener listener) {
        this.context = context;
        this.votingList = votingList;
        this.votingListFiltered = votingList;
        this.listener = listener;
    }

    public  interface CalendarAdapterListener {
        void onCalendarSelected(Voting voting);
    }
}
