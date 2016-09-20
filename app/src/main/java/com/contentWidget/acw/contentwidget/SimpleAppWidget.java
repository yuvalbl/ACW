package com.contentWidget.acw.contentwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;
import java.util.Random;
import com.google.gson.Gson;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link SimpleAppWidgetConfigureActivity SimpleAppWidgetConfigureActivity}
 */
public class SimpleAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {
        //get settings data
        String widgetTitle = SimpleAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        String widgetProviderToken = SimpleAppWidgetConfigureActivity
                .loadProviderTokenPref(context, appWidgetId);
        SimpleAppWidgetTheme theme = SimpleAppWidgetConfigureActivity
                .loadThemePref(context, appWidgetId);

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.simple_app_widget);
        views.setTextViewText(R.id.user_given_title, widgetTitle);

        //set text color
        int textColor = ContextCompat.getColor(context, theme.getTextColor());
        views.setTextColor(R.id.user_given_title, textColor);
        views.setTextColor(R.id.widget_content, textColor);
        views.setTextViewText(R.id.widget_content, "Loading...");

        //set background color
        int bgColor = ContextCompat.getColor(context, theme.getBackgroundColor());
        views.setInt(R.id.appwidget_layout, "setBackgroundColor", bgColor);

        //set button color (if required)
        if(theme.getButtonColor() != 0) {
            int buttonColor = ContextCompat.getColor(context, theme.getButtonColor());
            views.setInt(R.id.imageReloadButton, "setColorFilter", buttonColor);
        }

        // Register an onClickListener
        Intent intent = new Intent(context, SimpleAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] {appWidgetId});
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.imageReloadButton, pendingIntent);

        //update style
        appWidgetManager.updateAppWidget(appWidgetId, views);
        //update view
        updateContent(appWidgetManager, views, appWidgetId, widgetProviderToken);
    }

    //extract random item from JSON string
    static String getRandomContentItem(contentItem[] items) {
        String resultItemString = null;


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
        if(widgetProviderToken.isEmpty() || widgetProviderToken.equals(" ") ||
                widgetProviderToken == null) {
            return;
        }

        WebApiAsyncTask.AsyncResponse responseMethod = new WebApiAsyncTask.AsyncResponse(){
            @Override
            public void processFinish(String output){
                //getting result fired from async class of onPostExecute(result) method.
                String content;
                //Parse json
                contentItem[] items = new Gson().fromJson(output, contentItem[].class);

                if(items == null) {
                    content = "Error occurred, Please try again later.";
                } else if(items.length == 0) {
                    content = "The provider has no items for display.";
                } else {
                    content = getRandomContentItem(items);
                }

                //set content and update view
                views.setTextViewText(R.id.widget_content, content);
                appWidgetManager.updateAppWidget(widgetId, views);
            }
        };
        new WebApiAsyncTask(responseMethod, "items","token", widgetProviderToken).execute();
    }

    @Override
    public void onUpdate(Context context, final AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        int appWidgetId = appWidgetIds[0];
        String widgetProviderToken = SimpleAppWidgetConfigureActivity
                .loadProviderTokenPref(context, appWidgetId);

        //set content while fetching items from server
        final RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.simple_app_widget);
        views.setTextViewText(R.id.widget_content, "Loading...");
        appWidgetManager.updateAppWidget(appWidgetId, views);
        //get new content item async
        updateContent(appWidgetManager, views, appWidgetId, widgetProviderToken);
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

