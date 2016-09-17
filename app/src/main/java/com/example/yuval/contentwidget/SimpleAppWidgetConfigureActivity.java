package com.example.yuval.contentwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The configuration screen for the {@link SimpleAppWidget SimpleAppWidget} AppWidget.
 */
public class SimpleAppWidgetConfigureActivity extends Activity implements AdapterView.OnItemSelectedListener  {

    private static final String PREFS_NAME = "com.example.yuval.contentwidget.SimpleAppWidget";
    private static final String PREF_TITLE_PREFIX_KEY = "appwidget_title_";
    private static final String PREF_PROVIDER_TOKEN_PREFIX_KEY = "appwidget_provider_token_";
    private static final String PREF_THEME_PREFIX_KEY = "appwidget_theme_token_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetTitleText;
    EditText mAppWidgetTokenIdText;
    TextView mAppWidgetThemeText;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = SimpleAppWidgetConfigureActivity.this;

            // store the title string
            String widgetText = mAppWidgetTitleText.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText);
            // store the provider token string
            widgetText = mAppWidgetTokenIdText.getText().toString();
            saveProviderTokenPref(context, mAppWidgetId, widgetText);
            // store the theme string
            widgetText = mAppWidgetThemeText.getText().toString();
            saveThemePref(context, mAppWidgetId, widgetText);

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
    // Write the prefix to the SharedPreferences object for this widget
    static void saveThemePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_THEME_PREFIX_KEY + appWidgetId, text);
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
    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static SimpleAppWidgetTheme loadThemePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String themeString = prefs.getString(PREF_THEME_PREFIX_KEY + appWidgetId, null);
        return new SimpleAppWidgetTheme(themeString);
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


        //Set theme spinner
        Spinner themeSpinner = (Spinner) findViewById(R.id.theme_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> themesAdapter = ArrayAdapter.createFromResource(this, R.array.pref_themes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        themesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        themeSpinner.setAdapter(themesAdapter);
        //set selected event listener
        themeSpinner.setOnItemSelectedListener(this);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mAppWidgetThemeText = (TextView) view;
        String themeString = mAppWidgetThemeText.getText().toString();
        Toast.makeText(SimpleAppWidgetConfigureActivity.this, themeString, Toast.LENGTH_SHORT).show();

//        SimpleAppWidgetTheme theme  = new SimpleAppWidgetTheme(themeString);
//        Toast.makeText(SimpleAppWidgetConfigureActivity.this, theme.getTextColor() +':' + theme.getBackgroundColor(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

