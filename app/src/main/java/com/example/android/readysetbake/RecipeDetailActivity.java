package com.example.android.readysetbake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;


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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

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
        recipeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (findViewById(R.id.recipe_fragment_container2) ==null) {
                    if (fragmentManager.getBackStackEntryCount()> 1) {
                        //Return to Recipe Detail screen
                        fragmentManager.popBackStack(STACK_RECIPE_DETAIL, 0);
                    } else if (fragmentManager.getBackStackEntryCount()> 0) {
                        //Return to MainActivity Recipe screen
                        finish();
                    }
                }
                else {
                    //Return to MainActivity Recipe screen
                    finish();
                }

            }
        });
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
    }
}
