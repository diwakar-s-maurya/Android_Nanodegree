package com.example.diwakar.popular_movies;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by diwakar on 7/5/16.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private List<String> reviewList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView review_content;

        public MyViewHolder(View view) {
            super(view);
            review_content = (TextView) view.findViewById(R.id.movie_review_content);
        }
    }

    public ReviewAdapter(List<String> reviewList) {
        this.reviewList = reviewList;
    }

    public void addAll(List<String> l) {
        this.reviewList = l;
    }

    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie_reivew, parent, false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.review_content.setText(reviewList.get(position));
    }

    public String getData(int position) {
        return reviewList.get(position);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
