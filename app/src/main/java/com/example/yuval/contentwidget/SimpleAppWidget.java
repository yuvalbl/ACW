package com.example.yuval.contentwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

        updateContent(appWidgetManager, views, appWidgetId, widgetProviderToken);
    }

    //extract random item from JSON string
    static String getRandomContentItem(String jsonString) {
        String resultItemString = null;
        //Parse json
        contentItem[] items = new Gson().fromJson(jsonString, contentItem[].class);

        //get random item index
        Random rand = new Random();

        if(items != null  && items.length > 0) {
            int  itemIndex = rand.nextInt(items.length);
            resultItemString = items[itemIndex].data;
        }

        return resultItemString;
    }

    //update content with new random item
    static void updateContent (final AppWidgetManager appWidgetManager,
                                      final RemoteViews views, final int widgetId,
                                      String widgetProviderToken) {
        GetItems.AsyncResponse responseMethod = new GetItems.AsyncResponse(){
            @Override
            public void processFinish(String output){
                //getting result fired from async class of onPostExecute(result) method.
                Log.d("processFinish: ", "> " + output);
                String item = getRandomContentItem(output);

                if(item != null) {
                    views.setTextViewText(R.id.appwidget_provider_token, item);
                } else {
                    views.setTextViewText(R.id.appwidget_provider_token, "Error occurred, Please try again later.");

                }
                appWidgetManager.updateAppWidget(widgetId, views);
            }
        };
        new GetItems(responseMethod, widgetProviderToken).execute();
    }

    @Override
    public void onUpdate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        String widgetProviderToken;
        // There may be multiple widgets active, so update all of them
        ComponentName thisWidget = new ComponentName(context, SimpleAppWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

//        for (int appWidgetId : appWidgetIds) {
        for (final int widgetId : allWidgetIds) {

            widgetProviderToken = SimpleAppWidgetConfigureActivity.loadProviderTokenPref(context, widgetId);

            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.simple_app_widget);
//            Log.d("getRandomItem: ", "> onUpdate , RandomItem ");
            updateContent(appWidgetManager, views, widgetId, widgetProviderToken);




            // Register an onClickListener
            Intent intent = new Intent(context, SimpleAppWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.imageReloadButton, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, views);



//            Intent configIntent = new Intent(context, SimpleAppWidget.class);
//            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
//            views.setOnClickPendingIntent(R.id.imageReloadButton, configPendingIntent);
//            appWidgetManager.updateAppWidget(appWidgetIds, views);




//            Intent serviceIntent = new Intent(context, mysercice.class);
//            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
//            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
//            PendingIntent pendingServiceIntent = PendingIntent.getService(context, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//            views.setOnClickPendingIntent(R.id.refresh, pendingServiceIntent);
//
//            context.startService(serviceIntent);
//            appWidgetManager.updateAppWidget(appWidgetId, views);

//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
//            Intent intent = new Intent();
            ///////////////////////////////////////////////////////////
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.simple_app_widget);
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, 0);
//            views.setOnClickPendingIntent(R.id.imageReloadButton, pendingIntent);
//            updateAppWidget(context, appWidgetManager, appWidgetId);

///////////////////////////////////////////////////////////
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.simple_app_widget);
//            Intent configIntent = new Intent(context, AppCompatPreferenceActivity.class);
//            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
//            remoteViews.setOnClickPendingIntent(R.id.simple_widget, configPendingIntent);
//            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
///////////////////////////////////////////////////////////

/*            Intent serviceIntent = new Intent(context, loadItemService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingServiceIntent = PendingIntent.getService(context, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.simple_app_widget);
            views.setOnClickPendingIntent(R.id.imageReloadButton, pendingServiceIntent);
            updateAppWidget(context, appWidgetManager, appWidgetId);*/
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

