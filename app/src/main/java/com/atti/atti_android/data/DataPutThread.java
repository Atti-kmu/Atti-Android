package com.atti.atti_android.data;

import android.os.AsyncTask;
import android.util.Log;

import com.atti.atti_android.http.HttpServerConnection;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by BoWoon on 2016-05-17.
 */
public class DataPutThread extends AsyncTask<ArrayList<BasicNameValuePair>, Integer, Void> {
    @Override
    protected Void doInBackground(ArrayList<BasicNameValuePair>... params) {
//        HttpClient httpClient = ConnectSSLClient.getHttpClient();
        DefaultHttpClient httpClient = HttpServerConnection.getInstance();
        String responseString = null;
        String urlString = "http://52.79.147.144/mobile/user";

        try {
            HttpPut httpPut = new HttpPut(urlString);

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            Log.i("params length", String.valueOf(params[0].size()));
            for (int i = 1; i < params[0].size(); i++) {
                Log.i("DataPostThread getName", params[0].get(i).getName());
                Log.i("DataPostThread getValue", params[0].get(i).getValue());
                nameValuePairs.add(new BasicNameValuePair(params[0].get(i).getName(), params[0].get(i).getValue()));
            }

            httpPut.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(httpPut);
            responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

            Log.i("DataPostThread", String.valueOf(params[0]));
            Log.i("DataPostThread String", responseString);
        } catch (ClientProtocolException e) {
            Log.e("ClientProtocolException", e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("IOException", e.getLocalizedMessage());
            e.printStackTrace();
        }

        return null;
    }

    public DataPutThread() {
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
