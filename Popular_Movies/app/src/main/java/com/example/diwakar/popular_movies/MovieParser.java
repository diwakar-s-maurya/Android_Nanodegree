package com.example.diwakar.popular_movies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by diwakar on 7/3/16.
 */
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

    public String getMoviePosterURL(int position) {
        JSONObject data = null;
        try {
            data = new JSONObject(jsonData);
            JSONArray results = data.getJSONArray("results");
            JSONObject movieInfo = results.getJSONObject(position);
            return movieInfo.getString("poster_path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
            info.ID = getMovieID(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

    public int getMovieID(int position) {
        try {
            JSONObject data = new JSONObject(jsonData);
            JSONArray results = data.getJSONArray("results");
            JSONObject movieInfo = results.getJSONObject(position);
            return movieInfo.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
}