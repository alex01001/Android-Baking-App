package com.example.user.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.bakingapp.Model.Ingredient;
import com.example.user.bakingapp.Model.Recipe;
import com.example.user.bakingapp.Model.Step;
import com.example.user.bakingapp.adapters.IngredientsAdapter;
import com.example.user.bakingapp.adapters.StepsAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.StepClickListener{
    public static final String RECIPE_KEY = "recipe";

    @BindView(R.id.rv_steps_list) RecyclerView mRecyclerViewSteps;
    @BindView(R.id.rv_ingredients_list) RecyclerView mRecyclerViewIngredients;


    private boolean mTwoPane;



    //    @BindView(R.id.iv_thumbnail)
//    ImageView dvThumbnail;
//    @BindView(R.id.tv_ingredients_description) TextView ingredientsDescription;
//    @BindView(R.id.lv_ingredients) ListView ingredientsList;
    @BindView(R.id.tv_recipe_intro) TextView recipeIntro;

//    @BindView(R.id.tv_release_date) TextView dvReleaseDate;
//    @BindView(R.id.tv_user_rating) TextView dvRating;
//    @BindView(R.id.tv_overview) TextView dvOverview;
//    @BindView(R.id.btn_favorite)
//    ImageButton favButton;
//    @BindView(R.id.rv_trailersList) RecyclerView mRecyclerViewTrailers;
//    @BindView(R.id.rv_reviewsList) RecyclerView mRecyclerViewReviews;

    private StepsAdapter stepsAdapter;
    Recipe recipe;

    public ArrayList<Step> stepList;
    public ArrayList<Ingredient> ingredientsList;


//    @BindView(R.id.recipe_step_list)
//    RecyclerView mRecyclerView;
//
//    @BindView(android.R.id.content)
//    View mParentLayout;
//
//    private boolean mTwoPane;
//
//    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detailed_view);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(RECIPE_KEY)) {
            recipe = getIntent().getParcelableExtra(RECIPE_KEY);
        }
        else{
            finish();
        }


        recipeIntro.setText("Hello my friend, let's make " + recipe.getName() +"!");
        //ingredientsDescription.setText(recipe.toString());
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getBaseContext());
//        ingredientsList.setAdapter(ingredientsAdapter);


//        StepsAdapter stepsAdapter = new StepsAdapter(getBaseContext(), this);
        mRecyclerViewIngredients.setAdapter(ingredientsAdapter);
        mRecyclerViewIngredients.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        ingredientsList = new ArrayList<Ingredient>(recipe.getIngredients());
        ingredientsAdapter.setStepsData(ingredientsList);


        StepsAdapter stepsAdapter = new StepsAdapter(getBaseContext(), this);
        mRecyclerViewSteps.setAdapter(stepsAdapter);
        mRecyclerViewSteps.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        stepList = new ArrayList<Step>(recipe.getSteps());
//        stepList = new ArrayList<>();
//        stepList.add(recipe.getSteps().get(0));
        stepsAdapter.setStepsData(stepList);


        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeStepDetailFragment.STEP_KEY, recipe.getSteps().get(0));
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_detail_container, fragment)
                .commit();



//        ListView.LayoutParams mParam = new ListView.LayoutParams((int)(ListView.LayoutParams.MATCH_PARENT),(int)(ListView.LayoutParams.WRAP_CONTENT));
//        ingredientsList.setLayoutParams(mParam);
        //ingredientsList.requestLayout();

//        ViewGroup.LayoutParams params = ingredientsList.getLayoutParams();
////        params.height = ListView.LayoutParams.WRAP_CONTENT;
//        params.height = params.height * ingredientsList.getCount();
//        ingredientsList.setLayoutParams(params);
//        ingredientsList.requestLayout();


//        View listItem = (View) ingredientsList.getAdapter().getItem(0);
//
//        int totalHeight = 0;
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(ingredientsList.getWidth(), View.MeasureSpec.AT_MOST);
//        ViewGroup.LayoutParams params = ingredientsList.getLayoutParams();
////        ViewGroup.LayoutParams paramsItem = listItem.getLayoutParams();
//
////        listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//        params.height = params.height * ingredientsList.getCount() + (ingredientsList.getDividerHeight() * (ingredientsAdapter.getCount()));
////        totalHeight += listItem.getMeasuredHeight();
//        ingredientsList.setLayoutParams(params);
//        ingredientsList.requestLayout();


//        setListViewHeightBasedOnChildren(ingredientsList);


