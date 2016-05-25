package com.atti.atti_android.data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.atti.atti_android.R;
import com.atti.atti_android.constant.Constant;
import com.atti.atti_android.gcm.RegistrationIntentService;
import com.atti.atti_android.http.HttpServerConnection;
import com.atti.atti_android.mainactivity.MainActivity;
import com.atti.atti_android.registration.AutoLogin;
import com.atti.atti_android.registration.Login;

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
    private ProgressDialog progress;
    private String id, password, push_id;
    private AQuery aq;

    @Override
    protected Integer doInBackground(ArrayList<BasicNameValuePair>... params) {
//        HttpClient httpClient = ConnectSSLClient.getHttpClient();
        DefaultHttpClient httpClient = HttpServerConnection.getInstance();
        String responseString = null;
        String urlString = "http://52.79.147.144/mobile/user";

        Log.i("PostParams", String.valueOf(params[0]));
        if (params[0].get(0).getName().equals("login"))
            urlString = "http://52.79.147.144/mobile/user";
        else if (params[0].get(0).getName().equals("channel"))
            urlString = "http://52.79.147.144/mobile/call";

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
        aq = new AQuery(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setTitle("처리 중...");
        progress.setMessage("잠시만 기다려주세요...");
        progress.setCancelable(false);
        progress.setProgress(0);
        progress.setButton(DialogInterface.BUTTON_NEGATIVE, "취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancel(true);
            }
        });
        progress.show();
    }

    @Override
    protected void onPostExecute(Integer s) {
        super.onPostExecute(s);

        EditText idEdit = (EditText) ((Activity) context).findViewById(R.id.login_id_edit);
        EditText passwordEdit = (EditText) ((Activity) context).findViewById(R.id.login_password_edit);

        id = idEdit.getText().toString();
        password = passwordEdit.getText().toString();

        Log.i("PostID", id);
        Log.i("PostPassword", password);

        if (s == Constant.LOGIN_FAILED || s == Constant.LOGIN_ERROR) {
            Toast.makeText(context, "정보를 제대로 입력하세요!", Toast.LENGTH_SHORT).show();
            return;
        } else if (s == Constant.LOGIN_SUCCESS) {
            push_id = RegistrationIntentService.getGCMToken();
            AutoLogin.loginDataWrite(id, password, push_id);
            context.startActivity(new Intent(context, MainActivity.class));
            ((Activity) context).finish();
        }

        progress.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progress.setProgress(values[0]);
    }

    @Override
    protected void onCancelled(Integer s) {
        super.onCancelled(s);
        progress.dismiss();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progress.dismiss();
    }
}
