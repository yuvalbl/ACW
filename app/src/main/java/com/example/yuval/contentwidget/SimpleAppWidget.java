package com.example.yuval.contentwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import org.json.*;
import android.os.AsyncTask;

import com.google.gson.Gson;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link SimpleAppWidgetConfigureActivity SimpleAppWidgetConfigureActivity}
 */
public class SimpleAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        String widgetProviderToken = SimpleAppWidgetConfigureActivity.loadProviderTokenPref(context, appWidgetId);
        String widgetTitle = SimpleAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.simple_app_widget);
        views.setTextViewText(R.id.user_given_title, widgetTitle);
//        views.setTextViewText(R.id.appwidget_provider_token, "Shabadaba");


//        appWidgetManager.updateAppWidget(appWidgetId, views);

//        final AppWidgetManager widgetManager = appWidgetManager;
//        views2 = new RemoteViews(context.getPackageName(), R.layout.simple_app_widget);
        // Calling async task to get json
//        new GetItems().execute();

//        GetItems getItems = new GetItems(new GetItems.AsyncResponse(){

//            @Override
//            void processFinish(String output){
                //Here you will receive the result fired from async class
                //of onPostExecute(result) method.
//            }
//        }).execute();
        //create AsyncResponse for treating the response of GetItems
        GetItems.AsyncResponse responseMethod = new GetItems.AsyncResponse(){
            @Override
            public void processFinish(String output){
            //Here you will receive the result fired from async class
            //of onPostExecute(result) method.
                Log.d("processFinish: ", "> " + output);
                //Parse json
                contentItem[] items = new Gson().fromJson(output, contentItem[].class);

                //get random item index
                Random rand = new Random();
                int  itemIndex = rand.nextInt(items.length);

                views.setTextViewText(R.id.appwidget_provider_token, items[itemIndex].data);
                appWidgetManager.updateAppWidget(appWidgetId, views);

                Log.d("done?: ", "> done");
            }
        };
        new GetItems(responseMethod, widgetProviderToken).execute();

//        views.setTextViewText(R.id.appwidget_provider_token, "Shabadaba");
//        processFinish("asd");
//        GetItems.execute();
    }

//    static public void processFinish(String output){
//        views2.setTextViewText(R.id.appwidget_provider_token, "Shabadaba");
//    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            SimpleAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

