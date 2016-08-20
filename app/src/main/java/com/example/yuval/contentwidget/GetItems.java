package com.example.yuval.contentwidget;

/**
 * Created by Yuval on 26/07/2016.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Async task class to get json by making HTTP call
 * */
public class GetItems extends AsyncTask<String, Void, String> {
    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(String output);
    }
    public String providerToken;
    public AsyncResponse delegate = null;

    public GetItems(AsyncResponse delegate, String providerToken){
        this.delegate = delegate;
        this.providerToken = providerToken;
    }

    @Override
    protected String doInBackground(String... arg0) {
        // Creating service handler class instance
//            ServiceHandler sh = new ServiceHandler();
        String jsonStr = null;
        // Making a request to url and getting response
        try {
//            URL dataUrl = new URL("http://frontendfrontier.net/json/items.json");
            URL dataUrl = new URL("https://acw-server.ddns.net/items?provider" + this.providerToken);
            DataProvider dp = new DataProvider();
            jsonStr = dp.sendGet(dataUrl);
            System.out.println("jsonStr: " + jsonStr);
//            Log.d("Response: ", "> " + jsonStr);
//            delegate.processFinish(jsonStr);
        }
        catch (Exception e) {
            e.printStackTrace();
        }



        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Get Items", "Couldn't get any data from the url");
        }

        return jsonStr;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
