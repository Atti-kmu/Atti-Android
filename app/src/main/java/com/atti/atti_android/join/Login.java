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
    public static boolean loginResult = false;
    private SharedPreferences prefs;
    private String id;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_layout);
        new AutoLogin(getApplicationContext());

        aq = new AQuery(this);
        prefs = AutoLogin.getInstance();

        TypefaceProvider.registerDefaultIconSets();

        aq.id(R.id.login_submit).clicked(loginSubmit);
        aq.id(R.id.login_join_button).clicked(loginSubmit);

        if (prefs.getBoolean("auto_login", false))
            AutoLogin.loginDataRead(id, password);
    }

    public boolean loginChecked() {
        id = aq.id(R.id.login_id_edit).getText().toString();
        password = aq.id(R.id.login_password_edit).getText().toString();

        return !(id.equals("") || password.equals(""));

    }

    Button.OnClickListener loginSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_submit:
                    if (loginChecked()) {
                        id = aq.id(R.id.login_id_edit).getText().toString();
                        password = aq.id(R.id.login_password_edit).getText().toString();

                        ArrayList<BasicNameValuePair> loginPair = new ArrayList<BasicNameValuePair>();
                        loginPair.add(new BasicNameValuePair("login", "login"));
                        loginPair.add(new BasicNameValuePair("id", id));
                        loginPair.add(new BasicNameValuePair("password", password));
                        AutoLogin.loginDataWrite(id, password);
                        new DataPostThread().execute(loginPair);
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
