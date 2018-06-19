package com.example.user.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.bakingapp.Model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeItemClickListener {

    @BindView(R.id.tv_error_message_diaplay) TextView errorMessageTextView;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    @BindView(R.id.rv_grid) RecyclerView mRecyclerView;

    public ArrayList<Recipe> recipeList;
    private RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        adapter = new RecipeAdapter(getBaseContext(), this);
        mRecyclerView.setAdapter(adapter);
//        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
//        }

        // avoid reloading the list from the internet on device rotate
//        if(savedInstanceState!=null){
//            recipeList = savedInstanceState.getParcelableArrayList("RECIPE_LIST");
//            adapter.setMovieData(recipeList);
//            showPosterGrid();
//        }
//        else{
            makeSearchQuery();
//        }



    }

       // check if we are connected to a network
    public boolean isOnline() {
        android.net.ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showGrid(){
        mRecyclerView.setVisibility(View.VISIBLE);
        errorMessageTextView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.GONE);
    }
    // shows error when unable to load data
    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.GONE);
        errorMessageTextView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.GONE);
    }
    private void showLoadingIndicator(){
        mRecyclerView.setVisibility(View.GONE);
        errorMessageTextView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }


    private void makeSearchQuery() {
        if(isOnline()) {
            URL MovieUrl = NetworkTools.buildUrl(this.getResources().getString(R.string.recipies_url));
            String searchResults = null;
            new BakingQueryTask().execute(MovieUrl);
        }else {
            showErrorMessage();
        }
    }

    @Override
    public void onRecipeItemClick(int ClickedItemIndex, ImageView posterImg) {

    }

    public class BakingQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingIndicator();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String searchResults = null;
            try {
                searchResults = NetworkTools.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s==null){
                showErrorMessage();
                return;
            }
            // parsing the response.
            recipeList = new ArrayList<>();
//            outputArraysLength = 0;


//            JSONArray a = null;
//            try {
//                a = new JSONArray(s);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            int l = a.length();
//            for(int i = 0; i < l; i++) {
//                try {
//                    Log.i(i + "  ttrr", a.getString(i));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }

//            if(s!=null && !s.equals("")) {
//                JSONObject recipeJSON;
//                JSONArray recipies;
//                try {
//                    recipeJSON = new JSONObject(s);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    return;
//                }
//                try {
//                    recipies = recipeJSON.getJSONArray("results");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    return;
//                }
//
//                for (int i=0; i<movies.length(); i++) {
//                    try {
//                        JSONObject movieItem =  movies.getJSONObject(i);
//                        Movie movie = new Movie();
//                        movie.setVoteCount(movieItem.getInt("vote_count"));
//                        movie.setId(movieItem.getInt("id"));
//                        movie.setVideo(movieItem.getBoolean("video"));
//                        movie.setVoteAverage(movieItem.getDouble("vote_average"));
//                        movie.setTitle(movieItem.getString("title"));
//                        movie.setPopularity(movieItem.getDouble("popularity"));
//                        movie.setPosterPath(movieItem.getString("poster_path"));
//                        movie.setOriginalLanguage(movieItem.getString("original_language"));
//                        movie.setOriginalTitle(movieItem.getString("original_title"));
//
//                        List<Integer> genIDs = new ArrayList<Integer>();
//                        JSONArray genre_ids = movieItem.getJSONArray("genre_ids");
//                        for (int j=0; j<genre_ids.length(); j++) {
//                            genIDs.add(genre_ids.getInt(j));
//                        }
//                        movie.setGenreIds(genIDs);
//                        movie.setBackdropPath(movieItem.getString("backdrop_path"));
//                        movie.setAdult(movieItem.getBoolean("adult"));
//                        movie.setOverview(movieItem.getString("overview"));
//                        movie.setPosterPath(movieItem.getString("poster_path"));
//                        movie.setReleaseDate(movieItem.getString("release_date"));
//                        movieList.add(movie);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                adapter.setMovieData(movieList);
//                showPosterGrid();
//            }
        }
    }




}
