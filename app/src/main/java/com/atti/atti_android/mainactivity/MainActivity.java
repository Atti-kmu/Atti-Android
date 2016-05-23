package com.atti.atti_android.mainactivity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.androidquery.AQuery;
import com.atti.atti_android.R;
import com.atti.atti_android.data.DataGetThread;
import com.atti.atti_android.data.UsersDataManager;
import com.atti.atti_android.gcm.RegistrationIntentService;
import com.atti.atti_android.join.AutoLogin;
import com.atti.atti_android.list.ElderlyList;
import com.atti.atti_android.list.FamilyList;
import com.atti.atti_android.list.SocialWorkerList;
import com.atti.atti_android.service.PersistentService;
import com.atti.atti_android.service.RestartService;
import com.beardedhen.androidbootstrap.TypefaceProvider;

public class MainActivity extends Activity {
    private FragmentManager fm;

//    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
//    private static final String TAG = "MainActivity";

    //    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private AQuery aq;

    private FamilyList fl;
    private ElderlyList el;
    private SocialWorkerList sl;

    private SharedPreferences prefs;
    private BroadcastReceiver receiver;
    private Intent intentMyService;

    private DataGetThread family, friends, friendship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        TypefaceProvider.registerDefaultIconSets();

        UsersDataManager.getUsersInstance().getFamilies().clear();
        UsersDataManager.getUsersInstance().getElderly().clear();
        UsersDataManager.getUsersInstance().getSocialWorkers().clear();

        aq = new AQuery(this);

        init();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void init() {
        aq.id(R.id.btn_family).clicked(listener);
        aq.id(R.id.btn_friend).clicked(listener);
        aq.id(R.id.btn_social_worker).clicked(listener);
        aq.id(R.id.btn_logout).clicked(listener);

        fm = getFragmentManager();
        prefs = AutoLogin.getInstance();

        fl = new FamilyList();
        el = new ElderlyList();
        sl = new SocialWorkerList();
        fm.beginTransaction().add(R.id.list_fragment, fl, "Family").commit();

        Log.i("GCMToken", "" + RegistrationIntentService.getGCMToken());

        intentMyService = new Intent(this, PersistentService.class);
        receiver = new RestartService();

        try {
            IntentFilter mainFilter = new IntentFilter("com.hamon.GPSservice.ssss");
            registerReceiver(receiver, mainFilter);
            startService(intentMyService);
        } catch (Exception e) {
            Log.d("MpMainActivity", e.getMessage() + "");
            e.printStackTrace();
        }

        family = new DataGetThread("family", "http://52.79.147.144/mobile/family/1");
    }

//    /**
//     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
//     */
//    public void getInstanceIdToken() {
//        if (checkPlayServices()) {
//            // Start IntentService to register this application with GCM.
//            Intent intent = new Intent(this, RegistrationIntentService.class);
//            startService(intent);
//        }
//    }

//    /**
//     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
//     */
//    public void registBroadcastReceiver(){
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String action = intent.getAction();
//
//                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
//                    // 액션이 READY일 경우
////                    aq.id(R.id.registrationProgressBar).gone();
////                    aq.id(R.id.gcm_id_text).gone();
//                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
//                    // 액션이 GENERATING일 경우
////                    aq.id(R.id.registrationProgressBar).visible();
////                    aq.id(R.id.gcm_id_text).visible().text(R.string.registering_message_generating);
//                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)) {
//                    // 액션이 COMPLETE일 경우
////                    aq.id(R.id.registrationProgressBar).gone();
////                    aq.id(R.id.gcm_id_btn).text(R.string.registering_message_complete).enabled(false);
//                    String token = intent.getStringExtra("token");
////                    aq.id(R.id.gcm_id_text).text(token);
//                }
//            }
//        };
//    }

//    /**
//     * 앱이 실행되어 화면에 나타날때 LocalBoardcastManager에 액션을 정의하여 등록한다.
//     */
//    @Override
//    protected void onResume() {
//        super.onResume();
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
//
//    }

//    /**
//     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
//     */
//    @Override
//    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//        super.onPause();
//    }


//    /**
//     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
//     */
//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Log.i(TAG, "This device is not supported.");
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }

    Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UsersDataManager.getUsersInstance().getFamilies().clear();
            UsersDataManager.getUsersInstance().getElderly().clear();
            UsersDataManager.getUsersInstance().getSocialWorkers().clear();

            switch (v.getId()) {
                case R.id.btn_family:
                    family = new DataGetThread("family", "http://52.79.147.144/mobile/family/1");
                    fm.beginTransaction().replace(R.id.list_fragment, fl, "Family").commit();
                    break;
                case R.id.btn_friend:
                    friends = new DataGetThread("friends", "http://52.79.147.144/mobile/friends/1");
                    fm.beginTransaction().replace(R.id.list_fragment, el, "Friend").commit();
                    break;
                case R.id.btn_social_worker:
                    friendship = new DataGetThread("friendship", "http://52.79.147.144/mobile/friendship/1");
                    fm.beginTransaction().replace(R.id.list_fragment, sl, "SocialWorker").commit();
                    break;
                case R.id.btn_logout:
                    new DataGetThread("login", "http://52.79.147.144/mobile/user");
                    AutoLogin.logout();
                    finish();
                default:
                    break;
            }
        }
    };
}
