package com.atti.atti_android.mainactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.atti.atti_android.R;
import com.atti.atti_android.playrtc.PlayRTCDisplay;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_family).setOnClickListener(listener);
        findViewById(R.id.btn_friend).setOnClickListener(listener);
        findViewById(R.id.btn_social_worker).setOnClickListener(listener);
    }

    Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_family:
                    Intent familyList = new Intent(MainActivity.this, PlayRTCDisplay.class);
                    startActivity(familyList);
                    break;
                case R.id.btn_friend:
                    Intent friendList = new Intent(MainActivity.this, PlayRTCDisplay.class);
                    startActivity(friendList);
                    break;
                case R.id.btn_social_worker:
                    Intent socialWorkerList = new Intent(MainActivity.this, PlayRTCDisplay.class);
                    startActivity(socialWorkerList);
                    break;
                default:
                    break;
            }
        }
    };
}
