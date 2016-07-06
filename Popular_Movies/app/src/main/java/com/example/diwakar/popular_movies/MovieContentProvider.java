package com.example.diwakar.popular_movies;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by diwakar on 7/6/16.
 */
public class MovieContentProvider extends ContentProvider {

    private static final String AUTH = "com.example.diwakar.popular_movies.MovieContentProvider";
    private static final String MOVIES_PATH = "MOVIES";
    private static final Uri MOVIES_URI = Uri.parse("content://" + AUTH + MOVIES_PATH);
    private static final UriMatcher uriMatcher;
    final static int MOVIES_ID = 1;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTH, MOVIES_PATH, MOVIES_ID);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
