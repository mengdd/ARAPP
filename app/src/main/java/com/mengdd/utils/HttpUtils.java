package com.mengdd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {

    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 10000;

    public static InputStream getInputStream(String path) {
        InputStream inputStream = null;

        try {
            URL url = new URL(path);
            inputStream = getInputStream(url);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return inputStream;
    }

    public static InputStream getInputStream(URL url) {
        return getInputStream(url, READ_TIMEOUT, CONNECT_TIMEOUT);
    }

    public static InputStream getInputStream(URL url, int readTimeOut,
            int connectTimeOut) {

        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {

            if (null != url) {
                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(readTimeOut);
                httpURLConnection.setConnectTimeout(connectTimeOut);

                httpURLConnection.setDoInput(true);

                httpURLConnection.setRequestMethod("GET");

                // 200 stand for successfully connected
                if (200 == httpURLConnection.getResponseCode()) {
                    inputStream = httpURLConnection.getInputStream();
                }

            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

}
