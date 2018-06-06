package com.example.android.readysetbake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v4.app.FragmentManager;


/**
 * Created by Aiman Nabeel on 31/05/2018.
 */

public class RecipeDetailActivity extends AppCompatActivity {

    String recipeName;
    static String ALL_RECIPES="All_Recipes";
    static String SELECTED_RECIPES="Selected_Recipes";
    static String SELECTED_STEPS="Selected_Steps";
    static String SELECTED_INDEX="Selected_Index";
    static String STACK_RECIPE_DETAIL="STACK_RECIPE_DETAIL";
    static String STACK_RECIPE_STEP_DETAIL="STACK_RECIPE_STEP_DETAIL";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

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
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                if (findViewById(R.id.recipe_fragment_container) ==null) {
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


}
