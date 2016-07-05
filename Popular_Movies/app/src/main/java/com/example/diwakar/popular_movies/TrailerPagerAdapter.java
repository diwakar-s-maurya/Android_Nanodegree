package com.example.diwakar.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by diwakar on 7/5/16.
 */
public class TrailerPagerAdapter extends PagerAdapter {

    //No. of data items in this denotes the no. of pages
    private List<TrailerInfo> trailerInfoList;

    private Context context;

    public TrailerPagerAdapter(Context c, List<TrailerInfo> l) {
        this.context = c;
        this.trailerInfoList = l;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_movie_trailer, container, false);

        ImageView thumbnail = (ImageView) view.findViewById(R.id.trailer_thumbnail);
        TextView title = (TextView) view.findViewById(R.id.trailer_title);

        final TrailerInfo trailerInfo = trailerInfoList.get(position);
        // http://stackoverflow.com/questions/2068344/how-do-i-get-a-youtube-video-thumbnail-from-the-youtube-api
        Picasso.with(context).load("http://i1.ytimg.com/vi/" + trailerInfo.youtubeVideoId + "/mqdefault.jpg").into(thumbnail);
        title.setText(trailerInfo.title);
        container.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerInfo.getYoutubeURL()));

                if (intent.resolveActivity(context.getPackageManager()) != null)
                    context.startActivity(intent);
                else
                    Toast.makeText(context, "No app found to view trailer", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return trailerInfoList.size();
    }

    public void setData(List<TrailerInfo> info) {
        this.trailerInfoList = info;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        //layout here is the root layout of the pager layout resource
        return view == (LinearLayout) object;
    }
}
