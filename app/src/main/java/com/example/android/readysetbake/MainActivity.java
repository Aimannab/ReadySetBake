package com.example.android.readysetbake;

import android.content.Intent;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipeListItemClickListener{
    private RecyclerView rRecyclerView;
    private RecipesAdapter recipesAdapter;
    boolean recipeTwoPane;

    static String ALL_RECIPES="All_Recipes";
    static String SELECTED_RECIPES="Selected_Recipes";
    static String SELECTED_STEPS="Selected_Steps";
    static String SELECTED_INDEX="Selected_Index";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up Toolbar
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("ReadySetBake!");

    }

    @Override
    public void onRecipeListItemClick(Recipe selectedRecipeItemIndex) {

        if (recipeTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            View view = findViewById(R.id.imageView);
            if (view.getVisibility() == View.INVISIBLE) {
                findViewById(R.id.imageView).setVisibility(View.VISIBLE);
            }
        } else {

            Bundle recipeBundleSelected = new Bundle();
            ArrayList<Recipe> recipeSelected = new ArrayList<>();
            recipeSelected.add(selectedRecipeItemIndex);
            recipeBundleSelected.putParcelableArrayList(SELECTED_RECIPES, recipeSelected);

            final Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtras(recipeBundleSelected);
            startActivity(intent);

        }
    }

        @Override
        public void onSaveInstanceState (Bundle outState){
            super.onSaveInstanceState(outState);
        }
    }


