package com.atti.atti_android.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.atti.atti_android.constant.Constant;
import com.atti.atti_android.http.HttpServerConnection;
import com.atti.atti_android.join.Login;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by BoWoon on 2016-05-11.
 */
public class DataPostThread extends AsyncTask<ArrayList<BasicNameValuePair>, Integer, Integer> {
    private Context context;

    @Override
    protected Integer doInBackground(ArrayList<BasicNameValuePair>... params) {
//        HttpClient httpClient = ConnectSSLClient.getHttpClient();
        DefaultHttpClient httpClient = HttpServerConnection.getInstance();
        String responseString = null;
        String urlString = "http://52.79.147.144/mobile/user";

        Log.i("PostParams", String.valueOf(params[0]));
        if (params[0].get(0).getName().equals("login")) {
            Log.i("Login Pair", String.valueOf(params[0]));
            urlString = "http://52.79.147.144/mobile/user";
        }
        else if (params[0].get(0).getName().equals("channel")) {
            Log.i("channel Post", String.valueOf(params[0]));
            urlString = "http://52.79.147.144/mobile/call";
        }

        try {
            HttpPost httpPost = new HttpPost(urlString);

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            Log.i("params length", String.valueOf(params[0].size()));
            for (int i = 1; i < params[0].size(); i++) {
                Log.i("DataPostThread getName", params[0].get(i).getName());
                Log.i("DataPostThread getValue", "" + params[0].get(i).getValue());
                nameValuePairs.add(new BasicNameValuePair(params[0].get(i).getName(), params[0].get(i).getValue()));
            }

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

            HttpResponse response = httpClient.execute(httpPost);
            responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

            Log.i("responseString", responseString);
            JSONObject object = new JSONObject(responseString);
            String statusCode = object.getString("status");

            if (Integer.valueOf(statusCode) == Constant.LOGIN_FAILED || Integer.valueOf(statusCode) == Constant.LOGIN_ERROR) {
                Log.i("Login_Failed", String.valueOf(Constant.LOGIN_FAILED));
                return Constant.LOGIN_FAILED;
            }

            Log.i("DataPostThread", String.valueOf(params[0]));
            Log.i("DataPostThread String", responseString);
        } catch (ClientProtocolException e) {
            Log.e("ClientProtocolException", e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("IOException", e.getLocalizedMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Constant.LOGIN_SUCCESS;
    }

    public DataPostThread(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Integer s) {
        super.onPostExecute(s);

        if (s == Constant.LOGIN_FAILED || s == Constant.LOGIN_ERROR)
            Login.loginResult = false;
        else if (s == Constant.LOGIN_SUCCESS)
            Login.loginResult = true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Integer s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
