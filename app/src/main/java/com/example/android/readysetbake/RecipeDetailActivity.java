package com.example.android.readysetbake;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import com.google.android.exoplayer2.C;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.readysetbake.RecipeStepDetailFragment.SELECTED_POSITION;


/**
 * Created by Aiman Nabeel on 31/05/2018.
 */

////Setting up layouts
public class RecipeDetailActivity extends AppCompatActivity implements RecipesDetailAdapter.RecipeStepClickListener, RecipeStepDetailFragment.RecipeStepClickListener {

    static String ALL_RECIPES="All_Recipes";
    static String SELECTED_RECIPES="Selected_Recipes";
    static String SELECTED_STEPS="Selected_Steps";
    static String SELECTED_INDEX="Selected_Index";
    static String STACK_RECIPE_DETAIL="STACK_RECIPE_DETAIL";
    static String STACK_RECIPE_STEP_DETAIL="STACK_RECIPE_STEP_DETAIL";

    private ArrayList<Recipe> recipeList;
    String recipeName;
    private Long position;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @SuppressLint("ResourceAsColor")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        //To be used for onSaveInstance method - ExoPlayer
        position = C.TIME_UNSET;

        //Getting Recipe Name on Toolbar
        if(savedInstanceState == null) {
            Bundle recipeBundleSelected = getIntent().getExtras();
            recipeList = new ArrayList<>();
            recipeList = recipeBundleSelected.getParcelableArrayList(SELECTED_RECIPES);
            recipeName = recipeList.get(0).getName();

            //Setting up RecipeDetailFragment by replacing it with recipe_fragment_container i.e. activity_recipe_detail.xml
            final RecipeDetailFragment detailFragment = new RecipeDetailFragment();
            detailFragment.setArguments(recipeBundleSelected);
            FragmentManager detailFragmentManager = getSupportFragmentManager();
            detailFragmentManager.beginTransaction()
                    .replace(R.id.recipe_fragment_container, detailFragment).addToBackStack(STACK_RECIPE_DETAIL)
                    .commit();

            //Replacing with RecipeStepDetailFragement
            if (findViewById(R.id.recipe_detail_layout).getTag()!=null && findViewById(R.id.recipe_detail_layout).getTag().equals("tablet-land")){
                final RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();
                stepDetailFragment.setArguments(recipeBundleSelected);
                detailFragmentManager.beginTransaction()
                        .replace(R.id.recipe_fragment_container2, stepDetailFragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                        .commit();
            }

        } else {
            recipeName = savedInstanceState.getString("Title");
        }

        //Setting up each Recipe's Toolbar
        Toolbar recipeToolbar = (Toolbar) findViewById(R.id.recipeToolbar);
        setSupportActionBar(recipeToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipeName);

        //Setting up Navigation on Toolbar
        //Ref: https://www.i-programmer.info/professional-programmer/accreditation/10908-insiders-guide-to-udacity-android-developer-nanodegree-part-3-the-making-of-baking-app.html?start=1
        recipeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (findViewById(R.id.recipe_fragment_container2) ==null) {
                    if (fragmentManager.getBackStackEntryCount()> 1) {
                        //Return to Recipe Detail screen
                        fragmentManager.popBackStack();
                    } else if (fragmentManager.getBackStackEntryCount()> 0) {
                        //Return to MainActivity Recipe screen
                        getSupportFragmentManager().popBackStack();
                    }
                }
                else {
                    //Return to MainActivity Recipe screen
                    getSupportFragmentManager().popBackStack();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Enabling clickListener for RecipeStepDetailFragment here
    @Override
    public void onRecipeStepDetailItemClick(List<Step> stepsOut, int itemSelectedIndex, String recipeName) {

        final RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setTitle(recipeName);

        Bundle stepBundle = new Bundle();
        stepBundle.putParcelableArrayList(SELECTED_STEPS,(ArrayList<Step>) stepsOut);
        stepBundle.putInt(SELECTED_INDEX,itemSelectedIndex);
        stepBundle.putString("Title",recipeName);
        fragment.setArguments(stepBundle);

        if (findViewById(R.id.recipe_detail_layout).getTag()!=null && findViewById(R.id.recipe_detail_layout).getTag().equals("tablet-land")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_fragment_container2, fragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();

        }
        else {
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_fragment_container, fragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Title",recipeName);
        //For ExoPlayer
        outState.putLong(SELECTED_POSITION, position);
    }
}
