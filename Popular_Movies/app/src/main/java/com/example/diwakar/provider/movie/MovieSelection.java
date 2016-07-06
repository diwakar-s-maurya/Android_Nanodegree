package com.example.diwakar.provider.movie;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.example.diwakar.provider.base.AbstractSelection;

/**
 * Selection for the {@code movie} table.
 */
public class MovieSelection extends AbstractSelection<MovieSelection> {
    @Override
    protected Uri baseUri() {
        return MovieColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code MovieCursor} object, which is positioned before the first entry, or null.
     */
    public MovieCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new MovieCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public MovieCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code MovieCursor} object, which is positioned before the first entry, or null.
     */
    public MovieCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new MovieCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public MovieCursor query(Context context) {
        return query(context, null);
    }


    public MovieSelection id(long... value) {
        addEquals("movie." + MovieColumns._ID, toObjectArray(value));
        return this;
    }

    public MovieSelection idNot(long... value) {
        addNotEquals("movie." + MovieColumns._ID, toObjectArray(value));
        return this;
    }

    public MovieSelection orderById(boolean desc) {
        orderBy("movie." + MovieColumns._ID, desc);
        return this;
    }

    public MovieSelection orderById() {
        return orderById(false);
    }

    public MovieSelection title(String... value) {
        addEquals(MovieColumns.TITLE, value);
        return this;
    }

    public MovieSelection titleNot(String... value) {
        addNotEquals(MovieColumns.TITLE, value);
        return this;
    }

    public MovieSelection titleLike(String... value) {
        addLike(MovieColumns.TITLE, value);
        return this;
    }

    public MovieSelection titleContains(String... value) {
        addContains(MovieColumns.TITLE, value);
        return this;
    }

    public MovieSelection titleStartsWith(String... value) {
        addStartsWith(MovieColumns.TITLE, value);
        return this;
    }

    public MovieSelection titleEndsWith(String... value) {
        addEndsWith(MovieColumns.TITLE, value);
        return this;
    }

    public MovieSelection orderByTitle(boolean desc) {
        orderBy(MovieColumns.TITLE, desc);
        return this;
    }

    public MovieSelection orderByTitle() {
        orderBy(MovieColumns.TITLE, false);
        return this;
    }

    public MovieSelection posterurl(String... value) {
        addEquals(MovieColumns.POSTERURL, value);
        return this;
    }

    public MovieSelection posterurlNot(String... value) {
        addNotEquals(MovieColumns.POSTERURL, value);
        return this;
    }

    public MovieSelection posterurlLike(String... value) {
        addLike(MovieColumns.POSTERURL, value);
        return this;
    }

    public MovieSelection posterurlContains(String... value) {
        addContains(MovieColumns.POSTERURL, value);
        return this;
    }

    public MovieSelection posterurlStartsWith(String... value) {
        addStartsWith(MovieColumns.POSTERURL, value);
        return this;
    }

    public MovieSelection posterurlEndsWith(String... value) {
        addEndsWith(MovieColumns.POSTERURL, value);
        return this;
    }

    public MovieSelection orderByPosterurl(boolean desc) {
        orderBy(MovieColumns.POSTERURL, desc);
        return this;
    }

    public MovieSelection orderByPosterurl() {
        orderBy(MovieColumns.POSTERURL, false);
        return this;
    }

    public MovieSelection releaseDate(String... value) {
        addEquals(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public MovieSelection releaseDateNot(String... value) {
        addNotEquals(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public MovieSelection releaseDateLike(String... value) {
        addLike(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public MovieSelection releaseDateContains(String... value) {
        addContains(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public MovieSelection releaseDateStartsWith(String... value) {
        addStartsWith(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public MovieSelection releaseDateEndsWith(String... value) {
        addEndsWith(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public MovieSelection orderByReleaseDate(boolean desc) {
        orderBy(MovieColumns.RELEASE_DATE, desc);
        return this;
    }

    public MovieSelection orderByReleaseDate() {
        orderBy(MovieColumns.RELEASE_DATE, false);
        return this;
    }

    public MovieSelection duration(String... value) {
        addEquals(MovieColumns.DURATION, value);
        return this;
    }

    public MovieSelection durationNot(String... value) {
        addNotEquals(MovieColumns.DURATION, value);
        return this;
    }

    public MovieSelection durationLike(String... value) {
        addLike(MovieColumns.DURATION, value);
        return this;
    }

    public MovieSelection durationContains(String... value) {
        addContains(MovieColumns.DURATION, value);
        return this;
    }

    public MovieSelection durationStartsWith(String... value) {
        addStartsWith(MovieColumns.DURATION, value);
        return this;
    }

    public MovieSelection durationEndsWith(String... value) {
        addEndsWith(MovieColumns.DURATION, value);
        return this;
    }

    public MovieSelection orderByDuration(boolean desc) {
        orderBy(MovieColumns.DURATION, desc);
        return this;
    }

    public MovieSelection orderByDuration() {
        orderBy(MovieColumns.DURATION, false);
        return this;
    }

    public MovieSelection rating(String... value) {
        addEquals(MovieColumns.RATING, value);
        return this;
    }

    public MovieSelection ratingNot(String... value) {
        addNotEquals(MovieColumns.RATING, value);
        return this;
    }

    public MovieSelection ratingLike(String... value) {
        addLike(MovieColumns.RATING, value);
        return this;
    }

    public MovieSelection ratingContains(String... value) {
        addContains(MovieColumns.RATING, value);
        return this;
    }

    public MovieSelection ratingStartsWith(String... value) {
        addStartsWith(MovieColumns.RATING, value);
        return this;
    }

    public MovieSelection ratingEndsWith(String... value) {
        addEndsWith(MovieColumns.RATING, value);
        return this;
    }

    public MovieSelection orderByRating(boolean desc) {
        orderBy(MovieColumns.RATING, desc);
        return this;
    }

    public MovieSelection orderByRating() {
        orderBy(MovieColumns.RATING, false);
        return this;
    }

    public MovieSelection plot(String... value) {
        addEquals(MovieColumns.PLOT, value);
        return this;
    }

    public MovieSelection plotNot(String... value) {
        addNotEquals(MovieColumns.PLOT, value);
        return this;
    }

    public MovieSelection plotLike(String... value) {
        addLike(MovieColumns.PLOT, value);
        return this;
    }

    public MovieSelection plotContains(String... value) {
        addContains(MovieColumns.PLOT, value);
        return this;
    }

    public MovieSelection plotStartsWith(String... value) {
        addStartsWith(MovieColumns.PLOT, value);
        return this;
    }

    public MovieSelection plotEndsWith(String... value) {
        addEndsWith(MovieColumns.PLOT, value);
        return this;
    }

    public MovieSelection orderByPlot(boolean desc) {
        orderBy(MovieColumns.PLOT, desc);
        return this;
    }

    public MovieSelection orderByPlot() {
        orderBy(MovieColumns.PLOT, false);
        return this;
    }

    public MovieSelection movieid(String... value) {
        addEquals(MovieColumns.MOVIEID, value);
        return this;
    }

    public MovieSelection movieidNot(String... value) {
        addNotEquals(MovieColumns.MOVIEID, value);
        return this;
    }

    public MovieSelection movieidLike(String... value) {
        addLike(MovieColumns.MOVIEID, value);
        return this;
    }

    public MovieSelection movieidContains(String... value) {
        addContains(MovieColumns.MOVIEID, value);
        return this;
    }

    public MovieSelection movieidStartsWith(String... value) {
        addStartsWith(MovieColumns.MOVIEID, value);
        return this;
    }

    public MovieSelection movieidEndsWith(String... value) {
        addEndsWith(MovieColumns.MOVIEID, value);
        return this;
    }

    public MovieSelection orderByMovieid(boolean desc) {
        orderBy(MovieColumns.MOVIEID, desc);
        return this;
    }

    public MovieSelection orderByMovieid() {
        orderBy(MovieColumns.MOVIEID, false);
        return this;
    }
}
