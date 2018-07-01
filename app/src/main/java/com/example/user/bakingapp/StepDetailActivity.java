package com.example.user.bakingapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.user.bakingapp.model.Recipe;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {
    @BindView(R.id.recipe_step_viewpager)
    ViewPager vpRecipeStep;

    @BindView(R.id.recipe_step_tab_layout)
    TabLayout tabRecipeStep;

    @BindView(android.R.id.content)
    View mParentLayout;

    private Recipe recipe;
    private int mStepSelectedPosition;

    public static final String RECIPE_KEY = "recipe";
    public static final String STEP_SELECTED_KEY = "step";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(RECIPE_KEY) && bundle.containsKey(STEP_SELECTED_KEY)) {
            mStepSelectedPosition = bundle.getInt(STEP_SELECTED_KEY);
            recipe = bundle.getParcelable(RECIPE_KEY);
        } else {
            finish();
        }

        // Show the Up button in the action bar.
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(recipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        StepsFragmentPagerAdapter adapter = new StepsFragmentPagerAdapter(getApplicationContext(), recipe.getSteps(), getSupportFragmentManager());

        vpRecipeStep.setAdapter(adapter);
        tabRecipeStep.setupWithViewPager(vpRecipeStep);
        vpRecipeStep.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (actionBar != null) {
                    actionBar.setTitle(recipe.getSteps().get(position).getShortDescription());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpRecipeStep.setCurrentItem(mStepSelectedPosition);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Logger.d("onDestroy");
    }

}
