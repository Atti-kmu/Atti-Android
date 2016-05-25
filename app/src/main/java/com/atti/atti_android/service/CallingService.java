package com.atti.atti_android.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
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

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by BoWoon on 2016-05-10.
 */
public class CallingService extends Service {
    private View rootView;
    private String call_number, call_name, call_profile_img;
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private AQuery aq;
    private Bundle pushData;
    private MediaPlayer mediaPlayer;
    private Timer timer;

    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("start calling", "Start Calling");

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();

        int width = (int) (display.getWidth() * 0.3); //Display 사이즈의 90%

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

        timer = new Timer();
        timer.schedule(new CallTimer(), 60000);
        mediaPlayer = new MediaPlayer();
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), alert);
            MediaPlayer.create(getApplicationContext(), R.raw.bell_sound);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        call_number = pushData.getString("channel");
        call_name = pushData.getString("sender_name");
        call_profile_img = pushData.getString("sender_profile");

        Log.i("pushData", String.valueOf(pushData));
        Log.i("call_number", "" + call_number);
        Log.i("sender_name", "" + call_name);
        Log.i("call_profile_img", "" + call_profile_img);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        stopService(new Intent("com.atti.callService"));
//        removePopup();
    }

    public void removePopup() {
        if (rootView != null && windowManager != null)
            windowManager.removeView(rootView);
    }

    Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            timer.cancel();
            mediaPlayer.stop();

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

            stopService(new Intent("com.atti.callService"));
        }
    };

    private class CallTimer extends TimerTask {
        @Override
        public void run() {
            mediaPlayer.stop();
            removePopup();
        }
    }
}