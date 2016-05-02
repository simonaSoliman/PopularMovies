package com.example.simona.popularmovies;

import android.provider.BaseColumns;

/**
 * Created by Simona on 4/23/2016.
 */

public class MovieContract {

    public static final class DetailFragEntry implements BaseColumns {

        public static final String TABLE_NAME = "MovieFav";

        public static final String COLUMN_MOVIE_ID = "id";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_OVER_VIEW = "over_view";


        public static final String COLUMN_ORIGINAL_TITLE = "original_title";


        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";


    }
}
