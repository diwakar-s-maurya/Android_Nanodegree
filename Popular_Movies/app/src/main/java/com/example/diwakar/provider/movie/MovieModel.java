package com.example.diwakar.provider.movie;

import android.support.annotation.Nullable;

import com.example.diwakar.provider.base.BaseModel;

/**
 * Data model for the {@code movie} table.
 */
public interface MovieModel extends BaseModel {

    /**
     * Get the {@code title} value.
     * Can be {@code null}.
     */
    @Nullable
    String getTitle();

    /**
     * Get the {@code posterurl} value.
     * Can be {@code null}.
     */
    @Nullable
    String getPosterurl();

    /**
     * Get the {@code release_date} value.
     * Can be {@code null}.
     */
    @Nullable
    String getReleaseDate();

    /**
     * Get the {@code duration} value.
     * Can be {@code null}.
     */
    @Nullable
    String getDuration();

    /**
     * Get the {@code rating} value.
     * Can be {@code null}.
     */
    @Nullable
    String getRating();

    /**
     * Get the {@code plot} value.
     * Can be {@code null}.
     */
    @Nullable
    String getPlot();

    /**
     * Get the {@code movieid} value.
     * Can be {@code null}.
     */
    @Nullable
    String getMovieid();
}
