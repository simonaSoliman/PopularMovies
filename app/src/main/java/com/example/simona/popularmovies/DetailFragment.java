package com.example.simona.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

/**
 * Created by Michael on 4/30/2016.
 */
public  class DetailFragment extends Fragment {
    Button favButton;
    boolean y;
    public int id;
    public ListView TralierListView;
    public ListView ReviewListView;
    public ArrayList<String> traliers =new ArrayList();
    private String mMoviestStr;
    public ArrayAdapter<String> tralierAdapter;
    public ArrayAdapter<String> reviewAdapter;
    MovieDbHelper dbHelper;
    Boolean A;

    public DetailFragment() {

    }
    @Override
    public void onStart(){
        super.onStart();
        FetchTrailer fetchtrailer=new FetchTrailer();
        fetchtrailer.execute(id);
        FetchReviews fetchReviews=new FetchReviews();
        fetchReviews.execute(id);
        DataExist dataExist=new DataExist();
        dataExist.execute(id);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         dbHelper=new MovieDbHelper(getActivity());

        final String LOG_TAG = FetchTrailer.class.getSimpleName();

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        TralierListView= (ListView)rootView.findViewById(R.id.listview_trailer);
        ReviewListView=(ListView)rootView.findViewById(R.id.listview_review);
       tralierAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_trailer, R.id.list_item_trailer, new ArrayList<String>());

        TralierListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //we get the forecast by calling getItem from the forecast adapter

                Uri trailerLink = Uri.parse(traliers.get(position));
                Log.v(LOG_TAG, "we can open youtube" + trailerLink.toString());

                Intent tIntent = new Intent(Intent.ACTION_VIEW, trailerLink);
                startActivity(tIntent);
            }
        });



