package com.example.android.readysetbake;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Aiman Nabeel on 04/06/2018.
 */

/**************************************************************************************************************
 *    This code has been adapted from the following source:
 *    Title: Udacity-Advanced-Developer-Nanodegree-Baking-App-2017
 *    Author: nikosvaggalis
 *    Date: 2017
 *    Code version: N/A
 *    Availability: https://github.com/nikosvaggalis/Udacity-Advanced-Developer-Nanodegree-Baking-App-2017.git
 **************************************************************************************************************/

/**
 * A very simple implementation of {@link IdlingResource}.
 * <p>
 * Consider using CountingIdlingResource from espresso-contrib package if you use this class from
 * multiple threads or need to keep a count of pending operations.
 */
public class SimpleIdlingResource implements IdlingResource{
    @Nullable
    private volatile ResourceCallback mCallback;

    // Idleness is controlled with this boolean.
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    /**
     * Sets the new idle state, if isIdleNow is true, it pings the {@link ResourceCallback}.
     * @param isIdleNow false if there are pending operations, true if idle.
     */
    public void setIdleState(boolean isIdleNow) {
        mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }
}
