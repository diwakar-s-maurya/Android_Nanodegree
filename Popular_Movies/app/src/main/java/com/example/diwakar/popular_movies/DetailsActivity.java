package com.example.diwakar.popular_movies;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diwakar.provider.movie.MovieContentValues;
import com.example.diwakar.provider.movie.MovieSelection;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;
import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetailsActivity extends AppCompatActivity {

    private ReviewAdapter adapterReview;
    String movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(DetailsActivity.this).inflateTransition(R.transition.shared_element_transition));
        }

        MovieInfo movieInfo = getIntent().getParcelableExtra("movie_info");

        ((TextView) findViewById(R.id.movie_title)).setText(movieInfo.title);
        ImageView posterView = (ImageView) findViewById(R.id.movie_poster);
        Picasso.with(this).load(movieInfo.posterURL).into(posterView);
        posterView.setAdjustViewBounds(true);
        ((TextView) findViewById(R.id.movie_release_date)).setText(movieInfo.release_date);
//        //((TextView) findViewById(R.id.movie_duration)).setText(getMovieInfo.duration);
        ((TextView) findViewById(R.id.movie_rating)).setText(movieInfo.rating);
        ((TextView) findViewById(R.id.movie_synopsis)).setText(movieInfo.plot);


        ViewPager viewPager = (ViewPager) findViewById(R.id.trailer_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                DotIndicator dotIndicator = (DotIndicator) findViewById(R.id.dot_indicator);
                dotIndicator.setSelectedItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        RecyclerView reviewRecycler = (RecyclerView) findViewById(R.id.review_list);
        RecyclerView.LayoutManager reviewLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        reviewRecycler.setLayoutManager(reviewLayoutManager);
        adapterReview = new ReviewAdapter(new ArrayList<String>());
        reviewRecycler.setItemAnimator(new DefaultItemAnimator());
        reviewRecycler.setAdapter(adapterReview);
        reviewRecycler.setNestedScrollingEnabled(false);
        //reviewRecycler.addItemDecoration(new RecyclerDivider(this));
        reviewRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(Color.DKGRAY).sizeResId(R.dimen.trailer_recycler_diviver_size).build());

        movieID = String.valueOf(movieInfo.ID);
        (new FetchMovieReviewTask()).execute(movieID);
        (new FetchMovieTrailerTask()).execute(movieID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_favorite);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
        Set<String> set = prefs.getStringSet("favorites", new HashSet<String>());
        boolean foundThisMovie = set.contains(movieID);
        if (foundThisMovie) {
            menuItem.setIcon(R.drawable.favorite_red);
            menuItem.setTitle(getString(R.string.action_unmark_favorite));
        } else {
            menuItem.setIcon(R.drawable.favorite_white);
            menuItem.setTitle(getString(R.string.action_mark_favorite));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
        Set<String> set = prefs.getStringSet("favorites", new HashSet<String>());
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if (item.getTitle() == getString(R.string.action_mark_favorite)) {
                    item.setIcon(R.drawable.favorite_red);
                    item.setTitle(getString(R.string.action_unmark_favorite));
                    set.add(movieID);
                    saveMovie();
                } else {
                    item.setIcon(R.drawable.favorite_white);
                    item.setTitle(getString(R.string.action_mark_favorite));
                    set.remove(movieID);
                    unsaveMovie();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("favorites", set);
        editor.apply();
        return true;
    }

    private void saveMovie() {
        MovieInfo movieInfo = getIntent().getParcelableExtra("movie_info");

        MovieContentValues values = new MovieContentValues();
        values.putTitle(movieInfo.title);
        values.putReleaseDate(movieInfo.release_date);
        values.putDurationNull();
        values.putRating(movieInfo.rating);
        values.putPlot(movieInfo.plot);
        values.putMovieid(String.valueOf(movieInfo.ID));

        ImageView posterView = (ImageView) findViewById(R.id.movie_poster);
        Bitmap bitmap = ((BitmapDrawable) posterView.getDrawable()).getBitmap();

        try {
            String path = getFilesDir() + movieID;
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            values.putPosterurl("file:" + path); //this needs to be done to load image file using picasso
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        values.insert(getApplicationContext());


    }

    private void unsaveMovie() {
        MovieInfo info = getIntent().getParcelableExtra("movie_info");
        MovieSelection where = new MovieSelection();
        where.movieid(movieID);
        where.delete(getContentResolver());
        File file = new File(getFilesDir() + movieID);
        file.delete();
        //// TODO: 7/6/16 to check is delete working
    }


    public class FetchMovieReviewTask extends AsyncTask<String, Void, List<String>> {
        private String API_PARAM = "api_key";
        private String REVIEW_PATH = "reviews";
        private String BASE_URL_REVIEW = "http://api.themoviedb.org/3/movie/";

        @Override
        protected List<String> doInBackground(String... params) {
            String movieID = params[0];
            Uri uri = Uri.parse(BASE_URL_REVIEW).buildUpon()
                    .appendPath(movieID)
                    .appendPath(REVIEW_PATH)
                    .appendQueryParameter(API_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            try {
                URL url = new URL(uri.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null)
                    return null;
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                if (stringBuilder.length() == 0)
                    return null;
                String result = stringBuilder.toString();

                JSONObject object = new JSONObject(result);
                JSONArray results = object.getJSONArray("results");
                List<String> reviewContentList = new ArrayList<>(results.length());
                for (int i = 0; i < results.length(); ++i) {
                    JSONObject review = results.getJSONObject(i);
                    String content = review.getString("content");
                    reviewContentList.add(content);
                }
                return reviewContentList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            if (list != null) {
                adapterReview.addAll(list);
                adapterReview.notifyDataSetChanged();
            }
        }
    }

    public class FetchMovieTrailerTask extends AsyncTask<String, Void, List<TrailerInfo>> {
        private String API_PARAM = "api_key";
        private String VIDEOS_PATH = "videos";
        private String BASE_URL_TRAILER = "http://api.themoviedb.org/3/movie/";

        @Override
        protected List<TrailerInfo> doInBackground(String... params) {
            String movieID = params[0];
            Uri uri = Uri.parse(BASE_URL_TRAILER).buildUpon()
                    .appendPath(movieID)
                    .appendPath(VIDEOS_PATH)
                    .appendQueryParameter(API_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            try {
                URL url = new URL(uri.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null)
                    return null;
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                if (stringBuilder.length() == 0)
                    return null;
                String result = stringBuilder.toString();

                JSONObject object = new JSONObject(result);
                JSONArray results = object.getJSONArray("results");
                List<TrailerInfo> trailerList = new ArrayList<>(results.length());
                for (int i = 0; i < results.length(); ++i) {
                    JSONObject review = results.getJSONObject(i);
                    String site = review.getString("site");

                    if (site.equals("YouTube") == true) {
                        String key = review.getString("key");
                        String title = review.getString("name");

                        TrailerInfo trailerInfo = new TrailerInfo();
                        trailerInfo.title = title;
                        trailerInfo.youtubeVideoId = key;
                        trailerList.add(trailerInfo);
                    }
                }
                return trailerList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<TrailerInfo> list) {
            super.onPostExecute(list);
            if (list == null)
                return;
            ViewPager viewPager = (ViewPager) findViewById(R.id.trailer_pager);
            TrailerPagerAdapter trailerPagerAdapter = new TrailerPagerAdapter(DetailsActivity.this, list);
            viewPager.setAdapter(trailerPagerAdapter);
            DotIndicator dotIndicator = (DotIndicator) findViewById(R.id.dot_indicator);
            dotIndicator.setNumberOfItems(trailerPagerAdapter.getCount());
            final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
            viewPager.setPageMargin(pageMargin);
        }
    }
}
