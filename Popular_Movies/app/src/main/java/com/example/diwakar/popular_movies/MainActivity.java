package com.example.diwakar.popular_movies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String LOG_TAG = MainActivity.class.getSimpleName();
    private MovieParser movieParser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        (new FetchPopularMovies()).execute();
    }

    private class FetchPopularMovies extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String FORECAST_URL = "http://api.themoviedb.org/3/movie/popular";
            String API_PARAM = "api_key";

            Uri uri = Uri.parse(FORECAST_URL).buildUpon()
                    .appendQueryParameter(API_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();
            try {
                URL url = new URL(uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null)
                    return null;
                StringBuffer buffer = new StringBuffer();
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
            Toast.makeText(MainActivity.this, "Could not get movies", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v("Result", s);
            movieParser = new MovieParser(s);
            GridView gridView = (GridView) findViewById(R.id.grid_movies);
            gridView.setAdapter(new ImageAdapter(MainActivity.this, movieParser));
        }
    }
}