//                }
//            });setOnItemClickListener(new ListView.OnItemClickListener() {
//                @Override
//                public void onItemClick(ListView parent, View view, int position, long id) {
//                    //we get the forecast by calling getItem from the forecast adapter
//                    Uri trailerLink = Uri.parse(traliers.get(position));
//                    Intent tIntent = new Intent(Intent.ACTION_VIEW, trailerLink);
//                    startActivity(tIntent);
//                    Log.v(LOG_TAG, "we can open youtube" + trailerLink.toString());
//                }
//            });
        reviewAdapter =new ArrayAdapter<String>(getActivity(),R.layout.list_item_review,R.id.List_item_review,new ArrayList<String>());
        ReviewListView.setAdapter(reviewAdapter);
        favButton= (Button)rootView.findViewById(R.id.radio);




             if(getArguments()!=null){
                 Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185"+getArguments().getString("moviePath")).into((ImageView) rootView.findViewById(R.id.image_view));

                 ((TextView) rootView.findViewById(R.id.overview))
                         .setText(getArguments().getString("movieOverView"));
                 ((TextView) rootView.findViewById(R.id.original_title))
                         .setText(getArguments().getString("movieTitle"));
                 ((TextView) rootView.findViewById(R.id.release_date))
                         .setText(getArguments().getString("movieReleseDate"));
                 ((TextView) rootView.findViewById(R.id.vote_average))
                         .setText( getArguments().getDouble("movieVote", 0)+ "/10");
                 // y=intent.getBooleanExtra("movieFound",false);

                 id=getArguments().getInt("movieId", -1);
                 Log.v("intent appear","appeared"+id);
                 A=true;
             }
        else if(getActivity().getIntent()!=null){
                 Intent intent=getActivity().getIntent();
                 Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185"+intent.getStringExtra("moviePath")).into((ImageView) rootView.findViewById(R.id.image_view));
                 Log.v("zzzr", intent.getStringExtra("movieOverView"));
                 Log.v("zzzr", intent.getStringExtra("movieReleseDate"));

                 ((TextView) rootView.findViewById(R.id.overview))
                         .setText(intent.getStringExtra("movieOverView"));
                 ((TextView) rootView.findViewById(R.id.original_title))
                         .setText(intent.getStringExtra("movieTitle"));
                 ((TextView) rootView.findViewById(R.id.release_date))
                         .setText(intent.getStringExtra("movieReleseDate"));
                 ((TextView) rootView.findViewById(R.id.vote_average))
                         .setText( intent.getDoubleExtra("movieVote", 0)+ "/10");
                 // y=intent.getBooleanExtra("movieFound",false);

                 id=intent.getIntExtra("movieId", -1);
                 Log.v("intent appear","appeared"+id);
                 A=false;
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
                if (type.equals("Trailer")){traliers.add("https://www.youtube.com/watch?v="+key);}




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
                String apiKey = "my api key";
                String ApiKey="api_key";
                String VIDEOS="videos";
                //to append the parameters fo the link.
                //appendQueryParameters (name of the API key, API key).

                Uri UriBulider =Uri.parse(baseUrl).buildUpon().appendPath(String.valueOf(params[0])).appendPath(VIDEOS).appendQueryParameter(ApiKey,"my api key")
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



                    }
                    TralierListView.setAdapter(tralierAdapter);
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
                String apiKey = "my api key";
                String ApiKey="api_key";
                String REVIEWS="reviews";
                //to append the parameters fo the link.
                //appendQueryParameters (name of the API key, API key).

                Uri UriBulider =Uri.parse(baseUrl).buildUpon().appendPath(String.valueOf(params[0])).appendPath(REVIEWS).appendQueryParameter(ApiKey,"my api key")
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
    public class DeleteRow extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase favDB = dbHelper.getReadableDatabase();
            try{
                String[] ID=new String[1];
                ID[0]=""+id;
                favDB.delete("MovieFav", "id=?", ID);
                favDB.close();

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        public void onPostExecute(Void param){
            favButton.setBackgroundResource(R.drawable.graystar);
           if(A==true){ favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InsertRowForTab insertRow = new InsertRowForTab();
                    insertRow.execute();
                    Log.v("delete data by button","deleted");
                }
            });} else if(A==false){
               favButton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       InsertRowForMob insertRow = new InsertRowForMob();
                       insertRow.execute();
                       Log.v("delete data by button","deleted");
                   }
               });
           }


        }

    }
    public class InsertRowForMob extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase movieDB = dbHelper.getReadableDatabase();

                Intent intent = getActivity().getIntent();
                String posterPath = intent.getStringExtra("moviePath");
                String overView = intent.getStringExtra("movieOverView");
                String originalTitle = intent.getStringExtra("movieTitle");
                String releaseDate = intent.getStringExtra("movieReleseDate");
                double voteAvg = intent.getDoubleExtra("movieVote", 0);
                Log.v("data in mobile","");
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", id);
                contentValues.put("poster_path", posterPath);
                contentValues.put("over_view", overView);
                contentValues.put("original_title", originalTitle);
                contentValues.put("release_date", releaseDate);
                contentValues.put("vote_average", voteAvg);
                Long i;
                i= movieDB.insert("MovieFav", null, contentValues);
                Log.v("inserted?",""+i);
                movieDB.close();




            return null;
        }
        public void onPostExecute(Void param){
            favButton.setBackgroundResource(R.drawable.yellwostar);
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteRow deleteRow=new DeleteRow();
                    deleteRow.execute();
                    Log.v("insert data by button","inserted");
                }
            });

        }
    }
    public class InsertRowForTab extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase movieDB = dbHelper.getReadableDatabase();
                String posterPath = getArguments().getString("moviePath");
                String overView = getArguments().getString("movieOverView");
                String originalTitle = getArguments().getString("movieTitle");
                String releaseDate = getArguments().getString("movieReleseDate");
                double voteAvg = getArguments().getDouble("movieVote", 0);

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", id);
                contentValues.put("poster_path", posterPath);
                contentValues.put("over_view", overView);
                contentValues.put("original_title", originalTitle);
                contentValues.put("release_date", releaseDate);
                contentValues.put("vote_average", voteAvg);
                Long i;
                i= movieDB.insert("MovieFav", null, contentValues);
                Log.v("inserted?",""+i);
                movieDB.close();



            return null;
        }
        public void onPostExecute(Void param){
            favButton.setBackgroundResource(R.drawable.yellwostar);
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteRow deleteRow=new DeleteRow();
                    deleteRow.execute();
                    Log.v("insert data by button","inserted");
                }
            });

        }
    }
    public class DataExist extends AsyncTask<Integer,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Integer... params) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String QUERY = "SELECT id FROM MovieFav WHERE id=" + id;
            try {
                Cursor c = db.rawQuery(QUERY, null);
                if (c.moveToFirst()) {
                    c.close();
                    y = true;
                    Log.v("data found", "true");

                } else {
                    c.close();
                    y = false;
                    Log.v("data don't found", "false");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.v("check data by button", "checked");
            return y;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (y) {
                favButton.setBackgroundResource(R.drawable.yellwostar);
                favButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteRow deleteRow = new DeleteRow();
                        deleteRow.execute();
                        Log.v("checked true ", "delete");
                    }
                });

            } else {
                favButton.setBackgroundResource(R.drawable.graystar);
               if(A==true){ favButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InsertRowForTab insertRow = new InsertRowForTab();
                        insertRow.execute();
                        Log.v("checked false", "insert");
                    }
                });}else if(A==false){
                   favButton.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           InsertRowForMob insertRow = new InsertRowForMob();
                           insertRow.execute();
                           Log.v("checked false", "insert");
                       }
                   });
               }
            }
        }


    }
}

