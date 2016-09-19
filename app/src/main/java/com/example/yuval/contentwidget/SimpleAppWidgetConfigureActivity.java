package com.example.yuval.contentwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * The configuration screen for the {@link SimpleAppWidget SimpleAppWidget} AppWidget.
 */
public class SimpleAppWidgetConfigureActivity extends Activity implements AdapterView.OnItemSelectedListener  {

    private static final String PREFS_NAME = "com.example.yuval.contentwidget.SimpleAppWidget";
    private static final String PREF_TITLE_PREFIX_KEY = "appwidget_title_";
    private static final String PREF_PROVIDER_TOKEN_PREFIX_KEY = "appwidget_provider_token_";
    private static final String PREF_THEME_PREFIX_KEY = "appwidget_theme_token_";
    private static contentProvider[] contentProviders;
    private static View errorToast;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetTitleText;
    EditText mAppWidgetTokenIdText;
    TextView mAppWidgetThemeText;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = SimpleAppWidgetConfigureActivity.this;


            //get provider token
            String widgetText = mAppWidgetTokenIdText.getText().toString();
            //validate provider token
            if(!isValidateProviderToken(widgetText)) {
                showValidationMessage(widgetText);
                return;
            }

            // store the provider token string
            saveProviderTokenPref(context, mAppWidgetId, widgetText);

            // store the title string
            widgetText = mAppWidgetTitleText.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText);

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

    private void createErrorToast() {
        LayoutInflater inflater = getLayoutInflater();
        errorToast = inflater.inflate(R.layout.error_toast,
                (ViewGroup) findViewById(R.id.error_toast_layout));
    }
    private void showValidationMessage(String enteredProviderToken) {
        String message;
        TextView toastText = (TextView) errorToast.findViewById(R.id.error_toast_text);
        int toastErrorColor = ContextCompat.getColor(this, R.color.toastError);

        //set appropriate error message
        if(contentProviders == null || contentProviders.length == 0) {
            message = "Provider list not found. Please wait while provider list is loaded from the server";
        } else {
            if(enteredProviderToken == null || enteredProviderToken.isEmpty()) {
                message = "Please enter provider token.";
            } else {
                message = "The token " + enteredProviderToken + " is not a valid provider token";
            }
            errorToast.setBackgroundColor(toastErrorColor);
        }

        toastText.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(errorToast);
        toast.show();
    }

    static boolean isValidateProviderToken(String widgetText) {
        boolean isValid = false;
        if(contentProviders != null && contentProviders.length>0) {
            for (contentProvider cp : contentProviders) {
                if(widgetText.equals(cp.provider.token)) {
                    isValid = true;
                    break;
                }
            }
        }
        return isValid;
    }

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

        WebApiAsyncTask.AsyncResponse responseMethod = new WebApiAsyncTask.AsyncResponse(){
            @Override
            public void processFinish(String output){
                //getting result fired from async class of onPostExecute(result) method.
                //Parse json
                contentProviders = new Gson().fromJson(output, contentProvider[].class);
//                for (contentProvider cp : contentProviders) {
//                    Log.d("contentProviders: ", "> " + cp.provider.token);
//                }
            }
        };
        new WebApiAsyncTask(responseMethod, "providers", null, null).execute();

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

//        mAppWidgetTokenIdText.setText(loadProviderTokenPref(SimpleAppWidgetConfigureActivity.this, mAppWidgetId));

        createErrorToast();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mAppWidgetThemeText = (TextView) view;
//        String themeString = mAppWidgetThemeText.getText().toString();
//        Toast.makeText(SimpleAppWidgetConfigureActivity.this, themeString, Toast.LENGTH_SHORT).show();
//        if(contentProviders != null && contentProviders.length>0)
//        Toast.makeText(SimpleAppWidgetConfigureActivity.this, contentProviders[0].provider.token, Toast.LENGTH_SHORT).show();
//        Toast.makeText().show();

//        SimpleAppWidgetTheme theme  = new SimpleAppWidgetTheme(themeString);
//        Toast.makeText(SimpleAppWidgetConfigureActivity.this, theme.getTextColor() +':' + theme.getBackgroundColor(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

