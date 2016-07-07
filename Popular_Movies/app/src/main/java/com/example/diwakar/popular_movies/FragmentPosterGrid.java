package com.example.diwakar.popular_movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FragmentPosterGrid extends Fragment {

    String POPULAR_MOVIES_URL = "http://api.themoviedb.org/3/movie/popular";
    String TOP_RATE_MOVIES_URL = "http://api.themoviedb.org/3/movie/top_rated";
    private String LOG_TAG = FragmentPosterGrid.class.getSimpleName();
    private MovieParser movieParser = null;
    private Menu menu = null;

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putParcelable("movie_parser", movieParser);
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        movieParser = savedInstanceState.getParcelable("movie_parser");
//        // Toast.makeText(FragmentPosterGrid.this, "restored", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_poster_grid, container, false);
        setHasOptionsMenu(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setSharedElementExitTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.shared_element_transition));
        }


        if (savedInstanceState != null)
            movieParser = savedInstanceState.getParcelable("movie_parser");
        //else
        //Toast.makeText(FragmentPosterGrid.this, "Create called", Toast.LENGTH_SHORT).show();

        if (movieParser == null)
            executeFetchMoviesTask();
        else {
            GridView gridView = (GridView) view.findViewById(R.id.grid_movies);
            gridView.setAdapter(new ImageAdapter(getContext(), movieParser));
        }

        assert ((GridView) view.findViewById(R.id.grid_movies)) != null;
        ((GridView) view.findViewById(R.id.grid_movies)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                ((MainActivity)getActivity()).movieFromPosterGridClicked(movieParser.getMovieInfo(position));
                //                Intent intent = new Intent(getContext(), FragmentDetails.class);
//                MovieInfo movieInfo = movieParser.getMovieInfo(position);
//                intent.putExtra("movie_info", movieInfo);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    view.setTransitionName("poster_transition");
//                }
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    ActivityOptionsCompat options = null;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        options = ActivityOptionsCompat.
//                                makeSceneTransitionAnimation(getContext(), view, view.getTransitionName());
//                    }
//                    startActivity(intent, options.toBundle());
//                } else
//                    startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
        Log.v("Menu", "called");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortBy = prefs.getString(getString(R.string.pref_sortBy_key), getString(R.string.pref_popular));
        if (sortBy == getString(R.string.pref_popular))
            menu.findItem(R.id.sortBy_popular).setVisible(false);
        else
            menu.findItem(R.id.sortBy_top_rated).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();

        switch (item.getItemId()) {
            case R.id.sortBy_popular:
                editor.putString(getString(R.string.pref_sortBy_key), getString(R.string.pref_popular));
                menu.findItem(R.id.sortBy_top_rated).setVisible(true);
                menu.findItem(R.id.sortBy_popular).setVisible(false);
                break;
            case R.id.sortBy_top_rated:
                editor.putString(getString(R.string.pref_sortBy_key), getString(R.string.pref_topRated));
                menu.findItem(R.id.sortBy_popular).setVisible(true);
                menu.findItem(R.id.sortBy_top_rated).setVisible(false);
                break;
            case R.id.show_saved_movies:
                Intent intent = new Intent(getContext(), FavoriteMoviesActivity.class);
                startActivity(intent);
                break;
        }
        editor.apply();
        executeFetchMoviesTask();
        return true;
    }

    private void executeFetchMoviesTask() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortBy = prefs.getString(getString(R.string.pref_sortBy_key), getString(R.string.pref_popular));
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
        if (sortBy == getString(R.string.pref_popular)) {
//            actionBar.setSubtitle("by popularity");
            (new FetchPopularMovies()).execute(POPULAR_MOVIES_URL);
        } else {
//            actionBar.setSubtitle("by rating");
            (new FetchPopularMovies()).execute(TOP_RATE_MOVIES_URL);
        }
    }

    private class FetchPopularMovies extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String BASE_URL = params[0];
            String API_PARAM = "api_key";

            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(API_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();
            try {
                URL url = new URL(uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null)
                    return null;
                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0)
                    return null;
                result = buffer.toString();
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            if (result == null)
                while (cancel(true) != true) ;
            movieParser = new MovieParser(result);
            return result;
        }

        @Override
        protected void onCancelled(String s) {
            Toast.makeText(getContext(), "Could not get movies", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v("Result", s);
            movieParser = new MovieParser(s);
            GridView gridView = (GridView) getView().findViewById(R.id.grid_movies);
            gridView.setAdapter(new ImageAdapter(getContext(), movieParser));
        }
    }
}
