package com.example.diwakar.popular_movies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by diwakar on 7/6/16.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {

    private List<MovieInfo> movieList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView favorite_title, favorite_release_date, favorite_rating;
        public ImageView favorite_poster;

        public MyViewHolder(View view) {
            super(view);
            favorite_title = (TextView) view.findViewById(R.id.favorite_title);
            favorite_release_date = (TextView) view.findViewById(R.id.favorite_release_date);
            favorite_rating = (TextView) view.findViewById(R.id.favorite_rating);
            favorite_poster = (ImageView) view.findViewById(R.id.favorite_poster);
        }
    }

    public FavoriteAdapter(List<MovieInfo> reviewList) {
        this.movieList = reviewList;
    }

    public void addAll(List<MovieInfo> l) {
        this.movieList = l;
        notifyDataSetChanged();
    }

    @Override
    public FavoriteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_favorite_movie, parent, false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        MovieInfo info = movieList.get(position);
        holder.favorite_title.setText(info.title);
        holder.favorite_release_date.setText(info.release_date);
        holder.favorite_rating.setText(info.rating);
        String path = movieList.get(position).posterURL;
        Picasso.with(holder.favorite_poster.getContext()).load(path).into(holder.favorite_poster);
    }

    public MovieInfo get(int position) {
        return movieList.get(position);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
