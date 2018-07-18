package com.softmedialtda.votingapp.campaign.activity;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
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

import static com.softmedialtda.votingapp.util.Common.extractVideoIdFromUrl;
import static com.softmedialtda.votingapp.util.Constants.YOUTUBEKEY;


/**
 * Created by Agustin on 29/6/2018.
 */

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Publication> list;
    private List<Publication> listFiltered;
    private PublicationAdapterListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView nameCandidate, publicationDate, publicationText;
        public ImageView photoCandidate;
        public VideoView videoPublicate;
        public YouTubeThumbnailView youTubeThumbnailView;
        public RelativeLayout relativeLayoutOverYouTubeThumbnailView;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameCandidate = (TextView) itemView.findViewById(R.id.nameCandidate);
            publicationDate = (TextView) itemView.findViewById(R.id.publicationDate);
            publicationText = (TextView) itemView.findViewById(R.id.publicationText);
            photoCandidate = (ImageView) itemView.findViewById(R.id.photoCandidate);
            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);

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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Publication publication = listFiltered.get(position);
        holder.nameCandidate.setText(publication.getNameStudent());
        holder.publicationDate.setText(publication.getDate());
        holder.publicationText.setText(publication.getText());

        if (publication.getImageStudent().equals("")){
            holder.photoCandidate.setImageResource(R.mipmap.photodefault);
        }else{
            byte[] decodedString = Base64.decode(publication.getImageStudent(), Base64.DEFAULT);
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

            holder.photoCandidate.setImageBitmap(circleBitmap);
        }

        if (!publication.getLink().equals("")&&publication.getLink() != null&&!publication.getLink().equals("null")) {
            holder.youTubeThumbnailView.setVisibility(ImageView.VISIBLE);
            holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(ImageView.VISIBLE);

            try {
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
                        String idVideo =  extractVideoIdFromUrl(publication.getLink());
                        youTubeThumbnailLoader.setVideo(idVideo);
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
