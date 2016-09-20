package com.contentWidget.acw.contentwidget;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * Wrap HTTPS request for webservice API
 */
public class DataProvider {

    // HTTP/HTTPS GET request
    /**
     * Initiate HTTPS request. should match webservice API
     * Currently ignore security certificate (Non exist at the moment)
     * @param targetURL target url according to WCA API
     * @return result string
     */
    protected String sendGet(URL targetURL) throws Exception {

        //=========== SKIP HTTPS CERTIFICATION - FOR DEV ONLY! =========
        // Create a trust manager that does not validate certificate chains
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        };
        TrustManager[] trustAllCerts = new TrustManager[]{tm};

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        //=========== SKIP HTTPS CERTIFICATION - FOR DEV ONLY! END=========
        HttpURLConnection con = (HttpURLConnection) targetURL.openConnection();
        // optional default is GET

        con.setRequestMethod("GET");

        //add request header
        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        //release resources
        con.disconnect();
        in.close();

        //return result
        return response.toString();
    }
}