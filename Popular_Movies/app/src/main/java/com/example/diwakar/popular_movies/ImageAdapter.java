package com.example.diwakar.popular_movies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by diwakar on 7/3/16.
 */
public class ImageAdapter extends BaseAdapter {
    List<MovieInfo> movieInfoList;
    private Context context;

    // Constructor
    public ImageAdapter(Context context, List<MovieInfo> movieInfoList) {
        this.context = context;
        this.movieInfoList = movieInfoList;
    }

    @Override
    public int getCount() {
        return movieInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addAll(List<MovieInfo> list) {
        this.movieInfoList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(context).load(movieInfoList.get(position).posterURL)
                //.resize(185, 277)
                .into(imageView);
        return imageView;
    }
}
