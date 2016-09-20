package com.contentWidget.acw.contentwidget;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URL;

/**
 * Async task class to get json by making HTTP/S call
 * */
public class WebApiAsyncTask extends AsyncTask<String, Void, String> {
    // you may separate this or combined to caller class.
    // AsyncResponse is used for doing job after async action finished.
    public interface AsyncResponse {
        void processFinish(String output);
    }
    // example for a valid URL: "https://acw-server.ddns.net/items?token=TKN2"
    public String requestQueryString;       //API main source ("items", "providers" etc.)
    public String requestParamKey;          //API parameter key (e.g. "token")
    public String requestParamValue;        //API parameter value (e.g. "TKN2")
    public AsyncResponse delegate = null;

    public WebApiAsyncTask(AsyncResponse delegate, String requestQueryString, String requestParamKey,
                    String requestParamValue){
        this.delegate = delegate;
        this.requestQueryString = requestQueryString;
        this.requestParamKey = requestParamKey;
        this.requestParamValue = requestParamValue;
    }

    @Override
    protected String doInBackground(String... arg0) {
        String jsonStr = null;
        // Making a request to url and getting response
        try {
            URL dataUrl = buildRequestDataURL();
            if(dataUrl != null) {
                DataProvider dp = new DataProvider();
                jsonStr = dp.sendGet(dataUrl);
            }
        }
        catch (Exception e) {
            Log.e("doInBackground", "Error using DataProvider>sendGet");
            e.printStackTrace();
        }

        //check JSON result
        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Get Items", "Couldn't get any data from the url");
        }

        return jsonStr;
    }

    // create request URL by parameters passed to WebApiAsyncTask constructor.
    // example for valid string: "https://acw-server.ddns.net/items?token=TKN2"
    private URL buildRequestDataURL() {
        boolean itemRequest = this.requestQueryString.equals("items");
        boolean validRequestParam = this.requestParamKey != null && !this.requestParamKey.isEmpty();
        validRequestParam = validRequestParam && this.requestParamValue != null &&
                !this.requestParamValue.isEmpty();

        String requestString;
        URL dataUrl = null;

        //if valid parameter provided OR request is not "items" request
        if(validRequestParam || !itemRequest ) {
            requestString = "https://acw-server.ddns.net/" + this.requestQueryString;
            if(validRequestParam) {
                requestString += "?" + this.requestParamKey + "=" + this.requestParamValue;
            }
            try {
                dataUrl = new URL(requestString);
            } catch (Exception e) {
                Log.e("buildRequestDataURL", "Error while building data Url");
                e.printStackTrace();
            }
        }
        return dataUrl;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
