package com.example.simona.popularmovies;

/**
 * Created by Simona on 3/27/2016.
 * to get image item
 */
public class Movie {
    String Original_Tital;
    String Poster_path;
    String overview;
    double vote_average;
    String release_date;
    int id;



    public void setOriginal_title(String s){
        Original_Tital=s;
    }
    public void setPoster_path(String s){
        Poster_path=s;
    }
    public void setOver_view(String s){
        overview=s;
    }
    public void setVote_average(double v){
        vote_average=v;
    }

    public void setRelease(String s){
        release_date=s;
    }
    public void setMovie_Id(int v){
       id=v;
    }
    public String getOriginal_title(){
        return Original_Tital;
    }
    public String getPoster_path(){
        return Poster_path;
    }
    public String getOverview(){
        return overview;
    }
    public String getRelease_date(){
        return release_date;
    }
    public double getVote_average(){
        return vote_average;
    }
    public int getMovie_Id(){
        return id;
    }


}
