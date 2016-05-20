package com.atti.atti_android.join;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.atti.atti_android.R;
import com.atti.atti_android.constant.Constant;
import com.atti.atti_android.data.DataPutThread;
import com.atti.atti_android.gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by BoWoon on 2016-05-16.
 */
public class Join extends Activity {
    private AQuery aq;
    private int gender, kind;
    private String id, password, name, pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.join_layout);
        aq = new AQuery(this);

        gender = kind = -1;

        aq.id(R.id.join_submit).clicked(joinSubmit);
        aq.id(R.id.join_reject).clicked(joinSubmit);
        RadioGroup rgGender = (RadioGroup) findViewById(R.id.radio_group_gender);
        rgGender.setOnCheckedChangeListener(radioListener);
        RadioGroup rgKind = (RadioGroup) findViewById(R.id.radio_group_kind);
        rgKind.setOnCheckedChangeListener(radioListener);

        getInstanceIdToken();
    }

    public boolean joinChecked() {
        id = aq.id(R.id.join_id_edit).getText().toString();
        password = aq.id(R.id.join_password_edit).getText().toString();
        name = aq.id(R.id.join_name_edit).getText().toString();
        pattern = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{7,15}$";

        return !(id.equals("") || password.equals("") || name.equals("") || gender == -1 || kind == -1) && password.matches(pattern);
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

    Button.OnClickListener joinSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.join_submit:
                    if (joinChecked()) {
                        id = aq.id(R.id.join_id_edit).getText().toString();
                        password = aq.id(R.id.join_password_edit).getText().toString();
                        name = aq.id(R.id.join_name_edit).getText().toString();

                        ArrayList<BasicNameValuePair> joinPair = new ArrayList<BasicNameValuePair>();
                        joinPair.add(new BasicNameValuePair("join", "join"));
                        joinPair.add(new BasicNameValuePair("id", id));
                        joinPair.add(new BasicNameValuePair("password", password));
                        joinPair.add(new BasicNameValuePair("name", name));
                        joinPair.add(new BasicNameValuePair("gender", String.valueOf(gender)));
                        joinPair.add(new BasicNameValuePair("kind", String.valueOf(kind)));
                        joinPair.add(new BasicNameValuePair("push_id", RegistrationIntentService.getGCMToken()));
                        Log.i("GCMToken", "" + RegistrationIntentService.getGCMToken());
                        AutoLogin.loginDataWrite(id, password);
                        new DataPutThread().execute(joinPair);
                    } else {
                        Toast.makeText(getApplicationContext(), "정보를 제대로 입력하세요!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.join_reject:
                    break;
                default:
                    break;
            }

//            startActivity(new Intent(Join.this, MainActivity.class));
            finish();
        }
    };

    RadioGroup.OnCheckedChangeListener radioListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (group.getId() == R.id.radio_group_gender) {
                switch (checkedId) {
                    case R.id.radio_button_male:
                        gender = 0;
                        break;
                    case R.id.radio_button_female:
                        gender = 1;
                        break;
                }
            } else if (group.getId() == R.id.radio_group_kind) {
                switch (checkedId) {
                    case R.id.radio_button_elderly:
                        kind = 0;
                        break;
                    case R.id.radio_button_family:
                        kind = 1;
                        break;
                    case R.id.radio_button_socialworker:
                        kind = 2;
                        break;
                }
            }
        }
    };
}
