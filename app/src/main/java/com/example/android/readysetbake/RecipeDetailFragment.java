package com.example.android.readysetbake;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.Gson;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.readysetbake.MainActivity.SELECTED_RECIPES;

/**
 * Created by Aiman Nabeel on 31/05/2018.
 */

//Displaying Data
public class RecipeDetailFragment extends Fragment {

    //@BindView(R.id.stepDescriptionCard) pending
    ArrayList<Recipe> recipeList;
    String recipeName;
    public static final String SHARED_PREFS_KEY_INGRED = "SHARED_PREFS_KEY";

    public RecipeDetailFragment() {

    }

    @SuppressLint("ResourceAsColor")
    @TargetApi(Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recipeRecyclerView;
        final TextView ingredientsTextView;
        final TextView instructionsTextView;
        recipeList = new ArrayList<>();
        String ingredientsList;

        TextView ingredientsTitle;
        TextView instructionsTitle;

        if(savedInstanceState != null) {
            recipeList = savedInstanceState.getParcelableArrayList(SELECTED_RECIPES);

        } else {
            recipeList =getArguments().getParcelableArrayList(SELECTED_RECIPES);
        }

        List<Ingredient> ingredients = recipeList.get(0).getIngredients();
        recipeName=recipeList.get(0).getName();

        //Inflating view for Recipe Ingredients
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ingredientsTextView = (TextView) rootView.findViewById(R.id.recipe_detail_ingredients_text);

        ArrayList<String> recipeIngredientsForWidgets= new ArrayList<>();
        recipeIngredientsForWidgets.add("ReadySetBake! - " + recipeName);

        //Setting "Ingredients" tab here
        ingredientsTitle = (TextView) rootView.findViewById(R.id.ingredientsTitle);
        ingredientsTitle.setBackgroundColor(R.color.grey);
        ingredientsTitle.append("Ingredients");

            //Ref: https://www.programcreek.com/java-api-examples/index.php?api=org.jsoup.select.Elements
            for (Ingredient i : ingredients) {

                ingredientsTextView.append("\u2022 " + i.getIngredient() + "\n");
                ingredientsTextView.append("\t\t\t Quantity: " + i.getQuantity().toString() + "\n");
                ingredientsTextView.append("\t\t\t Measure: " + i.getMeasure() + "\n\n");

                recipeIngredientsForWidgets.add(i.getIngredient() + "\n" +
                        "Quantity: " + i.getQuantity().toString() + "\n" +
                        "Measure: " + i.getMeasure() + "\n");

                //Saving Data for Shared Preferences for Widget

                Gson gson = new Gson();
                String json = gson.toJson(recipeIngredientsForWidgets);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(SHARED_PREFS_KEY_INGRED, json).commit();
        }

        //Setting up Layout Manager here
        recipeRecyclerView=(RecyclerView)rootView.findViewById(R.id.recipe_detail_recycler);
        LinearLayoutManager rLayoutManager=new LinearLayoutManager(getContext());
        recipeRecyclerView.setLayoutManager(rLayoutManager);

        //Setting "Instructions" tab here
        instructionsTitle = (TextView) rootView.findViewById(R.id.instructionsTitle);
        instructionsTitle.setBackgroundColor(R.color.grey);
        instructionsTitle.append("Instructions");

        //Initializing Detail Adapter here
        RecipesDetailAdapter recipeDetailAdapter =new RecipesDetailAdapter((RecipeDetailActivity)getActivity());
        recipeRecyclerView.setAdapter(recipeDetailAdapter);
        recipeDetailAdapter.recipeStepData(recipeList,getContext());

        //update widget
        BakeWidgetService.startBakingIngredientsService(getContext(), recipeIngredientsForWidgets);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_RECIPES, recipeList);
        currentState.putString("RecipeTitle",recipeName);
    }
}
