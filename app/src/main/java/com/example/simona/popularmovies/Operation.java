//package com.example.simona.popularmovies;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.example.simona.popularmovies.MovieDbHelper;
//
///**
// * Created by Simona on 4/27/2016.
// */
//public class Operation {
//    void addContact(Movie movie) {
//        try{
//            MovieDbHelper movieDbHelper=new MovieDbHelper(Context context);
//            SQLiteDatabase db = movieDbHelper.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put(KEY_PRICE, movie.getPrice());
//            values.put(KEY_TYPE, movie.getType());
//            values.put(KEY_DATE, movie.getDate());
//            values.put(KEY_DESCRIPTION, movie.getDescription());
//            values.put(KEY_PAY_MODE, movie.getPaymode());
//
//            // Inserting Row
//            db.insert(TABLE_INCOME, null, values);
//            db.close(); // Closing database connection
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//}
