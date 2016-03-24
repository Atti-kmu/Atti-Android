package com.atti.atti_android.mainactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atti.atti_android.R;
import com.atti.atti_android.playrtc.PlayRTCDisplay;
import com.sktelecom.playrtc.PlayRTC;
import com.sktelecom.playrtc.PlayRTCFactory;
import com.sktelecom.playrtc.config.PlayRTCConfig;
import com.sktelecom.playrtc.config.PlayRTCSettings;
import com.sktelecom.playrtc.config.PlayRTCVideoConfig;
import com.sktelecom.playrtc.exception.RequiredConfigMissingException;
import com.sktelecom.playrtc.exception.RequiredParameterMissingException;
import com.sktelecom.playrtc.exception.UnsupportedPlatformVersionException;
import com.sktelecom.playrtc.observer.PlayRTCObserver;
import com.sktelecom.playrtc.stream.PlayRTCMedia;
import com.sktelecom.playrtc.util.android.PlayRTCAudioManager;
import com.sktelecom.playrtc.util.ui.PlayRTCVideoView;

import org.json.JSONObject;

import java.io.File;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_view).setOnClickListener(listener);
    }

    Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start_view:
                    Intent playRTCView = new Intent(MainActivity.this, PlayRTCDisplay.class);
                    startActivity(playRTCView);
                    break;
                default:
                    break;
            }
        }
    };
}
