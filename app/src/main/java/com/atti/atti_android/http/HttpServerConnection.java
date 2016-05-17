package com.atti.atti_android.http;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by BoWoon on 2016-05-17.
 */
public class HttpServerConnection {
    private static DefaultHttpClient ourInstance = new DefaultHttpClient();

    public static DefaultHttpClient getInstance() {
        return ourInstance;
    }

    private HttpServerConnection() {
    }
}
