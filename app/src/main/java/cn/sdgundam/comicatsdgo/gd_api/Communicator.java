package cn.sdgundam.comicatsdgo.gd_api;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by xhguo on 9/25/2014.
 */
public class Communicator {
    public static final String LOG_TAG = Communicator.class.getSimpleName();

    public static final String API_URL = "http://www.sdgundam.cn/services/app.ashx";

    public static String requestApi(String action, Map<String, String> parameters) {
        StringBuffer sb = new StringBuffer();

        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(API_URL);
            conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(6000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setUseCaches(true);
            conn.setDoOutput(true);

            if (!parameters.isEmpty()) {
                OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bw.write(getQuery(action, parameters));
                bw.flush();
                bw.close();
            }

            conn.connect();

            int responseCode = conn.getResponseCode();

            int bufferSize = 1024;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                char[] buffer = new char[bufferSize];

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                int read = 0;
                while((read = br.read(buffer, 0, bufferSize)) > 0) {
                    sb.append(buffer, 0, read);
                }

                br.close();
            } else {
                Log.e(LOG_TAG, "Http Response Code: " + responseCode);
            }
        }
        catch(MalformedURLException e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
        }
        catch(IOException e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
        }
        finally {
            conn.disconnect();
        }

        String result = sb.toString();
        Log.v(LOG_TAG, "API Result: " + result);
        return result;
    }


    private static String getQuery(String action, Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        result.append("a=" + action);

        try {
            for (String key : params.keySet()) {
                result.append("&");
                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(params.get(key), "UTF-8"));
            }
        }
        catch (UnsupportedEncodingException e) { }

        return result.toString();
    }
}
