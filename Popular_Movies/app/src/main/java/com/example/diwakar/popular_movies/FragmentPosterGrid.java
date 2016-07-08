package com.example.diwakar.popular_movies;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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

import com.example.diwakar.provider.movie.MovieCursor;
import com.example.diwakar.provider.movie.MovieSelection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FragmentPosterGrid extends Fragment {

    private final String POPULAR_MOVIES_URL = "http://api.themoviedb.org/3/movie/popular";
    private final String TOP_RATE_MOVIES_URL = "http://api.themoviedb.org/3/movie/top_rated";
    private final String LOG_TAG = FragmentPosterGrid.class.getSimpleName();
    private int MODE_CURRENT = ProjectConstants.MODE_NORMAL;
    private List<MovieInfo> displayedMovieList;
    private ImageAdapter gridViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        final View view = inflater.inflate(R.layout.fragment_poster_grid, container, false);

        // TODO: 7/7/16 shared element transition does not work. check
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getActivity().getWindow().setSharedElementExitTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.shared_element_transition));
//        }

        assert ((GridView) view.findViewById(R.id.grid_movies)) != null;
        GridView gridView = (GridView) view.findViewById(R.id.grid_movies);
        gridViewAdapter = new ImageAdapter(getContext(), new ArrayList<MovieInfo>());
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).movieFromPosterGridClicked(displayedMovieList.get(position));
            }
        });

        MODE_CURRENT = getActivity().getIntent().getIntExtra(ProjectConstants.BUNDLE_MODE, ProjectConstants.MODE_NORMAL);
        if (MODE_CURRENT == ProjectConstants.MODE_FAVOURITE) {
            displayedMovieList = getFavoriteMoviesList();
            if (displayedMovieList.isEmpty())
                Toast.makeText(getContext(), getString(R.string.notification_no_favourites), Toast.LENGTH_SHORT).show();
            gridViewAdapter.addAll(displayedMovieList);
        } else
            executeFetchMoviesTask();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (MODE_CURRENT == ProjectConstants.MODE_NORMAL)
            inflater.inflate(R.menu.details, menu);
    }

    //click on menu is first propagated to main activity, then to this menu. this can be reached only if main activity returned false;
    //menu items added to menu are accessible at both places
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        switch (item.getItemId()) {
            case R.id.sortBy_popular:
                editor.putString(getString(R.string.pref_sortBy_key), getString(R.string.pref_popular));
                actionBar.setSubtitle(R.string.title_sortBy_popular);
                editor.apply();
                executeFetchMoviesTask();
                return true;
            case R.id.sortBy_top_rated:
                editor.putString(getString(R.string.pref_sortBy_key), getString(R.string.pref_topRated));
                actionBar.setSubtitle(R.string.title_sortBy_top_rated);
                editor.apply();
                executeFetchMoviesTask();
                return true;
        }
        return false;
    }

    //fetches movie info depending on preference: popular or top-rated
    private void executeFetchMoviesTask() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortBy = prefs.getString(getString(R.string.pref_sortBy_key), getString(R.string.pref_popular));
        if (sortBy.equals(getString(R.string.pref_popular))) {
            (new FetchPopularMovies()).execute(POPULAR_MOVIES_URL);
        } else {
            (new FetchPopularMovies()).execute(TOP_RATE_MOVIES_URL);
        }
    }

    //refresh movie poster grid if showing favourite movies
    public void refreshGridInFavMode() {
        if (gridViewAdapter != null) {
            displayedMovieList = getFavoriteMoviesList();
            gridViewAdapter.addAll(displayedMovieList);
        } else
            Log.w(FragmentPosterGrid.class.getSimpleName(), "refreshGrid called on null " + gridViewAdapter.getClass().getSimpleName());
    }

    //get favourite movies info list
    public List<MovieInfo> getFavoriteMoviesList() {
        MovieSelection where = new MovieSelection();
        MovieCursor movieCursor = where.query(getContext());
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
            return result;
        }

        @Override
        protected void onCancelled(String s) {
            Toast.makeText(getContext(), getString(R.string.notification_movie_fetch_failure), Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (getView() != null) { //if fragment not deleted before reaching here (eg: screen rotated before this task reached here)
                // Log.v("Result", s);
                displayedMovieList = (new MovieParser(s)).getAllMovies();
                GridView gridView = (GridView) getView().findViewById(R.id.grid_movies);
                gridView.setAdapter(new ImageAdapter(getContext(), displayedMovieList));
            }
        }
    }
}
