package com.example.simona.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.simona.popularmovies.data.MovieContract;
import com.example.simona.popularmovies.data.MovieDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public ImageAdapter imageAdapter;

    public MainActivityFragment() {
    }

    GridView moviesGridView;

    //this method is created when we want to choose an item

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_setting,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent intent = new Intent(getActivity(), Setting.class);
            startActivity(intent);
            Log.i("settingIntent","we passed here");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_main, container, false);
        moviesGridView=(GridView)rootView.findViewById(R.id.gridView);

        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie =(Movie) imageAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("moviePath",movie.getPoster_path());
                intent.putExtra("movieTitle",movie.getOriginal_title());
                intent.putExtra("movieOverView",movie.getOverview());
                intent.putExtra("movieReleseDate",movie.getRelease_date());
                intent.putExtra("movieVote",movie.getVote_average());
                intent.putExtra("movieId",movie.getMovie_Id());

                startActivity(intent);
            }

        });
        setHasOptionsMenu(true);
        return rootView;
    }
    private void ChangeSort() {
        FetchPopMovies MoviesTask = new FetchPopMovies(getActivity(),R.layout.imageview);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.SORT_KEY),
                getString(R.string.action_sort_by_popular));
        MoviesTask.execute(location);
    }

    @Override
    public void onStart() {
        super.onStart();
        ChangeSort();
    }


//talt 7aga da 2l 7aga 2lly bn5odha>>(Movie[])
//
    public class FetchPopMovies extends AsyncTask<String, Movie, List<Movie>> {
Context context;
    int resours;
    public  FetchPopMovies(Context context,int resours){
       this.context=context;
        this.resours=resours;




    }
        private final String LOG_TAG = FetchPopMovies.class.getSimpleName();
    private List<Movie> getDataFromJsonStr( String popularmoviesJsonStr) throws JSONException {
        //THE EXTRACTED objects from Json str
        final String Poster_Path ="poster_path";
        final String Over_View ="overview";
        final String Release_Date="release_date";
        final String Original_Title ="original_title";
        final String Vote_Average ="vote_average";
        final String Results ="results";
        final String Movie_Id ="id";

        // Object al string bta3t al al link kolo
        JSONObject moviesJsonlink =new JSONObject(popularmoviesJsonStr);
        //hn7awl al link strings to array bta3t al results
        JSONArray moviesArray = moviesJsonlink.getJSONArray(Results);
       //we constract a new array with the length of movies array length
        ArrayList<Movie> movies =new ArrayList();

        for (int i=0; i<moviesArray.length();i++){
            String ChooseSort;
            //we will get the objects in the array
            JSONObject movie=moviesArray.getJSONObject(i);
             String original_title=movie.getString(Original_Title);
            Movie movieObj=new Movie();
            movieObj.setOriginal_title(original_title);

            String poster_path=movie.getString(Poster_Path);
            movieObj.setPoster_path(poster_path);

            String overview=movie.getString(Over_View);
            movieObj.setOver_view(overview);

            double  vote_average=movie.getDouble(Vote_Average);
            movieObj.setVote_average(vote_average);

            String release_date=movie.getString(Release_Date);
            movieObj.setRelease(release_date);

            int id =movie.getInt(Movie_Id);
            movieObj.setMovie_Id(id);
            movies.add(movieObj);
//            ChooseSort=ChooseSort_Type(SortType);

        }

        return movies ;
    }

        @Override
        protected List<Movie> doInBackground(String... params) {

//            if (params.length == 0) {
//                return null;
//            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            //3AYEZ ya3ml connection
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string (2lly rg3ali mn al API).
            String popularmoviesJsonStr = null;

            try

            {



                String baseUrl = "http://api.themoviedb.org/3/movie/";
                String apiKey = "32e2ff5162b0889faa612715d5ae09f4";
                String topRated ="top_rated";
                String popMovies ="popular";
                String ApiKey="api_key";
                //to append the parameters fo the link.
                //appendQueryParameters (name of the API key, API key).
                Uri UriBulider =Uri.parse(baseUrl).buildUpon().appendPath(params[0]).appendQueryParameter(ApiKey,"32e2ff5162b0889faa612715d5ae09f4")
                        .build();
                // if my favourite movies option selected
                if(params[0].equals("action_show_fav")){
                    List<Movie> favouriteMovies = null;
                    MovieDbHelper movieDbHelper = new MovieDbHelper(getActivity());
                    SQLiteDatabase db= movieDbHelper.getReadableDatabase();
                    Cursor cursor =movieDbHelper.getDataFromRow(db);
                    String posterPath,overView,originalTitle,releaseDate;
                    double voteAvg;
                    int id;
                    if(cursor.moveToFirst()) {
                        do {
                            posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.DetailFragEntry.COLUMN_POSTER_PATH));
                            overView = cursor.getString(cursor.getColumnIndex(MovieContract.DetailFragEntry.COLUMN_OVER_VIEW));
                            originalTitle = cursor.getString(cursor.getColumnIndex(MovieContract.DetailFragEntry.COLUMN_ORIGINAL_TITLE));
                            releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.DetailFragEntry.COLUMN_RELEASE_DATE));
                            voteAvg = cursor.getDouble(cursor.getColumnIndex(MovieContract.DetailFragEntry.COLUMN_VOTE_AVERAGE));
                            id = cursor.getInt(cursor.getColumnIndex(MovieContract.DetailFragEntry.COLUMN_MOVIE_ID));
                            Movie movie = new Movie();
                            movie.setPoster_path(posterPath);
                            movie.setOver_view(overView);
                            movie.setOriginal_title(originalTitle);
                            movie.setRelease(releaseDate);
                            movie.setVote_average(voteAvg);
                            movie.setMovie_Id(id);
                            favouriteMovies.add(movie);
                        }
                        while (cursor.moveToNext());
                    }
                    cursor.close();
                    db.close();

                    return favouriteMovies;
                }
                //to convert the url to string.
                URL url=new URL(UriBulider.toString());
                //TO LOG ERRORS
                Log.v(LOG_TAG,"Built URI"+UriBulider.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                //LW HWA MGLOSH DATA
                if (inputStream == null) {
                    //hy3ml crash lw hya b null.
                    popularmoviesJsonStr=null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                //lsa by2ra w byzawd .
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                //zai o5taha crach.
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                popularmoviesJsonStr = buffer.toString();
                Log.v(LOG_TAG,"popularmovies JsonStr: "+ popularmoviesJsonStr);
            } catch (IOException e)

            {
                Log.e("PlaceholderFragment", "Error ", e);

                popularmoviesJsonStr= null;
            }
            //kda kda hytnafz.
            finally

            {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try{
                //al method de ba3d ma bt7wali al data 2lly m3aya le json hatrg3li 2lly ana 3wzah
                return getDataFromJsonStr(popularmoviesJsonStr);
            }
            catch(JSONException e){
                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();
            }
            return  null;
        }
    protected void onPostExecute(List<Movie> movies){

        if (movies != null) {
            if (imageAdapter != null) {
                imageAdapter.clear();
                for (Movie movie : movies) {
                    imageAdapter.add(movie);
                }

            }
            else{
                imageAdapter =new ImageAdapter(context,resours,movies);
                moviesGridView.setAdapter(imageAdapter);
            }
        }
    }

  }
}

