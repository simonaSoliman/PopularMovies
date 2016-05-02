package com.example.simona.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;
//implements Listener

public class MainActivity extends ActionBarActivity implements Listener {
    boolean tabletUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameDetail = (FrameLayout) findViewById(R.id.frame_detail);
        if (frameDetail == null) {
            tabletUI = false;
        } else {
            tabletUI= true;
        }

        MainActivityFragment fragment = new MainActivityFragment();
        fragment.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,fragment ).commit();


    }


    @Override
    public void setMovie(Movie movie) {
        if (tabletUI) {
            DetailFragment detailsFragment= new DetailFragment();
            Bundle bundle= new Bundle();
            bundle.putString("moviePath", movie.getPoster_path());
            bundle.putString("movieTitle", movie.getOriginal_title());
            bundle.putString("movieOverView", movie.getOverview());
            bundle.putString("movieReleseDate",movie.getRelease_date());
            bundle.putDouble("movieVote",movie.getVote_average());
            bundle.putInt("movieId", movie.getMovie_Id());

            detailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_detail,detailsFragment).commit();
        } else {
            //Case Single Pane UI
            Intent intent = new Intent(this, DetailActivity.class);

            intent.putExtra("moviePath",movie.getPoster_path());
            intent.putExtra("movieTitle",movie.getOriginal_title());
            intent.putExtra("movieOverView",movie.getOverview());
            intent.putExtra("movieReleseDate",movie.getRelease_date());
            intent.putExtra("movieVote",movie.getVote_average());
            intent.putExtra("movieId", movie.getMovie_Id());

            startActivity(intent);
        }

    }

   }




