package com.example.simona.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.simona.popularmovies.data.MovieContract;
import com.example.simona.popularmovies.data.MovieDbHelper;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

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

public class DetailActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Setting.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {
        public int id;
        public LinearListView TralierListView;
        public LinearListView ReviewListView;
        public ArrayList<String> traliers =new ArrayList();
        private String mMoviestStr;
        public ArrayAdapter<String> tralierAdapter;
        public ArrayAdapter<String> reviewAdapter;
        public DetailFragment() {

        }
        @Override
        public void onStart(){
            super.onStart();
            FetchTrailer fetchtrailer=new FetchTrailer();
            fetchtrailer.execute(id);
            FetchReviews fetchReviews=new FetchReviews();
            fetchReviews.execute(id);
        }
//        public static void setListViewHeightBasedOnItems(ListView listView){
//            ListAdapter listAdapter = listView.getAdapter();
//            if (listAdapter == null) {
//                // pre-condition
//                return;
//            }
//
//            int totalHeight = 0;
//            for (int i = 0; i < listAdapter.getCount(); i++) {
//                View listItem = listAdapter.getView(i, null, listView);
//                listItem.measure(0, 0);
//                totalHeight += listItem.getMeasuredHeight();
//            }
//
//            ViewGroup.LayoutParams params = listView.getLayoutParams();
//            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//            listView.setLayoutParams(params);
//            listView.requestLayout();
//        }

//        public static boolean CheckIsDataAlreadyInDBorNot(String TableName,
//                                                          String dbfield, String fieldValue) {
//            MovieDbHelper movieDbHelper = new MovieDbHelper(Context context);
//            SQLiteDatabase movieDB = movieDbHelper.getReadableDatabase();
//            String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
//            Cursor cursor = movieDB.rawQuery(Query, null);
//            if(cursor.getCount() <= 0){
//                cursor.close();
//                return false;
//            }
//            cursor.close();
//            return true;
//        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
         final String LOG_TAG = FetchTrailer.class.getSimpleName();

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


            TralierListView= (LinearListView)rootView.findViewById(R.id.listview_trailer);
            ReviewListView=(LinearListView)rootView.findViewById(R.id.listview_review);

            tralierAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_trailer, R.id.list_item_trailer, new ArrayList<String>());
            TralierListView.setAdapter(tralierAdapter);

            TralierListView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, long id) {
                    //we get the forecast by calling getItem from the forecast adapter
                    Uri trailerLink = Uri.parse(traliers.get(position));
                    Intent tIntent = new Intent(Intent.ACTION_VIEW,trailerLink);
                    startActivity(tIntent);
                    Log.v(LOG_TAG,"we can open youtube"+trailerLink.toString());
                }
            });
            reviewAdapter =new ArrayAdapter<String>(getActivity(),R.layout.list_item_review,R.id.List_item_review,new ArrayList<String>());
            ReviewListView.setAdapter(reviewAdapter);
            final ToggleButton favButton= (ToggleButton)rootView.findViewById(R.id.radio);
            favButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                boolean DataIn;

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    boolean DataIn;
                    Intent intent = getActivity().getIntent();
                    MovieDbHelper movieDbHelper = new MovieDbHelper(getActivity());
                    SQLiteDatabase movieDB = movieDbHelper.getReadableDatabase();

                    //check if the id of the movie is on the list or not
                    String Query = "Select * from " + MovieContract.DetailFragEntry.TABLE_NAME+ " where " + MovieContract.DetailFragEntry.COLUMN_MOVIE_ID + " = " + id;
                    Cursor cursor = movieDB.rawQuery(Query, null);
                    if(cursor.getCount() <= 0){
                        cursor.close();
                        DataIn=false;
                    }
                    cursor.close();
                    DataIn=true;
                    if (isChecked) {
                        String posterPath = intent.getStringExtra("Poster_Path");
                        String overView = intent.getStringExtra("Over_View");
                        String originalTitle = intent.getStringExtra("original_title");
                        String releaseDate = intent.getStringExtra("Release_Date");
                        double voteAvg = intent.getDoubleExtra("vote_average", 0);
                        if(DataIn==true){
                            //delete
                            movieDB.delete("MovieFav", "id='id'", null);
                        favButton.setChecked(false);
                        }
//                        Intent intent = getActivity().getIntent();
//                        MovieDbHelper movieDbHelper = new MovieDbHelper(getActivity());
//                        SQLiteDatabase movieDB = movieDbHelper.getReadableDatabase();
//                        // movieDbHelper.onOpen(movieDB);
//                        String posterPath = intent.getStringExtra("Poster_Path");
//                        String overView = intent.getStringExtra("Over_View");
//                        String originalTitle = intent.getStringExtra("original_title");
//                        String releaseDate = intent.getStringExtra("Release_Date");
//                        double voteAvg = intent.getDoubleExtra("vote_average", 0);
//                        //check if the id of the movie is on the list or not
//                        String Query = "Select * from " + MovieContract.DetailFragEntry.TABLE_NAME+ " where " + MovieContract.DetailFragEntry.COLUMN_MOVIE_ID + " = " + id;
//                        Cursor cursor = movieDB.rawQuery(Query, null);
//                        if(cursor.getCount() <= 0){
//                            cursor.close();
//                            DataIn=false;
//                        }
//                        cursor.close();
//                        DataIn=true;
                     else if(DataIn==false) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("id", id);
                            contentValues.put("poster_path", posterPath);
                            contentValues.put("over_view", overView);
                            contentValues.put("original_title", originalTitle);
                            contentValues.put("release_date", releaseDate);
                            contentValues.put("vote_average", voteAvg);
                            movieDB.insert("MovieFav", null, contentValues);
                            movieDB.close();
                            favButton.setChecked(true);
                            Toast.makeText(getActivity(), "movie is in database", Toast.LENGTH_SHORT).show();

                        }
                    }cursor.close();
                    movieDB.close();
                }

            });

            Intent intent = getActivity().getIntent();

            if (intent != null ) {
                Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185"+intent.getStringExtra("moviePath")).into((ImageView) rootView.findViewById(R.id.image_view));

                ((TextView) rootView.findViewById(R.id.overview))
                        .setText(intent.getStringExtra("movieOverView"));
                ((TextView) rootView.findViewById(R.id.original_title))
                        .setText(intent.getStringExtra("movieTitle"));
                ((TextView) rootView.findViewById(R.id.release_date))
                        .setText(intent.getStringExtra("movieReleseDate"));
                ((TextView) rootView.findViewById(R.id.vote_average))
                        .setText( intent.getDoubleExtra("movieVote", 0)+ "/10");

               id=intent.getIntExtra("movieId",-1);

            }

            return rootView;
        }
        //FROM DO IN BACKGRound we take the data from jason string ,then we give them to getDataFromJason in which will decompose the data to url/s
        //and keys after that we go to OnPostExcute
        public class FetchTrailer extends AsyncTask<Integer, Void, List<String>> {
            private final String LOG_TAG = FetchTrailer.class.getSimpleName();
            private List<String> getDataFromJsonStr( String MovieIdJsonStr) throws JSONException {
                //THE EXTRACTED objects from Json str
                final String Results="results";
                final String Movie_Key ="key";
                final String Type="type";

                // Object al string bta3t al al link kolo
                JSONObject moviesJsonlink =new JSONObject(MovieIdJsonStr);
                //hn7awl al link strings to array bta3t al results
                JSONArray moviesTralierArray = moviesJsonlink.getJSONArray(Results);
                //we constract a new array with the length of movies array length


                for (int i=0; i<moviesTralierArray.length();i++){
                    //we will get the objects in the array
                    JSONObject movie=moviesTralierArray.getJSONObject(i);
                    String key=movie.getString("key");
                    String type=movie.getString("type");
                    if (type=="Trailer"){traliers.add("https://www.youtube.com/watch?v="+key);}




                    }



                return traliers;
                }




            @Override
            protected List<String> doInBackground(Integer... params) {


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
                    String ApiKey="api_key";
                    String VIDEOS="videos";
                    //to append the parameters fo the link.
                    //appendQueryParameters (name of the API key, API key).

                    Uri UriBulider =Uri.parse(baseUrl).buildUpon().appendPath(String.valueOf(params[0])).appendPath(VIDEOS).appendQueryParameter(ApiKey,"32e2ff5162b0889faa612715d5ae09f4")
                            .build();
                    //to convert the url to string.
                    URL url=new URL(UriBulider.toString());
                    //TO LOG ERRORS
                    Log.v(LOG_TAG, "Built URI" + UriBulider.toString());
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
                    Log.v(LOG_TAG,"traliermovies JsonStr: "+ popularmoviesJsonStr);
                } catch (IOException e)

                {
                    Log.e("PlaceholderFragment", "Error ", e);
                    // to parse it.
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
            protected void onPostExecute(List<String> movieTralier){

                if (movieTralier != null) {
                    Log.v(LOG_TAG, "ARRAY LIST OF TRAILERS" + movieTralier.toString());
                    if (tralierAdapter != null) {
                        tralierAdapter.clear();

                        for (int i=1;i<=movieTralier.size();i++) {
                            tralierAdapter.add("Trailer" + i);
//                            TralierListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    //we get the forecast by calling getItem from the forecast adapter
//                                    Uri trailerLink = Uri.parse(traliers.get(position));
//                                    Intent tIntent = new Intent(Intent.ACTION_VIEW, trailerLink);
//                                    startActivity(tIntent);
//                                    Log.v(LOG_TAG, "we can open youtube" + trailerLink.toString());
//                                }
//
//                            });

                        }
//                        tralierAdapter.notifyDataSetChanged();
//                        setListViewHeightBasedOnItems(TralierListView);
                    }

                }
            }

        }
        public class FetchReviews extends AsyncTask<Integer, Void, List<String>>{

            private final String LOG_TAG = FetchReviews.class.getSimpleName();
            private List<String> getDataFromJsonStr(String MovieIdJsonStr)throws JSONException{

                final String RESULTS="results";
                final String AUTHOR="author";
                final String CONTENT="content";
                JSONObject moviesReviewslink=new JSONObject(MovieIdJsonStr);
                JSONArray moviesReviewsArray=moviesReviewslink.getJSONArray(RESULTS);
                ArrayList<String> Reviews=new ArrayList<String>();

                for (int i=0;i<moviesReviewsArray.length();i++){
                 JSONObject mReview=moviesReviewsArray.getJSONObject(i);
                    String Author=mReview.getString("author");
                    String Content=mReview.getString("content");
                    Reviews.add(Author+":"+Content+"\n");
                }
                return Reviews;
            }
            @Override
            protected List<String> doInBackground(Integer... params) {


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
                    String ApiKey="api_key";
                    String REVIEWS="reviews";
                    //to append the parameters fo the link.
                    //appendQueryParameters (name of the API key, API key).

                    Uri UriBulider =Uri.parse(baseUrl).buildUpon().appendPath(String.valueOf(params[0])).appendPath(REVIEWS).appendQueryParameter(ApiKey,"32e2ff5162b0889faa612715d5ae09f4")
                            .build();
                    //to convert the url to string.
                    URL url=new URL(UriBulider.toString());
                    //TO LOG ERRORS
                    Log.v(LOG_TAG, "Built URI" + UriBulider.toString());
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
                    Log.v(LOG_TAG,"traliermovies JsonStr: "+ popularmoviesJsonStr);
                } catch (IOException e)

                {
                    Log.e("PlaceholderFragment", "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attemping
                    // to parse it.
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
                    Log.e(LOG_TAG,e.getMessage(), e);
                    e.printStackTrace();
                }
                return  null;
            }
            @Override
            protected  void onPostExecute(List<String> movieReview){
                if (movieReview !=null){
                    Log.v(LOG_TAG, "array list of reviews"+movieReview.toString());
                    if(reviewAdapter!=null){
                        reviewAdapter.clear();

                        for (String review :movieReview){
                            reviewAdapter.add(review);
                        }

//                        reviewAdapter.notifyDataSetChanged();
//                        setListViewHeightBasedOnItems(ReviewListView);
                    }

                }
            }
        }
    }
}


