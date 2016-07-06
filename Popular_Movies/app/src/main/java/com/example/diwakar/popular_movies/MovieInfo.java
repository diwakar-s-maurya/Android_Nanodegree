package com.example.diwakar.popular_movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by diwakar on 7/4/16.
 */
public class MovieInfo implements Parcelable {
    public String title;
    public String posterURL;
    public String release_date;
    public String duration;
    public String rating;
    public String plot;
    public int ID;

    public MovieInfo() {

    }

    protected MovieInfo(Parcel in) {
        title = in.readString();
        posterURL = in.readString();
        release_date = in.readString();
        duration = in.readString();
        rating = in.readString();
        plot = in.readString();
        ID = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterURL);
        dest.writeString(release_date);
        dest.writeString(duration);
        dest.writeString(rating);
        dest.writeString(plot);
        dest.writeInt(ID);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}
