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

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;


import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.readysetbake.MainActivity.SELECTED_INDEX;
import static com.example.android.readysetbake.MainActivity.SELECTED_RECIPES;
import static com.example.android.readysetbake.MainActivity.SELECTED_STEPS;

/**
 * Created by Aiman Nabeel on 31/05/2018.
 */

//Ref: https://www.i-programmer.info/professional-programmer/accreditation/10908-insiders-guide-to-udacity-android-developer-nanodegree-part-3-the-making-of-baking-app.html

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
    private BandwidthMeter bandwidthMeter;
    private Handler mainHandler;

    public RecipeStepDetailFragment() {

    }

    public interface RecipeStepClickListener {
        void onRecipeStepDetailItemClick(List<Step> allSteps, int Index, String recipeName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recipeList = new ArrayList<>();

        mainHandler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();

        clickListener =(RecipeDetailActivity)getActivity();

        //Correction test
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
            exoPlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.ic_visibility_off_white_36dp_foregroundmethod));
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

    //Method initializePlayer to implement in onCreateView
    private void initializePlayer(Uri mediaUri) {

        if(exoPlayer == null) {
            TrackSelection.Factory videoTrackSelector = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelector);
            LoadControl loadControl = new DefaultLoadControl();

            //USING newSimpleInstance method here, method explanation as follows:
            //public static SimpleExoPlayer newSimpleInstance(Context context, TrackSelector<?> trackSelector,
            //        LoadControl loadControl) {
            //    return newSimpleInstance(context, trackSelector, loadControl, null);
            //}

            /**
             * Creates a {@link SimpleExoPlayer} instance. Must be called from a thread that has an associated
             * {@link Looper}.
             *
             * @param context A {@link Context}.
             * @param trackSelector The {@link TrackSelector} that will be used by the instance.
             * @param loadControl The {@link LoadControl} that will be used by the instance.
             * @param drmSessionManager An optional {@link DrmSessionManager}. May be null if the instance
             *     will not be used for DRM protected playbacks.
             */
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);

            exoPlayerView.setPlayer(exoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "ReadySetBake!");

            //USING ExtractorMediaSource method here, method explanation as follows:
            //public ExtractorMediaSource(Uri uri, DataSource.Factory dataSourceFactory,
            //        ExtractorsFactory extractorsFactory, Handler eventHandler, EventListener eventListener)
            /**
             * @param uri The {@link Uri} of the media stream.
             * @param dataSourceFactory A factory for {@link DataSource}s to read the media.
             * @param extractorsFactory A factory for {@link Extractor}s to process the media stream. If the
             *     possible formats are known, pass a factory that instantiates extractors for those formats.
             *     Otherwise, pass a {@link DefaultExtractorsFactory} to use default extractors.
             * @param minLoadableRetryCount The minimum number of times to retry if a loading error occurs.
             * @param eventHandler A handler for events. May be null if delivery of events is not required.
             * @param eventListener A listener of events. May be null if delivery of events is not required.
             */
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);

            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    //Method isInLandscapeMode to implement in onCreateView
    public boolean isInLandscapeMode (Context context) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    //Called when the fragment is no longer attached to its activity. This is called after onDestroy(),
    // except in the cases where the fragment instance is retained across Activity re-creation
    @Override
    public void onDetach() {
        super.onDetach();
        if (exoPlayer!=null) {
            exoPlayer.stop();
            exoPlayer.release();
        }

    }

    //Called when the view previously created by onCreateView(LayoutInflater, ViewGroup, Bundle) has been
    //detached from the fragment. The next time the fragment needs to be displayed, a new view will be created.
    // This is called after onStop() and before onDestroy().
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (exoPlayer!=null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer=null;
        }
    }

    //Called when the Fragment is no longer started. This is generally tied to Activity.onStop of the containing Activity's lifecycle.
    @Override
    public void onStop() {
        super.onStop();
        if (exoPlayer!=null) {
            exoPlayer.stop();
            exoPlayer.release();
        }
    }

    //Called when the Fragment is no longer resumed. This is generally tied to Activity.onPause of the containing Activity's lifecycle.
    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer!=null) {
            exoPlayer.stop();
            exoPlayer.release();
        }
    }
}
