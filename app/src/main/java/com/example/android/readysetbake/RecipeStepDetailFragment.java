package com.example.android.readysetbake;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
    private RecipeStepClickListener clickListener;
    //SimpleExoPlayer variables
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView exoPlayerView;

    public RecipeStepDetailFragment() {

    }

    public interface RecipeStepClickListener {
        void onRecipeStepDetailItemClick(List<Step> stepsOut, int itemSelectedIndex, String recipeName);
    }

    //Method initializePlayer to implement in onCreateView
    private void initializePlayer(Uri mediaUri) {
        //pendinggggggggg
    }

    //Method isInLandscapeMode to implement in onCreateView
    public boolean isInLandscapeMode (Context context) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recipeList = new ArrayList<>();

        clickListener =(RecipeDetailActivity)getActivity();

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

        //Setting up ExoPlayer here
        exoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.exoPlayerView);
        exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        //Getting video URL
        String videoUrl = recipeSteps.get(selectedIndex).getVideoURL();

        //Inflating view for phone or iPad
        if (rootView.findViewWithTag("sw600dp-port-recipe_step_detail")!=null) {
            recipeName=((RecipeDetailActivity) getActivity()).recipeName;
            ((RecipeDetailActivity) getActivity()).getSupportActionBar().setTitle(recipeName);
        }

        //Getting imageUrl
        String imageUrl=recipeSteps.get(selectedIndex).getThumbnailURL();
        if (imageUrl!="") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            ImageView thumbImage = (ImageView) rootView.findViewById(R.id.thumbImage);
            Picasso.with(getContext()).load(builtUri).into(thumbImage);
        }

        //Using initializePlayer method here
        if (!videoUrl.isEmpty()) {
            initializePlayer(Uri.parse(recipeSteps.get(selectedIndex).getVideoURL()));

            if (rootView.findViewWithTag("sw600dp-land-recipe_step_detail")!=null) {
                getActivity().findViewById(R.id.recipe_fragment_container2).setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
                exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            }
            else if (isInLandscapeMode(getContext())){
                recipeStepDetailTextview.setVisibility(View.GONE);
            }
        }
        else {
            exoPlayer=null;
            //exoPlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.ic_visibility_off_white_36dp));
            exoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        }

        //Setting up previous and next step buttons for recipe steps, using onClickListener
        Button recipePrevStep = (Button) rootView.findViewById(R.id.previousStep);
        Button recipeNextstep = (Button) rootView.findViewById(R.id.nextStep);

        recipePrevStep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (recipeSteps.get(selectedIndex).getId() > 0) {
                    if (exoPlayer!=null){
                        exoPlayer.stop();
                    }
                    clickListener.onRecipeStepDetailItemClick(recipeSteps,recipeSteps.get(selectedIndex).getId() - 1,recipeName);
                }
                else {
                    Toast.makeText(getActivity(),"You already are in the First step of the recipe", Toast.LENGTH_SHORT).show();

                }
            }});

        recipeNextstep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                int lastIndex = recipeSteps.size()-1;
                if (recipeSteps.get(selectedIndex).getId() < recipeSteps.get(lastIndex).getId()) {
                    if (exoPlayer!=null){
                        exoPlayer.stop();
                    }
                    clickListener.onRecipeStepDetailItemClick(recipeSteps,recipeSteps.get(selectedIndex).getId() + 1,recipeName);
                }
                else {
                    Toast.makeText(getContext(),"You already are in the Last step of the recipe", Toast.LENGTH_SHORT).show();

                }
            }});

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_STEPS,recipeSteps);
        currentState.putInt(SELECTED_INDEX,selectedIndex);
        currentState.putString("Title",recipeName);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
