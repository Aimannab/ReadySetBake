package com.example.android.readysetbake;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.Matchers.not;
import android.support.test.espresso.IdlingResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aiman Nabeel on 12/07/2018.
 */

//Intent Test
@RunWith(AndroidJUnit4.class)
public class RecipeStepButtonClickTest {

        private IdlingResource mIdlingResource2;



        @Rule
        public IntentsTestRule<RecipeDetailActivity> mIntentTestRule2 = new IntentsTestRule<RecipeDetailActivity>(RecipeDetailActivity.class){

            @Override
            protected Intent getActivityIntent() {
                Parcel in;
                String name = in.writeString("Nutella Pie");
                Double quantity = in.writeDouble(1);
                String measure;
                String ingredient;
                List<Ingredient> ingredient = new ArrayList<>();

                Recipe selectedRecipeItemIndex =  new Recipe();
                Bundle recipeBundleSelected = new Bundle();
                ArrayList<Recipe> recipeSelected = new ArrayList<>();
                recipeSelected.add(selectedRecipeItemIndex);
                recipeBundleSelected.putParcelableArrayList(MainActivity.SELECTED_RECIPES, recipeSelected);

                //Context targetContext = InstrumentationRegistry.getInstrumentation()
                 //       .getTargetContext();
                final Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                intent.putExtras(recipeBundleSelected);

                return intent;
            }
        };

        @Before
        public void registerIdlingResource() {
            mIdlingResource2 = mIntentTestRule2.getActivity().getIdlingResource();
            // To prove that the test fails, omit this call:
            Espresso.registerIdlingResources(mIdlingResource2);
        }

        @Before
        public void stubAllExternalIntents2() {
            // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
            // every test run. In this case all external Intents will be blocked.
            intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
        }

        @Test
        public void clickRecipeButton_opensRecipeStepDetailActivity() {
            //1.Find the view
            //2.Perform action on the view
            //3.Check if the view does what is expected

            //Ref: https://spin.atomicobject.com/2016/04/15/espresso-testing-recyclerviews/
            onView(ViewMatchers.withId(R.id.recipe_detail_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

            //Ref:https://developer.android.com/training/testing/espresso/intents
            intended(hasComponent(RecipeStepDetailFragment.class.getName()));
        }

        @After
        public void unregisterIdlingResource() {
            if (mIdlingResource2 != null) {
                Espresso.unregisterIdlingResources(mIdlingResource2);
            }
        }
    }
