package com.contentWidget.acw.contentwidget;

/**
 * Created by Yuval on 26/07/2016.
 */

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

/**
 * Async task class to get json by making HTTP call
 * */
public class WebApiAsyncTask extends AsyncTask<String, Void, String> {
    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(String output);
    }
    public String requestQueryString;
    public String requestParamKey;
    public String requestParamValue;
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
        // Creating service handler class instance
//            ServiceHandler sh = new ServiceHandler();
        String jsonStr = null;
        // Making a request to url and getting response
        try {
//            URL dataUrl = new URL("http://frontendfrontier.net/json/items.json");
            URL dataUrl = buildRequestDataURL();
            if(dataUrl != null) {
                DataProvider dp = new DataProvider();
                jsonStr = dp.sendGet(dataUrl);
            }
//            System.out.println("jsonStr: " + jsonStr);
        }
        catch (Exception e) {
            Log.e("doInBackground", "Error using DataProvider>sendGet");
            e.printStackTrace();
        }
//        if (jsonStr != null) {
//            try {
//                JSONObject jsonObj = new JSONObject(jsonStr);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Log.e("Get Items", "Couldn't get any data from the url");
//        }

        return jsonStr;
    }

    //create request URL. example for valid string:
    // "https://acw-server.ddns.net/items?provider=TOKEN"
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
