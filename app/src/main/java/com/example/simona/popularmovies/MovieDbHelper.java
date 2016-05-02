package com.example.simona.popularmovies;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Simona on 4/24/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper{
    private final String LOG_TAG =MovieDbHelper.class.getSimpleName();
    private static final  int DATABASE_VERSION =2;
    private Context context;

     static final String DATABASE_NAME = "Movie.dp";
    public MovieDbHelper(Context context){
        //super of SQLiteOpenHelper
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_TABLE="CREATE TABLE "+MovieContract.DetailFragEntry.TABLE_NAME + " (" +
                MovieContract.DetailFragEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY ," +
                MovieContract.DetailFragEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL , " +
                MovieContract.DetailFragEntry.COLUMN_OVER_VIEW + " TEXT NOT NULL , " +
                MovieContract.DetailFragEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL , " +
                MovieContract.DetailFragEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL , " +
                MovieContract.DetailFragEntry.COLUMN_RELEASE_DATE+" TEXT NOT NULL"+
                " )";
        Log.i(LOG_TAG, "database table" + SQL_CREATE_TABLE);
        try{
            sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
            Log.i(LOG_TAG, "SQL CREATE STATEMENT" + SQL_CREATE_TABLE);

        }
        catch (SQLException e){
            e.printStackTrace();
            Log.e(LOG_TAG, "creation field");
            Log.e(LOG_TAG, "Database Exception: " + e.getMessage());        }
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int oldVersion, int newVersion){
//        if(newVersion>oldVersion)
//            copyDatabase();
        try{
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MovieFav");
            onCreate(sqLiteDatabase);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
