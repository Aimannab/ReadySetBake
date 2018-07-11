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

//Using this class in RecipeDetailFragment class
public class BakeWidgetService extends IntentService{

    public static String ACTION_BAKING_INGREDIENTS =
            "ACTION_BAKING_INGREDIENTS";

    public BakeWidgetService() {
        super("UpdateBakingService");
    }

    public static void startBakingIngredientsService(Context context, ArrayList<String> ingredientsList) {
        Intent intent = new Intent(context, BakeWidgetService.class);
        intent.putExtra(ACTION_BAKING_INGREDIENTS, ingredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<String> ingredientsList = intent.getExtras().getStringArrayList(ACTION_BAKING_INGREDIENTS);
                handleActionUpdateBakingIngredientsWidget(ingredientsList);
        }

    }

    //Ref: https://developer.android.com/reference/android/appwidget/AppWidgetManager.html#ACTION_APPWIDGET_UPDATE
    //Sent when it is time to update your AppWidget.
    //This may be sent in response to a new instance for this AppWidget provider having been instantiated, the requested update interval having lapsed, or the system booting.
    private void handleActionUpdateBakingIngredientsWidget(ArrayList<String> ingredientsList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(ACTION_BAKING_INGREDIENTS, ingredientsList);
        sendBroadcast(intent);
    }
}
