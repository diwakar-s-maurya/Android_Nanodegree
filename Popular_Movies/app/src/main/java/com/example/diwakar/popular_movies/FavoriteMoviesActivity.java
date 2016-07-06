package com.example.diwakar.popular_movies;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.View;

import com.example.diwakar.provider.movie.MovieCursor;
import com.example.diwakar.provider.movie.MovieSelection;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMoviesActivity extends AppCompatActivity {

    final int FAVORITE_STATE = 0;
    final int STILL_FAVORITE = 1;
    final int NOT_STILL_FAVORITE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementExitTransition(TransitionInflater.from(FavoriteMoviesActivity.this).inflateTransition(R.transition.shared_element_transition));
        }

        List<MovieInfo> list = getMoviesList();

        RecyclerView favoriteRecyclerView = (RecyclerView) findViewById(R.id.favorite_list);
        FavoriteAdapter adapter = new FavoriteAdapter(list);
        favoriteRecyclerView.setItemAnimator(new DefaultItemAnimator());
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        favoriteRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(Color.DKGRAY).sizeResId(R.dimen.trailer_recycler_diviver_size).build());
        favoriteRecyclerView.setAdapter(adapter);

        RecyclerItemClickSupport.addTo(favoriteRecyclerView).setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {

            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View view) {
                MovieInfo info = ((FavoriteAdapter) recyclerView.getAdapter()).get(position);
                Intent intent = new Intent(FavoriteMoviesActivity.this, DetailsActivity.class);

                intent.putExtra("movie_info", info);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.setTransitionName("poster_transition");
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityOptionsCompat options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(FavoriteMoviesActivity.this, view, view.getTransitionName());
                    }
                    startActivityForResult(intent, FAVORITE_STATE, options.toBundle());
                } else
                    startActivityForResult(intent, FAVORITE_STATE);
            }
        });
    }

    public List<MovieInfo> getMoviesList() {
        MovieSelection where = new MovieSelection();
        MovieCursor movieCursor = where.query(getContentResolver());
        List<MovieInfo> list = new ArrayList<>();
        while (movieCursor.moveToNext()) {
            MovieInfo info = new MovieInfo();
            info.title = movieCursor.getTitle();
            info.posterURL = movieCursor.getPosterurl();
            info.release_date = movieCursor.getReleaseDate();
            info.duration = movieCursor.getDuration();
            info.rating = movieCursor.getRating();
            info.plot = movieCursor.getPlot();
            info.ID = Integer.parseInt(movieCursor.getMovieid());

            list.add(info);
        }
        return list;
    }
}
