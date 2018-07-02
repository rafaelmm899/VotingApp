package com.softmedialtda.votingapp.campaign.activity;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.softmedialtda.votingapp.R;
import com.softmedialtda.votingapp.campaign.domain.Publication;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.softmedialtda.votingapp.util.Constants.YOUTUBEKEY;


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
        public VideoView videoPublicate;
        public YouTubeThumbnailView youTubeThumbnailView;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameCandidate = (TextView) itemView.findViewById(R.id.nameCandidate);
            publicationDate = (TextView) itemView.findViewById(R.id.publicationDate);
            publicationText = (TextView) itemView.findViewById(R.id.publicationText);
            photoCandidate = (ImageView) itemView.findViewById(R.id.photoCandidate);
            //videoPublicate = (VideoView) itemView.findViewById(R.id.videoPublicate);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listener.onPublicatedSelected(listFiltered.get(getAdapterPosition()));
                    //videoPublicate.start();
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
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

        if (!publication.getLink().equals("")&&publication.getLink() != null&&!publication.getLink().equals("null")) {
            //holder.videoPublicate.setVisibility(ImageView.VISIBLE);

            try {
                /*String urlVideo = publication.getLink();
                Uri videoUri = Uri.parse(urlVideo);
                MediaController mediacontroller = new MediaController(context);
                mediacontroller.setAnchorView(holder.videoPublicate);
                holder.videoPublicate.setMediaController(mediacontroller);
                holder.videoPublicate.setVideoURI(videoUri);

                holder.videoPublicate.requestFocus();
                holder.videoPublicate.start();*/
                //holder.videoPublicate.setVideoURI(videoUri);
                /*holder.videoPublicate.setVideoPath(urlVideo);

                        holder.videoPublicate.start();*/



                /*holder.videoPublicate.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                        //holder.videoPublicate.start();
                    }
                });*/

                final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){

                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {

                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                };

                holder.youTubeThumbnailView.initialize(YOUTUBEKEY, new YouTubeThumbnailView.OnInitializedListener(){

                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        youTubeThumbnailLoader.setVideo("azxDhcKYku4");
                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });

            }catch (Exception e) {
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
