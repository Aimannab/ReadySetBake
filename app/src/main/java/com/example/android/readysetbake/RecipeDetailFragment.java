package com.example.android.readysetbake;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import com.example.android.readysetbake.RecipesDetailAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.jsoup.Jsoup;
import butterknife.BindView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.readysetbake.MainActivity.SELECTED_RECIPES;

/**
 * Created by Aiman Nabeel on 31/05/2018.
 */

//Displaying Data
public class RecipeDetailFragment extends Fragment {

    //@BindView(R.id.stepDescriptionCard)
    ArrayList<Recipe> recipeList;
    String recipeName;

    public RecipeDetailFragment() {

    }

    @TargetApi(Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recipeRecyclerView;
        final TextView ingredientsTextView;
        recipeList = new ArrayList<>();

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

        //Ref: https://www.programcreek.com/java-api-examples/index.php?api=org.jsoup.select.Elements
        ingredients.forEach((a) ->
        {
            ingredientsTextView.append("\u2022 "+ a.getIngredient()+"\n");
            ingredientsTextView.append("\t\t\t Quantity: "+a.getQuantity().toString()+"\n");
            ingredientsTextView.append("\t\t\t Measure: "+a.getMeasure()+"\n\n");

            recipeIngredientsForWidgets.add(a.getIngredient()+"\n"+
                    "Quantity: "+a.getQuantity().toString()+"\n"+
                    "Measure: "+a.getMeasure()+"\n");
        });

        //Setting up Layout Manager here
        recipeRecyclerView=(RecyclerView)rootView.findViewById(R.id.recipe_detail_recycler);
        LinearLayoutManager rLayoutManager=new LinearLayoutManager(getContext());
        recipeRecyclerView.setLayoutManager(rLayoutManager);

        //Initializing Detail Adapter here
        RecipesDetailAdapter recipeDetailAdapter =new RecipesDetailAdapter((RecipeDetailActivity)getActivity());
        recipeRecyclerView.setAdapter(recipeDetailAdapter);
        recipeDetailAdapter.recipeStepData(recipeList,getContext());

        //update widget
        //UpdateBakingService.startBakingService(getContext(),recipeIngredientsForWidgets);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_RECIPES, recipeList);
        currentState.putString("RecipeTitle",recipeName);
    }
}
