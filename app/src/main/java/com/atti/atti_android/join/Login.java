package com.atti.atti_android.join;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.atti.atti_android.R;
import com.atti.atti_android.data.DataPostThread;
import com.atti.atti_android.mainactivity.MainActivity;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by BoWoon on 2016-05-16.
 */
public class Login extends Activity {
    private AQuery aq;
    private SharedPreferences prefs;
    public static boolean loginResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_layout);
        aq = new AQuery(this);
        prefs = getSharedPreferences("login", Activity.MODE_PRIVATE);

        TypefaceProvider.registerDefaultIconSets();

        aq.id(R.id.login_submit).clicked(loginSubmit);
//        aq.id(R.id.login_reject).clicked(loginSubmit);
        aq.id(R.id.login_join_button).clicked(loginSubmit);

        if (prefs.getBoolean("auto_login", false))
            loginDataRead();
    }

    public boolean loginChecked() {
        String id = aq.id(R.id.login_id_edit).getText().toString();
        String password = aq.id(R.id.login_password_edit).getText().toString();

        return !(id.equals("") || password.equals(""));

    }

    private void loginDataWrite() {
        String id = aq.id(R.id.login_id_edit).getText().toString();
        String password = aq.id(R.id.login_password_edit).getText().toString();

        prefs = getSharedPreferences("login", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id", id);
        editor.putString("password", password);
        editor.putBoolean("auto_login", true);
        editor.apply();
    }

    private void loginDataRead() {
        prefs = getSharedPreferences("login", Activity.MODE_PRIVATE);

        String id = prefs.getString("id", "");
        String password = prefs.getString("password", "");

        ArrayList<BasicNameValuePair> loginPair = new ArrayList<BasicNameValuePair>();
        loginPair.add(new BasicNameValuePair("login", "login"));
        loginPair.add(new BasicNameValuePair("id", id));
        loginPair.add(new BasicNameValuePair("password", password));
        new DataPostThread(getApplicationContext()).execute(loginPair);
        if (loginResult) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }

    Button.OnClickListener loginSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            prefs = getSharedPreferences("login", Activity.MODE_PRIVATE);
            Log.i("auto_login", String.valueOf(prefs.getBoolean("auto_login", false)));
            switch (v.getId()) {
                case R.id.login_submit:
                    if (loginChecked()) {
                        String id = aq.id(R.id.login_id_edit).getText().toString();
                        String password = aq.id(R.id.login_password_edit).getText().toString();

                        ArrayList<BasicNameValuePair> loginPair = new ArrayList<BasicNameValuePair>();
                        loginPair.add(new BasicNameValuePair("login", "login"));
                        loginPair.add(new BasicNameValuePair("id", id));
                        loginPair.add(new BasicNameValuePair("password", password));
                        loginDataWrite();
                        new DataPostThread(getApplicationContext()).execute(loginPair);
                        if (loginResult) {
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "정보를 제대로 입력하세요!", Toast.LENGTH_SHORT).show();
                    }
                    break;
//                case R.id.login_reject:
////                    startActivity(new Intent(Login.this, MainActivity.class));
//                    finish();
//                    break;
                case R.id.login_join_button:
                    startActivity(new Intent(Login.this, Join.class));
                default:
                    break;
            }
        }
    };
}
