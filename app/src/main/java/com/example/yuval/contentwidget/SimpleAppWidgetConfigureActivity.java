package com.example.yuval.contentwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * The configuration screen for the {@link SimpleAppWidget SimpleAppWidget} AppWidget.
 */
public class SimpleAppWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.example.yuval.contentwidget.SimpleAppWidget";
    private static final String PREF_TITLE_PREFIX_KEY = "appwidget_title_";
    private static final String PREF_PROVIDER_TOKEN_PREFIX_KEY = "appwidget_provider_token_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetTitleText;
    EditText mAppWidgetTokenIdText;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = SimpleAppWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = mAppWidgetTitleText.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText);
            widgetText = mAppWidgetTokenIdText.getText().toString();
            saveProviderTokenPref(context, mAppWidgetId, widgetText);
            System.out.println("widgetTextwidgetText: " + widgetText);


            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            SimpleAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public SimpleAppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_TITLE_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }
    // Write the prefix to the SharedPreferences object for this widget
    static void saveProviderTokenPref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PROVIDER_TOKEN_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_TITLE_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return "";
        }
    }
    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadProviderTokenPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String providerTokenValue = prefs.getString(PREF_PROVIDER_TOKEN_PREFIX_KEY + appWidgetId, null);
        if (providerTokenValue  != null) {
            return providerTokenValue ;
        } else {
            return context.getString(R.string.appwidget_provider_token);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_TITLE_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.simple_app_widget_configure);
        mAppWidgetTitleText = (EditText) findViewById(R.id.appwidget_title);
        mAppWidgetTokenIdText = (EditText) findViewById(R.id.appwidget_provider_token);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mAppWidgetTokenIdText.setText(loadProviderTokenPref(SimpleAppWidgetConfigureActivity.this, mAppWidgetId));
    }
}

