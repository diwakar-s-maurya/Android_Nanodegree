package com.example.diwakar.popular_movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by diwakar on 7/3/16.
 */
public class MovieParser {
    private String jsonData = null;

    public MovieParser(String jsonData) {
        this.jsonData = jsonData;
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
}
