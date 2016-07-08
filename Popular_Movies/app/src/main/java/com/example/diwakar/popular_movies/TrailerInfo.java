package com.example.diwakar.popular_movies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by diwakar on 7/4/16.
 */

// keeps info about movie trailer
// use parcelabler.com to easily make your class implement Parcelable interface
public class TrailerInfo implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TrailerInfo> CREATOR = new Parcelable.Creator<TrailerInfo>() {
        @Override
        public TrailerInfo createFromParcel(Parcel in) {
            return new TrailerInfo(in);
        }

        @Override
        public TrailerInfo[] newArray(int size) {
            return new TrailerInfo[size];
        }
    };
    public String youtubeVideoId;
    public String title;

    public TrailerInfo() {
    }

    protected TrailerInfo(Parcel in) {
        youtubeVideoId = in.readString();
        title = in.readString();
    }

    public String getYoutubeURL() {
        return Uri.parse("https://www.youtube.com/watch").buildUpon().appendQueryParameter("v", youtubeVideoId).build().toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(youtubeVideoId);
        dest.writeString(title);
    }
}