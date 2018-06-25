package com.example.user.bakingapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.user.bakingapp.Model.Ingredient;
import com.example.user.bakingapp.Model.Recipe;
import com.example.user.bakingapp.Model.Step;
import com.example.user.bakingapp.adapters.RecipeAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.example.user.bakingapp.R;



//public class MainActivity extends AppCompatActivity implements RecipesListFragment.OnRecipeClickListener {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//    }
//
//    @Override
//    public void onRecipeSelected(Recipe recipe) {
//        Intent intent = new Intent(this, RecipeDetailActivity.class);
//        intent.putExtra(RecipeDetailActivity.RECIPE_KEY, recipe);
//        startActivity(intent);
//    }
//
//}



public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeItemClickListener {

    @BindView(R.id.tv_error_message_display) TextView errorMessageTextView;
//    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout mSwipeToRefresh;
    @BindView(R.id.rv_recipes_list) RecyclerView mRecyclerView;

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
        mSwipeToRefresh.setRefreshing(false);
//        mLoadingIndicator.setVisibility(View.GONE);
    }
    // shows error when unable to load data
    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.GONE);
        errorMessageTextView.setVisibility(View.VISIBLE);
        mSwipeToRefresh.setRefreshing(false);
//        mLoadingIndicator.setVisibility(View.GONE);
    }
    private void showLoadingIndicator(){
        mRecyclerView.setVisibility(View.GONE);
        errorMessageTextView.setVisibility(View.GONE);
        mSwipeToRefresh.setRefreshing(true);
//        mLoadingIndicator.setVisibility(View.VISIBLE);
    }


    private void makeSearchQuery() {
        if(isOnline()) {
            Log.i("ddd", getResources().getString(R.string.recipies_url));
            URL MovieUrl = NetworkTools.buildUrl(this.getResources().getString(R.string.recipies_url));
            String searchResults = null;
            new BakingQueryTask().execute(MovieUrl);
        }else {
            Log.i("ddd", "is not online ");
            showErrorMessage();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onRecipeItemClick(int ClickedItemIndex, TextView recipeName) {
        Context context = MainActivity.this;
        Log.i("zzz", "before: ");
        Class detActivity = RecipeDetailActivity.class;
        Log.i("zzz", "before0: ");
        Intent intent = new Intent(getApplicationContext(),detActivity);
//        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,(View) posterImg, "sharedPoster");
        intent.putExtra("recipe", recipeList.get(ClickedItemIndex));
        Log.i("zzz", "before1: ");
        startActivity(intent);
        Log.i("zzz", "after ");

//        Class detActivity = DetailActivity.class;
//        Intent intent = new Intent(context,detActivity);
//        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,(View) posterImg, "sharedPoster");
//        intent.putExtra("movie", movieList.get(clickedItemIndex));
//        startActivity(intent,optionsCompat.toBundle());


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
//            Log.i("ddd", "onPostExecute: " +s);
            if(s==null){
                showErrorMessage();
                return;
            }
            // parsing the response.
            recipeList = new ArrayList<>();

//            outputArraysLength = 0;


            JSONArray a = null;
            try {
                a = new JSONArray(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int l = a.length();
            for(int j = 0; j < l; j++) {
                try {
                    Log.i(j + "  ddd", a.getString(j));
                    int recipeId;
                    String recipeName;
                    ArrayList<Ingredient> ingredients = new ArrayList<>();
                    ArrayList<Step> steps = new ArrayList<>();
                    JSONObject cs = a.getJSONObject(j);
                    String str = a.getString(j);

                    recipeId = cs.getInt("id");
                    recipeName = cs.getString("name");

                    JSONObject recipeJSON;
                    JSONArray ingredientsJSON;
                    Recipe recipe = new Recipe();

                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        recipe = mapper.readValue(str, Recipe.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    Log.i("ggg", recipe.toString() );
                    recipeList.add(recipe);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            adapter.setRecipeData(recipeList);
            showGrid();
        }
    }




}
