package com.example.diwakar.popular_movies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by diwakar on 7/4/16.
 */
public class TrailerInfo implements Parcelable {
    public String youtubeVideoId;
    public String title;

    public TrailerInfo() {
    }

    public String getYoutubeURL() {
        return Uri.parse("https://www.youtube.com/watch").buildUpon().appendQueryParameter("v", youtubeVideoId).build().toString();
    }

    protected TrailerInfo(Parcel in) {
        youtubeVideoId = in.readString();
        title = in.readString();
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
}