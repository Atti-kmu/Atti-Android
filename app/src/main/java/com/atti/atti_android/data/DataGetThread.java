package com.atti.atti_android.data;

import android.os.AsyncTask;
import android.util.Log;

import com.atti.atti_android.http.HttpServerConnection;
import com.atti.atti_android.person.ElderlyPerson;
import com.atti.atti_android.person.Family;
import com.atti.atti_android.person.SocialWorker;

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
public class DataGetThread extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {
        DefaultHttpClient httpClient = HttpServerConnection.getInstance();
        String responseString = null;
        String urlString = "http://52.79.147.144/mobile/family/1";
        String imgUrl = "http://52.79.147.144/images/profile/";

        if (params[0].equals("family"))
            urlString = "http://52.79.147.144/mobile/family/1";
        else if (params[0].equals("friends"))
            urlString = "http://52.79.147.144/mobile/friends/1";
        else if (params[0].equals("friendship"))
            urlString = "http://52.79.147.144/mobile/friendship/1";
        else if (params[0].equals("mypage"))
            urlString = "http://52.79.147.144/mobile/user/mypage";
        else if (params[0].equals("login"))
            urlString = "http://52.79.147.144/mobile/user";

        try {
            HttpGet httpGet = new HttpGet(urlString);

            HttpResponse response = httpClient.execute(httpGet);
            responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

            Log.d("DataGetThread", responseString);

            JSONObject object = new JSONObject(responseString);
            JSONArray jarray = new JSONArray();

            if (params[0].equals("family"))
                jarray = new JSONArray(object.getString("family_list"));
            else if (params[0].equals("friends"))
                jarray = new JSONArray(object.getString("friend_list"));
            else if (params[0].equals("friendship"))
                jarray = new JSONArray(object.getString("friendship_list"));
            else if (params[0].equals("mypage"))
                jarray = new JSONArray(object.getString("mypage"));
            else if (params[0].equals("login"))
                return "logout";

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);

                String name = jObject.getString("name");
                String img = jObject.getString("profile_name");

                if (params[0].equals("family"))
                    UsersDataManager.getUsersInstance().addData(new Family(name, imgUrl + img));
                else if (params[0].equals("friends"))
                    UsersDataManager.getUsersInstance().addData(new ElderlyPerson(name, imgUrl + img));
                else if (params[0].equals("friendship"))
                    UsersDataManager.getUsersInstance().addData(new SocialWorker(name, imgUrl + img));
            }
        } catch (ClientProtocolException e) {
            Log.e("ClientProtocolException", e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("IOException", e.getLocalizedMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("JSONException", e.getLocalizedMessage());
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
