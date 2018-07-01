package com.example.user.bakingapp;

import android.content.Context;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;

import com.example.user.bakingapp.model.Recipe;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import android.support.test.rule.ActivityTestRule;

public class RecipesTest {


    protected GlobalApplication globalApplication;
    protected IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
//        globalApplication = (GlobalApplication) activityTestRule.getActivity().getApplicationContext();
        globalApplication = new GlobalApplication();
        mIdlingResource =  globalApplication.getIdlingResource();
        // Register Idling Resources
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }



    public static void getMeToRecipeInfo(int recipePosition) {
        onView(withId(R.id.rv_recipes_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(recipePosition, click()));
    }

    public static void selectRecipeStep(int recipeStep) {
        onView(withId(R.id.rv_steps_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(recipeStep, click()));
    }

    @Test
    public void clickRecyclerViewItemHasIntentWithAKey() {
        //Checks if the key is present
        Intents.init();
//        getMeToRecipeInfo(0);
        onView(withId(R.id.rv_recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasExtraWithKey(RecipeDetailActivity.RECIPE_KEY));
        Intents.release();

//        onView(withId(R.id.rv_steps_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
    }

    @Test
    public void clickOnRecyclerViewItem_opensRecipeDetailActivity() {

        getMeToRecipeInfo(0);

//        onView(withId(R.id.tv_ingredient_item))
//                .check(matches(isDisplayed()));

//        onView(withId(R.id.rv_steps_list))
//                .check(matches(isDisplayed()));
    }

//    @Test
//    public void clickOnRecyclerViewStepItem_opensRecipeStepActivity_orFragment() {
//        getMeToRecipeInfo(0);
//
////        boolean twoPaneMode = globalApplication.getResources().getBoolean(R.bool.twoPaneMode);
////        if (!twoPaneMode) {
//        // Checks if the keys are present and the intent launched is RecipeStepDetailActivity
//
//        onView(withId(R.id.rv_steps_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
//
////        Intents.init();
////        selectRecipeStep(1);
////        intended(hasComponent(StepDetailActivity.class.getName()));
////        intended(hasExtraWithKey(StepDetailActivity.RECIPE_KEY));
////        intended(hasExtraWithKey(StepDetailActivity.STEP_SELECTED_KEY));
////        Intents.release();
//
//        // Check TabLayout
////        onView(withId(R.id.recipe_step_tab_layout))
////                .check(matches(isCompletelyDisplayed()));
//
//
//        //        } else {
////            Navigation.selectRecipeStep(1);
////
////            onView(withId(R.id.exo_player_view))
////                    .check(matches(isDisplayed()));
////        }
//    }

 }