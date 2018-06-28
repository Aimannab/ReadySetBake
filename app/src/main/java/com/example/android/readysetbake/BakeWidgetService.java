package com.example.android.readysetbake;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Aiman Nabeel on 28/06/2018.
 */

public class BakeWidgetService extends IntentService{

    public BakeWidgetService(String updateBakeService) {
        super(updateBakeService);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
