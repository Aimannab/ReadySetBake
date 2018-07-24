package com.example.android.readysetbake;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.readysetbake.ReadySetBakeWidgetProvider.ingredientsList;

/**
 * Created by Aiman Nabeel on 03/07/2018.
 */
public class GridWidgetService extends RemoteViewsService {

    ArrayList<String> recipeIngredientsForWidgets= new ArrayList<>();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context context = null;

        //We have enclosed this class in another class so we can use the following method with our own customized parameters: added Intent intent parameter
        public GridRemoteViewsFactory(Context applicationContext, Intent intent) {
            context = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            //Retrieving Data for RecipeName
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String json = preferences.getString(MainActivity.SHARED_PREFS_KEY, "");
            if (!json.equals("")) {
                Gson gson = new Gson();
                ingredientsList = gson.fromJson(json, new TypeToken<ArrayList<String>>() {
                }.getType());
            }

            SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String json2 = preferences.getString(RecipeDetailFragment.SHARED_PREFS_KEY_INGRED, "");
            if (!json2.equals("")) {
                Gson gson = new Gson();
                recipeIngredientsForWidgets = gson.fromJson(json, new TypeToken<ArrayList<String>>() {
                }.getType());
            }

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return ingredientsList.size();
        }

        //Setting widget_gridview_item here
        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_gridview_item);
            views.setTextViewText(R.id.widget_gridview_item, ingredientsList.get(position));

            //Setting up FillIn Intent here for the gridview - To fill in the PendingIntent Template
            Intent fillInIntent = new Intent();
            views.setOnClickFillInIntent(R.id.widget_gridview_item, fillInIntent);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
