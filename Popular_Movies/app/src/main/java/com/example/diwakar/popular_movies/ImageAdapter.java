package com.example.diwakar.popular_movies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by diwakar on 7/3/16.
 */
public class ImageAdapter extends BaseAdapter {
    String BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private Context context;
    private MovieParser movieParser;
    private int widhtOfPoster = 0;
    private int heightOfPoster = 0;

    // Constructor
    public ImageAdapter(Context context, MovieParser movieParser) {
        this.context = context;
        this.movieParser = movieParser;
    }

    @Override
    public int getCount() {
        return movieParser.movieCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;

        String url = movieParser.getMoviePosterURL(position);

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            //imageView.setLayoutParams(new GridView.LayoutParams(185, 277));
            // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(context).load(BASE_URL + url)
                //.resize(185, 277)
                .into(imageView);
        return imageView;
    }
}