//        int totalHeight = 0;
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(ingredientsList.getWidth(), View.MeasureSpec.AT_MOST);
//        for (int i = 0; i < ingredientsAdapter.getCount(); i++) {
////            View listItem = ingredientsAdapter.getView(i, null, ingredientsList);
////            View listItem = ingredientsAdapter.getView(i,null,ingredientsList);
//            View listItem = (View) ingredientsList.getAdapter().getItem(i);
//            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
////            totalHeight += listItem.getMeasuredHeight();
//            totalHeight += listItem.getMeasuredHeight();
//            Log.i("hhh", String.valueOf(totalHeight));
//        }
//
//        ViewGroup.LayoutParams params = ingredientsList.getLayoutParams();
//        params.height = totalHeight + (ingredientsList.getDividerHeight() * (ingredientsAdapter.getCount() - 1));
////        params.height = 100;
//        ingredientsList.setLayoutParams(params);
//        ingredientsList.requestLayout();




//        int totalHeight = 0;
//        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//        listView.requestLayout();


//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null && bundle.containsKey(RECIPE_KEY)) {
//            mRecipe = bundle.getParcelable(RECIPE_KEY);
//        } else {
//            Misc.makeSnackBar(this, mParentLayout, getString(R.string.failed_to_load_recipe), true);
//            finish();
//        }
//
//        setContentView(R.layout.activity_recipe_info);
//        ButterKnife.bind(this);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        // Show the Up button in the action bar and set recipes name as title.
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle(mRecipe.getName());
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//
//        mTwoPane = getResources().getBoolean(R.bool.twoPaneMode);
//        if (mTwoPane) {
//            // The detail container view will be present only in the
//            // large-screen layouts (res/values-w900dp).
//            // If this view is present, then the
//            // activity should be in two-pane mode.
//
//            // If there is no fragment state and the recipe contains steps, show the 1st one
//            if (savedInstanceState == null && !mRecipe.getSteps().isEmpty()) {
//                showStep(0);
//            }
//        }
//
//        setupRecyclerView();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


//    class IngredientsAdapter extends BaseAdapter {
//
//
//        @Override
//        public int getCount() {
//            if(recipe==null) return 0;
//            return recipe.getIngredients().size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if( convertView == null ) {
//                convertView = getLayoutInflater().inflate(R.layout.ingredients_item, null);
//            }
//            TextView tvIngredient = (TextView) convertView.findViewById(R.id.tv_ingredient_item);
//            TextView tvMeasure = (TextView) convertView.findViewById(R.id.tv_ingredient_measure);
//            TextView tvQuant = (TextView) convertView.findViewById(R.id.tv_ingredient_quantity);
//            tvIngredient.setText((CharSequence) recipe.getIngredients().get(position).getIngredient());
//            tvMeasure.setText((CharSequence) recipe.getIngredients().get(position).getMeasure());
//            tvQuant.setText(String.valueOf(recipe.getIngredients().get(position).getQuantity()));
//
//            return convertView;
//        }
//    }



//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Logger.d("onDestroy");
//    }
//
//    private void setupRecyclerView() {
////        mRecyclerView.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.margin_medium)));
////        mRecyclerView.setAdapter(new RecipeAdapter(mRecipe, new Listeners.OnItemClickListener() {
////            @Override
////            public void onItemClick(int position) {
////                showStep(position);
////            }
////        }));
//    }
//
//    private void showStep(int position) {
////        if (mTwoPane) {
////            Bundle arguments = new Bundle();
////            arguments.putParcelable(RecipeStepDetailFragment.STEP_KEY, mRecipe.getSteps().get(position));
////            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
////            fragment.setArguments(arguments);
////            getSupportFragmentManager().beginTransaction()
////                    .replace(R.id.recipe_step_detail_container, fragment)
////                    .commit();
////        } else {
////            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
////            intent.putExtra(RecipeStepDetailActivity.RECIPE_KEY, mRecipe);
////            intent.putExtra(RecipeStepDetailActivity.STEP_SELECTED_KEY, position);
////            startActivity(intent);
////        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        super.onCreateOptionsMenu(menu);
////        MenuInflater inflater = getMenuInflater();
////        inflater.inflate(R.menu.recipe_info, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        if (item.getItemId() == R.id.action_add_to_widget) {
////            AppWidgetService.updateWidget(this, mRecipe);
////            Misc.makeSnackBar(this, mParentLayout, String.format(getString(R.string.added_to_widget), mRecipe.getName()), false);
////
////            return true;
////        } else
//           return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onStepItemClick(int ClickedItemIndex, TextView recipeName) {

        Context context = this;
        Class detActivity = StepDetailActivity.class;
        Intent intent = new Intent(getApplicationContext(),detActivity);
//        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,(View) posterImg, "sharedPoster");
        intent.putExtra("recipe", recipe);
        intent.putExtra("step", ClickedItemIndex);
        startActivity(intent);


    }
}

