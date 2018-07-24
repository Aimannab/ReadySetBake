package com.example.android.readysetbake;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

import static com.example.android.readysetbake.MainActivity.ALL_RECIPES;


/**
 * Created by Aiman Nabeel on 31/05/2018.
 */

public class RecipeFragment extends Fragment{

    public static final String SHARED_PREFS_KEY = "SHARED_PREFS_KEY";


    public RecipeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView;

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_recycler);
        final RecipesAdapter recipesAdapter = new RecipesAdapter((MainActivity) getActivity());
        recyclerView.setAdapter(recipesAdapter);

        //Setting up Grid Layout for the MainActivity UI
        if (rootView.getTag() != null && rootView.getTag().equals("phone-land")) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
            recyclerView.setLayoutManager(mLayoutManager);
        } else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
        }

        //Using Retrofit to extract Recipes List for MainActiity UI
        RecipesApiService iRecipe = RetrofitBuilder.Retrieve();
        Call<ArrayList<Recipe>> recipe = iRecipe.getRecipe();

        final SimpleIdlingResource idlingResource = (SimpleIdlingResource) ((MainActivity) getActivity()).getIdlingResource();

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        recipe.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                Integer statusCode = response.code();
                Log.v("status code: ", statusCode.toString());

                ArrayList<Recipe> recipes = response.body();

                Bundle recipesBundle = new Bundle();
                recipesBundle.putParcelableArrayList(ALL_RECIPES, recipes);

                recipesAdapter.setRecipeList(recipes, getContext());
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }

                    //Saving Data for Shared Preferences for Widget
                    ArrayList<String> recipeName = new ArrayList<>();
                    recipeName.add("ReadySetBake!");
                    recipeName.add(recipes.get(0).getName());
                    recipeName.add(recipes.get(1).getName());
                    recipeName.add(recipes.get(2).getName());
                    recipeName.add(recipes.get(3).getName());

                    Gson gson = new Gson();
                    String json = gson.toJson(recipeName);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(SHARED_PREFS_KEY, json).commit();

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.v("http fail: ", t.getMessage());
            }
        });

        return rootView;
    }

}

