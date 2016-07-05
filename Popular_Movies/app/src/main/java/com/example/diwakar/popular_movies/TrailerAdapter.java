package com.example.diwakar.popular_movies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by diwakar on 7/5/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {

    private List<TrailerInfo> trailerInfoList;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView title;
        public ImageView thumbnail;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.trailer_title);
            thumbnail = (ImageView) view.findViewById(R.id.trailer_thumbnail);
        }
    }

    public TrailerAdapter(List<TrailerInfo> trailerInfoList) {
        this.trailerInfoList = trailerInfoList;
    }

    public void addAll(List<TrailerInfo> l) {
        this.trailerInfoList = l;
    }

    @Override
    public TrailerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie_trailer, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.MyViewHolder holder, int position) {
        TrailerInfo trailerInfo = trailerInfoList.get(position);
        holder.title.setText(trailerInfo.title);
        // http://stackoverflow.com/questions/2068344/how-do-i-get-a-youtube-video-thumbnail-from-the-youtube-api
        Picasso.with(holder.thumbnail.getContext()).load("http://i1.ytimg.com/vi/" + trailerInfo.youtubeVideoId + "/mqdefault.jpg").into(holder.thumbnail);
    }

    public TrailerInfo getData(int position) {
        return trailerInfoList.get(position);
    }

    @Override
    public int getItemCount() {
        return trailerInfoList.size();
    }
}
