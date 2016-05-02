package com.example.simona.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Simona on 3/23/2016.
 */
//we make an array adapter (ImageAdaper) and make it extends ArrayAdapter to extend it's methods
//we use extended ArrayAdapter by passing array as input


public class ImageAdapter extends ArrayAdapter {

    private Context Context;
    int resource;
    List<Movie> mMovies;
    public ImageAdapter(Context Context,int resource,List<Movie> mMovies) {
        //initialize it's patent in class ArrayAdapter before initialize it
        super(Context, resource, mMovies);
        this.Context=Context;
        this.resource=resource;
        this.mMovies=mMovies;

    }

    /** responsible for creating a new View for each grid item
     *  position:the position of the item
     *   convertView:get data and covert it to view
     *   parent: The parent that this view will eventually be attached to
     */

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
    View dataRow = convertView;
        //we create here the view which will have the data ,at first it will give null
    if(dataRow==null){
    //bt2ra al xml w t3mlo inflate
        LayoutInflater inflater=((Activity)Context).getLayoutInflater();
        //to specify a root view and to prevent attachment to the root.
        //de 2lly ht2olo 3andi imageview .xml hashof feh kam item w anw3hom eah w mn picasso ha7ot al data bta3ti w data row htrga3 bl data
        dataRow =inflater.inflate(resource, parent, false);

    }
        //when the datarow have view in it ,here we update it and make a new datarow
    Log.i("TAG", mMovies.get(position).getPoster_path());
        Picasso.with(Context).load("http://image.tmdb.org/t/p/w185"+mMovies.get(position).getPoster_path()).into((ImageView)dataRow.findViewById(R.id.image));
    return dataRow;
    }

    }
