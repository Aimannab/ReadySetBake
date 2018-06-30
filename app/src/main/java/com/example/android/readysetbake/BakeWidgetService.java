package com.example.android.readysetbake;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Aiman Nabeel on 28/06/2018.
 */

public class BakeWidgetService extends IntentService{

    public static final String ACTION_BAKING_INGREDIENTS =
            "com.example.android.readysetbake.action_baking_ingredients";

    public BakeWidgetService(String updateBakeService) {
        super(updateBakeService);
    }

    public static void startBakingIngredientsService(Context context, ArrayList<String> ingredientsList) {
        Intent intent = new Intent(context, BakeWidgetService.class);
        intent.putExtra(ACTION_BAKING_INGREDIENTS, ingredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            ArrayList<String> ingredientsList = intent.getExtras().getStringArrayList(ACTION_BAKING_INGREDIENTS);
                handleActionUpdateBakingIngredientsWidget(ingredientsList);
        }

    }

    private void handleActionUpdateBakingIngredientsWidget(ArrayList<String> ingredientsList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(ACTION_BAKING_INGREDIENTS, ingredientsList);
        sendBroadcast(intent);
    }
}
