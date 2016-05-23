package com.atti.atti_android.join;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.atti.atti_android.data.DataPostThread;
import com.atti.atti_android.gcm.RegistrationIntentService;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by BoWoon on 2016-05-20.
 */
public class AutoLogin {
    private static SharedPreferences ourInstance;

    public static SharedPreferences getInstance() {
        return ourInstance;
    }

    public AutoLogin(Context context) {
        ourInstance = context.getSharedPreferences("login", Activity.MODE_PRIVATE);
    }

    public static void loginDataWrite(String id, String password, String push_id) {
        SharedPreferences.Editor editor = ourInstance.edit();
        editor.putString("id", id);
        editor.putString("password", password);
        editor.putString("push_id", push_id);
        editor.putBoolean("auto_login", true);
        editor.apply();
    }

    public static void loginDataRead() {
        ArrayList<BasicNameValuePair> loginPair = new ArrayList<BasicNameValuePair>();
        loginPair.add(new BasicNameValuePair("login", "login"));
        loginPair.add(new BasicNameValuePair("id", ourInstance.getString("id", "")));
        loginPair.add(new BasicNameValuePair("password", ourInstance.getString("password", "")));
        loginPair.add(new BasicNameValuePair("push_id", ourInstance.getString("push_id", "")));
        new DataPostThread().execute(loginPair);
    }

    public static void logout() {
        SharedPreferences.Editor editor = ourInstance.edit();

        editor.remove("id");
        editor.remove("password");
        editor.remove("push_id");
        editor.remove("auto_login");
        editor.apply();
    }
}
