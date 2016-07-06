package com.example.diwakar.provider.movie;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.diwakar.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code movie} table.
 */
public class MovieContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return MovieColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable MovieSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable MovieSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public MovieContentValues putTitle(@Nullable String value) {
        mContentValues.put(MovieColumns.TITLE, value);
        return this;
    }

    public MovieContentValues putTitleNull() {
        mContentValues.putNull(MovieColumns.TITLE);
        return this;
    }

    public MovieContentValues putPosterurl(@Nullable String value) {
        mContentValues.put(MovieColumns.POSTERURL, value);
        return this;
    }

    public MovieContentValues putPosterurlNull() {
        mContentValues.putNull(MovieColumns.POSTERURL);
        return this;
    }

    public MovieContentValues putReleaseDate(@Nullable String value) {
        mContentValues.put(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public MovieContentValues putReleaseDateNull() {
        mContentValues.putNull(MovieColumns.RELEASE_DATE);
        return this;
    }

    public MovieContentValues putDuration(@Nullable String value) {
        mContentValues.put(MovieColumns.DURATION, value);
        return this;
    }

    public MovieContentValues putDurationNull() {
        mContentValues.putNull(MovieColumns.DURATION);
        return this;
    }

    public MovieContentValues putRating(@Nullable String value) {
        mContentValues.put(MovieColumns.RATING, value);
        return this;
    }

    public MovieContentValues putRatingNull() {
        mContentValues.putNull(MovieColumns.RATING);
        return this;
    }

    public MovieContentValues putPlot(@Nullable String value) {
        mContentValues.put(MovieColumns.PLOT, value);
        return this;
    }

    public MovieContentValues putPlotNull() {
        mContentValues.putNull(MovieColumns.PLOT);
        return this;
    }

    public MovieContentValues putMovieid(@Nullable String value) {
        mContentValues.put(MovieColumns.MOVIEID, value);
        return this;
    }

    public MovieContentValues putMovieidNull() {
        mContentValues.putNull(MovieColumns.MOVIEID);
        return this;
    }
}
