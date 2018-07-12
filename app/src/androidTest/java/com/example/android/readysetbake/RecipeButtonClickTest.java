package com.example.android.readysetbake;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.Espresso;
import static android.support.test.espresso.Espresso.onView;
import android.support.test.espresso.contrib.RecyclerViewActions;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;


/**
 * Created by Aiman Nabeel on 11/07/2018.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeButtonClickTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecipeButton_opensRecipeDetailActivity() {
        //1.Find the view
        //2.Perform action on the view
        //3.Check if the view does what is expected

        //Ref: https://spin.atomicobject.com/2016/04/15/espresso-testing-recyclerviews/
        onView(ViewMatchers.withId(R.id.recipe_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        //Ref:https://developer.android.com/training/testing/espresso/intents
        intended(hasComponent(RecipeDetailActivity.class.getName()));
    }
}
