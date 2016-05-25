package com.atti.atti_android.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.atti.atti_android.R;
import com.atti.atti_android.constant.Constant;
import com.atti.atti_android.data.DataPostThread;
import com.atti.atti_android.gcm.RegistrationIntentService;
import com.atti.atti_android.mainactivity.MainActivity;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by BoWoon on 2016-05-16.
 */
public class Login extends Activity {
    private AQuery aq;
    public static boolean loginResult = false;
    private SharedPreferences prefs;
    private String id, password, push_id;

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
        aq.id(R.id.login_main_layout).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        if (RegistrationIntentService.getGCMToken() == null)
            getInstanceIdToken();

        if (prefs.getBoolean("auto_login", false)) {
            ArrayList<BasicNameValuePair> loginPair = AutoLogin.loginDataRead();
//            new DataPostThread(Login.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, loginPair);
            new DataPostThread(Login.this).execute(loginPair);
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }

    public boolean loginChecked() {
        id = aq.id(R.id.login_id_edit).getText().toString();
        password = aq.id(R.id.login_password_edit).getText().toString();

        return !(id.equals("") || password.equals(""));

    }

    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        Constant.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    Button.OnClickListener loginSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_submit:
                    if (loginChecked()) {
//                        id = aq.id(R.id.login_id_edit).getText().toString();
//                        password = aq.id(R.id.login_password_edit).getText().toString();
                        push_id = RegistrationIntentService.getGCMToken();

                        ArrayList<BasicNameValuePair> loginPair = new ArrayList<BasicNameValuePair>();
                        loginPair.add(new BasicNameValuePair("login", "login"));
                        loginPair.add(new BasicNameValuePair("id", id));
                        loginPair.add(new BasicNameValuePair("password", password));
                        loginPair.add(new BasicNameValuePair("push_id", push_id));
                        AutoLogin.loginDataWrite(id, password, push_id);
//                        new DataPostThread(Login.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, loginPair);
                        new DataPostThread(Login.this).execute(loginPair);
//                        if (loginResult) {
//                            startActivity(new Intent(Login.this, MainActivity.class));
//                            finish();
//                        }
//                        else {
//                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
//                        }
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
