package com.atti.atti_android.call_service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.atti.atti_android.R;
import com.atti.atti_android.playrtc.PlayRTCDisplay;

/**
 * Created by BoWoon on 2016-05-10.
 */
public class CallingService extends Service {
    public static final String EXTRA_CALL_NUMBER = "call_number";
    protected View rootView;
    private TextView callNameText;
    private ImageView callProfileimg;
    private String call_number, call_name, call_profile_img;
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private AQuery aq;

    private Bundle pushData;

    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();

        int width = (int) (display.getWidth() * 0.9); //Display 사이즈의 90%

        params = new WindowManager.LayoutParams(
                width,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                PixelFormat.TRANSLUCENT);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        rootView = layoutInflater.inflate(R.layout.call_layout, null);
//        ButterKnife.inject(this, rootView);
        setDraggable();
        aq = new AQuery(rootView);
        aq.id(R.id.reject).clicked(listener);
        aq.id(R.id.receive).clicked(listener);
    }

    private void setDraggable() {
        rootView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        if (rootView != null)
                            windowManager.updateViewLayout(rootView, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = "http://52.79.147.144/images/profile/";
        windowManager.addView(rootView, params);
        setExtra(intent);

//        if (!TextUtils.isEmpty(call_number))
        aq.id(R.id.call_name).text(call_name);
        aq.id(R.id.call_profile_img).image(url + call_profile_img);

        return START_REDELIVER_INTENT;
    }

    private void setExtra(Intent intent) {
        if (intent == null) {
            removePopup();
            return;
        }

        pushData = intent.getBundleExtra("Bundle");
//        call_number = intent.getStringExtra("channelID");
        call_number = pushData.getString("channel");
        call_name = pushData.getString("sender_id");
        call_profile_img = pushData.getString("sender_profile");

        Log.i("pushData", String.valueOf(pushData));
        Log.i("call_number", "" + call_number);
        Log.i("sender_name", "" + call_name);
        Log.i("call_profile_img", "" + call_profile_img);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removePopup();
    }

    public void removePopup() {
        if (rootView != null && windowManager != null)
            windowManager.removeView(rootView);
    }

    Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.receive:
                    removePopup();
                    Intent playRTC = new Intent(getApplicationContext(), PlayRTCDisplay.class);
                    Log.i("callChannelID", call_number);
                    playRTC.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    playRTC.putExtra("channelID", call_number);
                    playRTC.putExtra("connect", 1);
                    startActivity(playRTC);
                    break;
                case R.id.reject:
                    removePopup();
                    break;
                default:
                    break;
            }
        }
    };
}