package com.atti.atti_android.mainactivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atti.atti_android.R;
import com.atti.atti_android.data.HttpConnect;
import com.atti.atti_android.data.UsersDataManager;
import com.atti.atti_android.gcm.QuickstartPreferences;
import com.atti.atti_android.gcm.RegistrationIntentService;
import com.atti.atti_android.list.ElderlyList;
import com.atti.atti_android.list.FamilyList;
import com.atti.atti_android.list.SocialWorkerList;
import com.atti.atti_android.person.ElderlyPerson;
import com.atti.atti_android.person.Family;
import com.atti.atti_android.person.SocialWorker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends Activity {
    private UsersDataManager users;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private Button mRegistrationButton;
    private ProgressBar mRegistrationProgressBar;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView mInformationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpConnect http = new HttpConnect();
        http.getData();

        init();
    }

    public void init() {
        findViewById(R.id.btn_family).setOnClickListener(listener);
        findViewById(R.id.btn_friend).setOnClickListener(listener);
        findViewById(R.id.btn_social_worker).setOnClickListener(listener);

        users = UsersDataManager.getUsersInstance();

//        if (users.getElderly().size() == 0) {
//            users.addData(new ElderlyPerson("김씨", "010-1234-5678", "김씨"));
//            users.addData(new ElderlyPerson("이씨", "010-1234-5678", "이씨"));
//            users.addData(new ElderlyPerson("박씨", "010-1234-5678", "박씨"));
//        }

        if (users.getFamilies().size() == 0) {
            users.addData(new Family("최씨", "010-4321-8765", "최씨"));
            users.addData(new Family("강씨", "010-4321-8765", "강씨"));
            users.addData(new Family("조씨", "010-4321-8765", "조씨"));
        }

//        if (users.getSocialWorkers().size() == 0) {
//            users.addData(new SocialWorker("송씨", "010-8765-4321"));
//            users.addData(new SocialWorker("남씨", "010-8765-4321"));
//            users.addData(new SocialWorker("진씨", "010-8765-4321"));
//        }

        registBroadcastReceiver();

        // 토큰을 보여줄 TextView를 정의
        mInformationTextView = (TextView) findViewById(R.id.gcm_id_text);
        mInformationTextView.setVisibility(View.GONE);
        // 토큰을 가져오는 동안 인디케이터를 보여줄 ProgressBar를 정의
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
        // 토큰을 가져오는 Button을 정의
        mRegistrationButton = (Button) findViewById(R.id.gcm_id_btn);
        mRegistrationButton.setOnClickListener(new View.OnClickListener() {
            /**
             * 버튼을 클릭하면 토큰을 가져오는 getInstanceIdToken() 메소드를 실행한다.
             * @param view
             */
            @Override
            public void onClick(View view) {
                getInstanceIdToken();
            }
        });
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
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                    // 액션이 READY일 경우
                    mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    mInformationTextView.setVisibility(View.GONE);
                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                    // 액션이 GENERATING일 경우
                    mRegistrationProgressBar.setVisibility(ProgressBar.VISIBLE);
                    mInformationTextView.setVisibility(View.VISIBLE);
                    mInformationTextView.setText(getString(R.string.registering_message_generating));
                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)) {
                    // 액션이 COMPLETE일 경우
                    mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    mRegistrationButton.setText(getString(R.string.registering_message_complete));
                    mRegistrationButton.setEnabled(false);
                    String token = intent.getStringExtra("token");
                    mInformationTextView.setText(token);
                }
            }
        };
    }

    /**
     * 앱이 실행되어 화면에 나타날때 LocalBoardcastManager에 액션을 정의하여 등록한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_family:
                    Intent familyList = new Intent(MainActivity.this, FamilyList.class);
                    startActivity(familyList);
                    break;
                case R.id.btn_friend:
                    Intent friendList = new Intent(MainActivity.this, ElderlyList.class);
                    startActivity(friendList);
                    break;
                case R.id.btn_social_worker:
                    Intent socialWorkerList = new Intent(MainActivity.this, SocialWorkerList.class);
                    startActivity(socialWorkerList);
                    break;
                default:
                    break;
            }
        }
    };
}
