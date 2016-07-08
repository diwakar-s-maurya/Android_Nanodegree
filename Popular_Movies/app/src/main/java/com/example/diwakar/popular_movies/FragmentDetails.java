package com.example.diwakar.popular_movies;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diwakar.provider.movie.MovieContentValues;
import com.example.diwakar.provider.movie.MovieCursor;
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
import java.util.List;

public class FragmentDetails extends Fragment {

    private ReviewAdapter adapterReview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_details, container, false);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getActivity().getWindow().setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.shared_element_transition));
//        }

        setHasOptionsMenu(true);

        MovieInfo movieInfo = getArguments().getParcelable("movie_info");

        ((TextView) view.findViewById(R.id.movie_title)).setText(movieInfo.title);
        ImageView posterView = (ImageView) view.findViewById(R.id.movie_poster);
        Picasso.with(getContext()).load(movieInfo.posterURL).into(posterView);
        posterView.setAdjustViewBounds(true);
        ((TextView) view.findViewById(R.id.movie_release_date)).setText(movieInfo.release_date);
//        //((TextView) findViewById(R.id.movie_duration)).setText(getMovieInfo.duration);
        ((TextView) view.findViewById(R.id.movie_rating)).setText(movieInfo.rating);
        ((TextView) view.findViewById(R.id.movie_synopsis)).setText(movieInfo.plot);

        ImageView favImage = ((ImageView) view.findViewById(R.id.action_favourite));
        ImageView unfavImage = ((ImageView) view.findViewById(R.id.action_unfavourite));
        onFavUnFavButtonClick favUnfavListener = new onFavUnFavButtonClick();
        favImage.setOnClickListener(favUnfavListener);
        unfavImage.setOnClickListener(favUnfavListener);
        favImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), getString(R.string.info_favourite_button), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        unfavImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), getString(R.string.info_unfavourite_button), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        if (checkIfFavourite(movieInfo.ID)) {
            favImage.setVisibility(View.VISIBLE);
            unfavImage.setVisibility(View.GONE);
        } else {
            favImage.setVisibility(View.GONE);
            unfavImage.setVisibility(View.VISIBLE);
        }

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.trailer_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                DotIndicator dotIndicator = (DotIndicator) view.findViewById(R.id.dot_indicator);
                dotIndicator.setSelectedItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        RecyclerView reviewRecycler = (RecyclerView) view.findViewById(R.id.review_list);
        RecyclerView.LayoutManager reviewLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        reviewRecycler.setLayoutManager(reviewLayoutManager);
        adapterReview = new ReviewAdapter(new ArrayList<String>());
        reviewRecycler.setItemAnimator(new DefaultItemAnimator());
        reviewRecycler.setAdapter(adapterReview);
        reviewRecycler.setNestedScrollingEnabled(false);
        //reviewRecycler.addItemDecoration(new RecyclerDivider(this));
        reviewRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).color(Color.DKGRAY).sizeResId(R.dimen.trailer_recycler_diviver_size).build());

        (new FetchMovieReviewTask()).execute(String.valueOf(movieInfo.ID));
        (new FetchMovieTrailerTask()).execute(String.valueOf(movieInfo.ID));
        return view;
    }


    //notify activity about self detachment
    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) getActivity()).fragmentDetailsDetached();
    }

    //checks if a movie is marked favorite or not. true => favourite, false => not favourite
    private boolean checkIfFavourite(int movieID) {
        MovieSelection where = new MovieSelection();
        where.movieid(String.valueOf(movieID));
        MovieCursor cursor = where.query(getContext());
        return cursor.moveToNext();
    }

    //fetch movie reviews
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

                    if (site.equals("YouTube")) {
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

            if (getView() != null) { //if the fragment is deleted before reaching this method, then view will be null.
                TrailerViewPager viewPager = (TrailerViewPager) getView().findViewById(R.id.trailer_pager);
                TrailerPagerAdapter trailerPagerAdapter = new TrailerPagerAdapter(getContext(), list);
                viewPager.setAdapter(trailerPagerAdapter);
                DotIndicator dotIndicator = (DotIndicator) getView().findViewById(R.id.dot_indicator);
                dotIndicator.setNumberOfItems(trailerPagerAdapter.getCount());
                final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
                viewPager.setPageMargin(pageMargin);
            }
        }
    }

    private class onFavUnFavButtonClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ImageView favImage = ((ImageView) getView().findViewById(R.id.action_favourite));
            ImageView unFavImage = ((ImageView) getView().findViewById(R.id.action_unfavourite));
            switch (v.getId()) {
                case R.id.action_unfavourite:
                    saveMovie();
                    favImage.setVisibility(View.VISIBLE);
                    unFavImage.setVisibility(View.GONE);
                    Toast.makeText(getContext(), getString(R.string.notification_added_favourite_movie), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_favourite:
                    unsaveMovie();
                    favImage.setVisibility(View.GONE);
                    unFavImage.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), getString(R.string.notification_removed_favourite_movie), Toast.LENGTH_SHORT).show();
                    break;
            }

            ((MainActivity) getActivity()).favUnFavButtonClicked(); //notify activity about the click
        }

        private void saveMovie() {
            MovieInfo movieInfo = getArguments().getParcelable(ProjectConstants.BUNDLE_MOVIE_INFO);

            MovieContentValues values = new MovieContentValues();
            values.putTitle(movieInfo.title);
            values.putReleaseDate(movieInfo.release_date);
            values.putDurationNull();
            values.putRating(movieInfo.rating);
            values.putPlot(movieInfo.plot);
            values.putMovieid(String.valueOf(movieInfo.ID));

            ImageView posterView = (ImageView) getView().findViewById(R.id.movie_poster);
            Bitmap bitmap = ((BitmapDrawable) posterView.getDrawable()).getBitmap();

            try {
                String path = getContext().getFilesDir() + String.valueOf(movieInfo.ID);
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

            values.insert(getContext());
        }

        private void unsaveMovie() {
            MovieInfo info = getArguments().getParcelable(ProjectConstants.BUNDLE_MOVIE_INFO);
            MovieSelection where = new MovieSelection();
            where.movieid(String.valueOf(info.ID));
            where.delete(getContext());
            File file = new File(getContext().getFilesDir() + String.valueOf(info.ID));
            file.delete();
        }
    }
}
