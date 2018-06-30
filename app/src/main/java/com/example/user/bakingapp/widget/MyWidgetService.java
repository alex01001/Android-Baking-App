package com.example.user.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.Prefs;

public class MyWidgetService extends RemoteViewsService {

    public static void updateWidget(Context context, Recipe recipe) {
        android.util.Log.i("wqq1", "MyWidgetService.updateWidget");

        Prefs.saveRecipe(context, recipe);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));


        android.util.Log.i("wqq1 component", String.valueOf(new ComponentName(context, RecipeWidgetProvider.class)));

        android.util.Log.i("wqq1 ids", String.valueOf(appWidgetIds.length));
        RecipeWidgetProvider.updateAppWidgets(context, appWidgetManager, appWidgetIds);

       // appWidgetManager.updateAppWidget(new android.content.ComponentName(getPackageName(),RecipeWidgetProvider.class.getName()));
    }

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        return new ListRemoteViews(getApplicationContext());
    }

}
