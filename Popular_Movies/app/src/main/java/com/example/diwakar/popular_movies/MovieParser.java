package com.example.diwakar.popular_movies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diwakar on 7/3/16.
 */

//parse movie info from json string
public class MovieParser implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieParser> CREATOR = new Parcelable.Creator<MovieParser>() {
        @Override
        public MovieParser createFromParcel(Parcel in) {
            return new MovieParser(in);
        }

        @Override
        public MovieParser[] newArray(int size) {
            return new MovieParser[size];
        }
    };
    private String jsonData = null;
    private String BASE_URL_POSTER = "http://image.tmdb.org/t/p/w185/";

    public MovieParser(String jsonData) {
        this.jsonData = jsonData;
    }

    protected MovieParser(Parcel in) {
        jsonData = in.readString();
    }

    public int movieCount() {
        JSONObject data = null;
        try {
            data = new JSONObject(jsonData);
            return data.getJSONArray("results").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jsonData);
    }

    public MovieInfo getMovieInfo(int position) {
        MovieInfo info = new MovieInfo();
        try {
            JSONObject data = new JSONObject(jsonData);
            JSONArray results = data.getJSONArray("results");
            JSONObject movieInfo = results.getJSONObject(position);

            info.title = movieInfo.getString("title");
            info.posterURL = BASE_URL_POSTER + movieInfo.getString("poster_path");
            info.release_date = movieInfo.getString("release_date");
            info.duration = String.valueOf(-1);
            info.rating = movieInfo.getString("vote_average") + "/10";
            info.plot = movieInfo.getString("overview");
            info.ID = movieInfo.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

    public List<MovieInfo> getAllMovies() {
        List<MovieInfo> allMovies = new ArrayList<>();
        int len = movieCount();
        for (int i = 0; i < len; ++i)
            allMovies.add(getMovieInfo(i));
        return allMovies;
    }
}