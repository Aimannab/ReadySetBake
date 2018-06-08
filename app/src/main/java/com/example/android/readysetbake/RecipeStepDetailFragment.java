package com.example.android.readysetbake;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.android.readysetbake.MainActivity.SELECTED_INDEX;
import static com.example.android.readysetbake.MainActivity.SELECTED_RECIPES;
import static com.example.android.readysetbake.MainActivity.SELECTED_STEPS;

/**
 * Created by Aiman Nabeel on 31/05/2018.
 */

public class RecipeStepDetailFragment extends Fragment {
    ArrayList<Recipe> recipeList;
    TextView recipeStepDetailTextview;
    private ArrayList<Step> recipeSteps = new ArrayList<>();
    private int selectedIndex;
    String recipeName;

    public RecipeStepDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        recipeList = new ArrayList<>();

        if(savedInstanceState != null) {
            recipeSteps = savedInstanceState.getParcelableArrayList(SELECTED_STEPS);
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX);
            recipeName = savedInstanceState.getString("Title");


        }
        else {
            recipeSteps =getArguments().getParcelableArrayList(SELECTED_STEPS);
            if (recipeSteps!=null) {
                recipeSteps =getArguments().getParcelableArrayList(SELECTED_STEPS);
                selectedIndex=getArguments().getInt(SELECTED_INDEX);
                recipeName=getArguments().getString("Title");
            }
            else {
                recipeSteps =getArguments().getParcelableArrayList(SELECTED_RECIPES);
                //casting List to ArrayList
                recipeSteps=(ArrayList<Step>)recipeList.get(0).getSteps();
                selectedIndex=0;
            }

        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        recipeStepDetailTextview = (TextView) rootView.findViewById(R.id.recipe_step_detail_content);
        recipeStepDetailTextview.setText(recipeSteps.get(selectedIndex).getDescription());
        recipeStepDetailTextview.setVisibility(View.VISIBLE);

        if (rootView.findViewWithTag("sw600dp-port-recipe_step_detail")!=null) {
            recipeName=((RecipeDetailActivity) getActivity()).recipeName;
            ((RecipeDetailActivity) getActivity()).getSupportActionBar().setTitle(recipeName);
        }

        String imageUrl=recipeSteps.get(selectedIndex).getThumbnailURL();
        if (imageUrl!="") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            ImageView thumbImage = (ImageView) rootView.findViewById(R.id.thumbImage);
            Picasso.with(getContext()).load(builtUri).into(thumbImage);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_STEPS,recipeSteps);
        currentState.putInt(SELECTED_INDEX,selectedIndex);
        currentState.putString("Title",recipeName);
    }
}
