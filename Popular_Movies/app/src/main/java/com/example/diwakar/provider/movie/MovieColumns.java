package com.example.diwakar.provider.movie;

import android.net.Uri;
import android.provider.BaseColumns;

import com.example.diwakar.provider.MovieProvider;

/**
 * Columns for the {@code movie} table.
 */
public class MovieColumns implements BaseColumns {
    public static final String TABLE_NAME = "movie";
    public static final Uri CONTENT_URI = Uri.parse(MovieProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String TITLE = "title";

    public static final String POSTERURL = "posterURL";

    public static final String RELEASE_DATE = "release_date";

    public static final String DURATION = "duration";

    public static final String RATING = "rating";

    public static final String PLOT = "plot";

    public static final String MOVIEID = "movieID";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." + _ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[]{
            _ID,
            TITLE,
            POSTERURL,
            RELEASE_DATE,
            DURATION,
            RATING,
            PLOT,
            MOVIEID
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(TITLE) || c.contains("." + TITLE)) return true;
            if (c.equals(POSTERURL) || c.contains("." + POSTERURL)) return true;
            if (c.equals(RELEASE_DATE) || c.contains("." + RELEASE_DATE)) return true;
            if (c.equals(DURATION) || c.contains("." + DURATION)) return true;
            if (c.equals(RATING) || c.contains("." + RATING)) return true;
            if (c.equals(PLOT) || c.contains("." + PLOT)) return true;
            if (c.equals(MOVIEID) || c.contains("." + MOVIEID)) return true;
        }
        return false;
    }

}
