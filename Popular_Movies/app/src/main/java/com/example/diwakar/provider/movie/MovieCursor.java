package com.example.diwakar.provider.movie;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.example.diwakar.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code movie} table.
 */
public class MovieCursor extends AbstractCursor implements MovieModel {
    public MovieCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(MovieColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code title} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getTitle() {
        String res = getStringOrNull(MovieColumns.TITLE);
        return res;
    }

    /**
     * Get the {@code posterurl} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getPosterurl() {
        String res = getStringOrNull(MovieColumns.POSTERURL);
        return res;
    }

    /**
     * Get the {@code release_date} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getReleaseDate() {
        String res = getStringOrNull(MovieColumns.RELEASE_DATE);
        return res;
    }

    /**
     * Get the {@code duration} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getDuration() {
        String res = getStringOrNull(MovieColumns.DURATION);
        return res;
    }

    /**
     * Get the {@code rating} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getRating() {
        String res = getStringOrNull(MovieColumns.RATING);
        return res;
    }

    /**
     * Get the {@code plot} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getPlot() {
        String res = getStringOrNull(MovieColumns.PLOT);
        return res;
    }

    /**
     * Get the {@code movieid} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getMovieid() {
        String res = getStringOrNull(MovieColumns.MOVIEID);
        return res;
    }
}
