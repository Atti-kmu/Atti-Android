package com.atti.atti_android.data;

import android.os.AsyncTask;
import android.util.Log;

import com.atti.atti_android.ssl.ConnectSSLClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by BoWoon on 2016-05-11.
 */
public class DataPostThread extends AsyncTask<String, Integer, Void> {
    @Override
    protected Void doInBackground(String... params) {
        HttpClient httpClient = ConnectSSLClient.getHttpClient();
        String responseString = null;

        String urlString = "http://52.79.147.144/atti/family";
        try {
            HttpPost httpPost = new HttpPost(urlString);

            ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("Channel", params[0]));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(httpPost);
//            responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

            Log.i("DataPostThread", String.valueOf(params[0]));
        } catch (ClientProtocolException e) {
            Log.e("ClientProtocolException", e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("IOException", e.getLocalizedMessage());
            e.printStackTrace();
        }

        return null;
    }

    public DataPostThread() {
        super();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Void s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}

