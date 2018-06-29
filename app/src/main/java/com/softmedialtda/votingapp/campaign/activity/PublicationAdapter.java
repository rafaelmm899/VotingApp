package com.softmedialtda.votingapp.campaign.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import com.softmedialtda.votingapp.R;
import com.softmedialtda.votingapp.campaign.domain.Publication;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agustin on 29/6/2018.
 */

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Publication> list;
    private List<Publication> listFiltered;
    private PublicationAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameCandidate, publicationDate, publicationText;
        public ImageView photoCandidate;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameCandidate = (TextView) itemView.findViewById(R.id.nameCandidate);
            publicationDate = (TextView) itemView.findViewById(R.id.publicationDate);
            publicationText = (TextView) itemView.findViewById(R.id.publicationText);
            photoCandidate = (ImageView) itemView.findViewById(R.id.photoCandidate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPublicatedSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public PublicationAdapter(Context context, List<Publication> list, PublicationAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listFiltered = list;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Publication publication = listFiltered.get(position);
        holder.nameCandidate.setText(publication.getNameStudent());
        holder.publicationDate.setText(publication.getDate());
        holder.publicationText.setText(publication.getText());

        if (publication.getImageStudent().equals("")){
            holder.photoCandidate.setImageResource(R.mipmap.photodefault);
        }else{
            try {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(publication.getImageStudent()).getContent());
                holder.photoCandidate.setImageBitmap(bitmap);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.publication_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = list;
                } else {
                    List<Publication> filteredList = new ArrayList<>();
                    for (Publication row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNameStudent().toLowerCase().contains(charString.toLowerCase()) || row.getDate().contains(charSequence)|| row.getText().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    listFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (ArrayList<Publication>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public  interface PublicationAdapterListener{
        void onPublicatedSelected(Publication publication);
    }
}
