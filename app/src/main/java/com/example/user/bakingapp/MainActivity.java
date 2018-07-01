package com.example.user.bakingapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.bakingapp.model.Ingredient;
import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.model.Step;
import com.example.user.bakingapp.widget.MyWidgetService;
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


public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeItemClickListener {

    @BindView(R.id.tv_error_message_display) TextView errorMessageTextView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout mSwipeToRefresh;
    @BindView(R.id.rv_recipes_list) RecyclerView mRecyclerView;
    @BindView(R.id.layout_main_screen) LinearLayout mainLayout;

    public ArrayList<Recipe> recipeList;
    private RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        adapter = new RecipeAdapter(getBaseContext(), this);
        mRecyclerView.setAdapter(adapter);

        if(isTablet(this)){
            mRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),3));
        }else{
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        }

        if(isTablet(this)){
            mRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),3));
        }else{
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mainLayout.setBackgroundResource(R.drawable.backgtound);
        }else{
            mainLayout.setBackgroundResource(R.drawable.backgtound2);
        }
        // avoid reloading the list from the internet on device rotate
        if(savedInstanceState!=null){
            recipeList = savedInstanceState.getParcelableArrayList("RECIPE_LIST");
            adapter.setRecipeData(recipeList);
            showGrid();
        }
        else{
            makeSearchQuery();
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("RECIPE_LIST", recipeList);
    }
    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
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
    }
    // shows error when unable to load data
    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.GONE);
        errorMessageTextView.setVisibility(View.VISIBLE);
        mSwipeToRefresh.setRefreshing(false);
    }
    private void showLoadingIndicator(){
        mRecyclerView.setVisibility(View.GONE);
        errorMessageTextView.setVisibility(View.GONE);
        mSwipeToRefresh.setRefreshing(true);
    }


    private void makeSearchQuery() {
        if(isOnline()) {
            URL MovieUrl = Utils.buildUrl(this.getResources().getString(R.string.recipies_url));
            String searchResults = null;
            new BakingQueryTask().execute(MovieUrl);
        }else {
            showErrorMessage();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onRecipeItemClick(int ClickedItemIndex, TextView recipeName) {
        Context context = MainActivity.this;
        Class detActivity = RecipeDetailActivity.class;
        Intent intent = new Intent(getApplicationContext(),detActivity);
        intent.putExtra("recipe", recipeList.get(ClickedItemIndex));
        startActivity(intent);
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
                searchResults = Utils.getResponseFromHttpUrl(searchUrl);
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

            JSONArray a = null;
            try {
                a = new JSONArray(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int l = a.length();
            for(int j = 0; j < l; j++) {
                try {
                    ArrayList<Ingredient> ingredients = new ArrayList<>();
                    ArrayList<Step> steps = new ArrayList<>();
                    JSONObject cs = a.getJSONObject(j);
                    String str = a.getString(j);

                    Recipe recipe = new Recipe();

                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        recipe = mapper.readValue(str, Recipe.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
