package com.softmedialtda.votingapp.voting.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.widget.Filterable;
import android.widget.Filter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.softmedialtda.votingapp.R;
import com.softmedialtda.votingapp.voting.domain.Candidate;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.bitmap;

/**
 * Created by Agustin on 25/6/2018.
 */

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Candidate> candidateList;
    private List<Candidate> candidateListFiltered;
    private CandidateAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, grade,group, tvoto;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            grade = (TextView) view.findViewById(R.id.grade);
            group = (TextView) view.findViewById(R.id.group);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            tvoto = (TextView) view.findViewById(R.id.tvoto);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onCandidateSelected(candidateListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public CandidateAdapter(Context context, List<Candidate> candidateList, CandidateAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.candidateList = candidateList;
        this.candidateListFiltered = candidateList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.candidate_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Candidate candidate = candidateListFiltered.get(position);
        holder.name.setText(candidate.getName());
        holder.grade.setText("Grado : "+candidate.getGrade());
        holder.group.setText("Grupo : "+candidate.getGroup());

        if (candidate.gettVoto() != null){
            holder.tvoto.setText(candidate.gettVoto()+" Votos");
            holder.tvoto.setVisibility(View.VISIBLE);
        }

        if (candidate.getImage().equals("")){
            holder.thumbnail.setImageResource(R.mipmap.photodefault);
        }else{
                //Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(candidate.getImage()).getContent());

            byte[] decodedString = Base64.decode(candidate.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Bitmap circleBitmap = Bitmap.createBitmap(decodedByte.getWidth(), decodedByte.getHeight(), Bitmap.Config.ARGB_8888);
            BitmapShader shader = new BitmapShader(decodedByte,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setShader(shader);
            paint.setAntiAlias(true);
            Canvas c = new Canvas(circleBitmap);

            float width = decodedByte.getWidth()/2; //60
            float height = decodedByte.getHeight()/2; //80

            c.drawCircle(decodedByte.getWidth()/2, decodedByte.getHeight()/2, decodedByte.getWidth()/2, paint);

            holder.thumbnail.setImageBitmap(circleBitmap);

        }

        /*Glide.with(context)
                .load(candidate.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);*/
    }

    @Override
    public int getItemCount() {
        return candidateListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    candidateListFiltered = candidateList;
                } else {
                    List<Candidate> filteredList = new ArrayList<>();
                    for (Candidate row : candidateList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getGrade().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    candidateListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = candidateListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                candidateListFiltered = (ArrayList<Candidate>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CandidateAdapterListener {
        void onCandidateSelected(Candidate contact);
    }
}
