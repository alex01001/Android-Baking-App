package com.example.user.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.user.bakingapp.model.Ingredient;
import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.model.Step;
import com.example.user.bakingapp.widget.MyWidgetService;
import com.example.user.bakingapp.adapters.IngredientsAdapter;
import com.example.user.bakingapp.adapters.StepsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.StepClickListener, Switch.OnCheckedChangeListener{
    public static final String RECIPE_KEY = "recipe";

    @BindView(R.id.rv_steps_list) RecyclerView mRecyclerViewSteps;
    @BindView(R.id.rv_ingredients_list) RecyclerView mRecyclerViewIngredients;
    @BindView(R.id.widget_switch) Switch widgetSwitch;


    private boolean twoPaneMode;



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

        if(isTablet(this)){
            twoPaneMode = true;
            showStepDetails(0);
        }else {
            twoPaneMode = false;
        }


    }

    public void showStepDetails(int pos){
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeStepDetailFragment.STEP_KEY, recipe.getSteps().get(pos));
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_detail_container, fragment)
                .commit();
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }



    @Override
    public void onStepItemClick(int ClickedItemIndex, TextView recipeName) {

        if(twoPaneMode){
            showStepDetails(ClickedItemIndex);
        }
        else {
            Context context = this;
            Class detActivity = StepDetailActivity.class;
            Intent intent = new Intent(getApplicationContext(), detActivity);
            intent.putExtra("recipe", recipe);
            intent.putExtra("step", ClickedItemIndex);
            startActivity(intent);

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            MyWidgetService.updateWidget(this, recipe);
        }

    }


}

