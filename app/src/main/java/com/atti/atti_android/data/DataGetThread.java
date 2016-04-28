package com.atti.atti_android.data;

import android.os.AsyncTask;
import android.util.Log;

import com.atti.atti_android.person.Family;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by BoWoon on 2016-04-28.
 */
public class DataGetThread extends AsyncTask<Void, Integer, String> {
    @Override
    protected String doInBackground(Void... params) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String responseString = null;

        String urlString = "http://52.79.147.144/atti/family";
        try {
            HttpGet httpGet = new HttpGet(urlString);

            HttpResponse response = httpClient.execute(httpGet);
            responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

            Log.d("TAG", responseString);
        } catch (ClientProtocolException e) {
            Log.e("TAG", e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("TAG", e.getLocalizedMessage());
            e.printStackTrace();
        }

        return responseString;
    }

    public DataGetThread() {
        super();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject object = new JSONObject(s);
            JSONArray jarray = new JSONArray(object.getString("results"));

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);
                Log.i("data", "" + jObject);

                String name = jObject.getString("name");
                String nickname = jObject.getString("nickname");
                String img = jObject.getString("profile_url");

                Log.i("name", name);
                Log.i("nickname", nickname);
                Log.i("img", img);

                UsersDataManager.getUsersInstance().addData(new Family(name, img, nickname));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
