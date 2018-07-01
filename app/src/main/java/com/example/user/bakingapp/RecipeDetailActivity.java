package com.example.user.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.StepClickListener {
    public static final String RECIPE_KEY = "recipe";

    @BindView(R.id.rv_steps_list) RecyclerView mRecyclerViewSteps;
    @BindView(R.id.rv_ingredients_list) RecyclerView mRecyclerViewIngredients;
    @BindView(R.id.tv_recipe_intro) TextView recipeIntro;

    private boolean twoPaneMode;

    private StepsAdapter stepsAdapter;
    Recipe recipe;

    public ArrayList<Step> stepList;
    public ArrayList<Ingredient> ingredientsList;

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


        recipeIntro.setText(getResources().getString(R.string.recipe_intro) + " " + recipe.getName() +"!");

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getBaseContext());
        mRecyclerViewIngredients.setAdapter(ingredientsAdapter);
        mRecyclerViewIngredients.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        ingredientsList = new ArrayList<Ingredient>(recipe.getIngredients());
        ingredientsAdapter.setStepsData(ingredientsList);


        StepsAdapter stepsAdapter = new StepsAdapter(getBaseContext(), this);
        mRecyclerViewSteps.setAdapter(stepsAdapter);
        mRecyclerViewSteps.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        stepList = new ArrayList<Step>(recipe.getSteps());
        stepsAdapter.setStepsData(stepList);

        if(isTablet(this)){
            twoPaneMode = true;
            showStepDetails(0);
        }else {
            twoPaneMode = false;
        }

        //update widget ingredients according to selected recipe
        MyWidgetService.updateWidget(getBaseContext(), recipe);


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
}

