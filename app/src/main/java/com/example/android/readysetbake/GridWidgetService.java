package com.example.android.readysetbake;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import static com.example.android.readysetbake.ReadySetBakeWidgetProvider.ingredientsList;

/**
 * Created by Aiman Nabeel on 03/07/2018.
 */
public class GridWidgetService extends RemoteViewsService {

    List<String> remoteIngredientsList;

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
            remoteIngredientsList = ingredientsList;

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return remoteIngredientsList.size();
        }

        //Setting widget_gridview_item here
        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_gridview_item);
            views.setTextViewText(R.id.widget_gridview_item, remoteIngredientsList.get(position));

            //Setting up FillIn Intent here
            Intent intent = new Intent();
            views.setOnClickFillInIntent(R.id.widget_gridview, intent);
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
            return false;
        }
    }

}
