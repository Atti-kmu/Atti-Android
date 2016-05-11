package com.atti.atti_android.playrtc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atti.atti_android.R;
import com.atti.atti_android.data.DataPostThread;
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

import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by 보운 on 2016-03-24.
 */
public class PlayRTCDisplay extends Activity {
    private final String T_DEVELOPERS_PROJECT_KEY = "9f081460-6001-4dfc-a4cd-60282bbcd701";
    private PlayRTC playrtc;
    private PlayRTCObserver playrtcObserver;
    private PlayRTCVideoView localView, remoteView;
    private PlayRTCMedia localMedia, remoteMedia;
    private RelativeLayout videoViewGroup;
    private PlayRTCAudioManager pAudioManager = null;

    private AlertDialog closeAlertDialog;

    private boolean isCloseActivity = false;
    private boolean isChannelConnected = false;

    private String channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);

        createPlayRTCObserverInstance();
        createPlayRTCInstance();
        setOnClickEventListenerToButton();

        try {
            playrtc.createChannel(new JSONObject());
        } catch (RequiredConfigMissingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Make the videoView at the onWindowFocusChanged time.
        if (hasFocus && this.localView == null) {
            createVideoView();
        }
    }

    private void createPlayRTCObserverInstance() {
        playrtcObserver = new PlayRTCObserver() {
            @Override
            public void onConnectChannel(final PlayRTC obj, final String channelId, final String channelCreateReason) {
                isChannelConnected = true;

                // Fill the channelId to the channel_id TextView.
                TextView channelIdTextView = (TextView) findViewById(R.id.channel_id);
                channelIdTextView.setText(channelId);
                new DataPostThread().execute(channelId);
            }

            @Override
            public void onAddLocalStream(final PlayRTC obj, final PlayRTCMedia playRTCMedia) {
                long delayTime = 0;

                localMedia = playRTCMedia;
                localView.show(delayTime);

                // Link the media stream to the view.
                playRTCMedia.setVideoRenderer(localView.getVideoRenderer());
            }

            @Override
            public void onAddRemoteStream(final PlayRTC obj, final String peerId, final String peerUserId, final PlayRTCMedia playRTCMedia) {
                long delayTime = 0;

                remoteMedia = playRTCMedia;
                remoteView.show(delayTime);

                // Link the media stream to the view.
                playRTCMedia.setVideoRenderer(remoteView.getVideoRenderer());
            }

            @Override
            public void onDisconnectChannel(final PlayRTC obj, final String disconnectReason) {
                long delayTime = 0;

                isChannelConnected = false;
                remoteView.hide(delayTime);
                localView.hide(delayTime);

                // Clean the channel_id TextView.
                TextView ChannelIdTextView = (TextView) findViewById(R.id.channel_id);
                ChannelIdTextView.setText(null);

                // Create PlayRTC instance again.
                // Because at the disconnect moment, the PlayRTC instance has removed.
                createPlayRTCInstance();
            }

            @Override
            public void onOtherDisconnectChannel(final PlayRTC obj, final String peerId, final String peerUserId) {
                remoteView.hide(0);

                // Delete channel and call onDisconnectChannel.
                playrtc.deleteChannel();
            }
        };
    }

    private void createPlayRTCInstance() {
        try {
            //function for sdk v2.1.2
            PlayRTCSettings settings = createPlayRTCConfiguration();
            playrtc = PlayRTCFactory.newInstance(settings, playrtcObserver);

        } catch (UnsupportedPlatformVersionException e) {
            e.printStackTrace();
        } catch (RequiredParameterMissingException e) {
            e.printStackTrace();
        }
    }

    private void setOnClickEventListenerToButton() {
        // Add a create channel event listener.
//        Button createButton = (Button) findViewById(R.id.create_button);
//        createButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                try {
//                    playrtc.createChannel(new JSONObject());
//                } catch (RequiredConfigMissingException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        // Add a connect channel event listener.
        Button connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    TextView ChannelIdInput = (TextView) findViewById(R.id.connect_channel_id);
                    channelId = ChannelIdInput.getText().toString();
                    playrtc.connectChannel(channelId, new JSONObject());
                } catch (RequiredConfigMissingException e) {
                    e.printStackTrace();
                }
            }
        });

        // Add a exit channel event listener.
        Button exitButton = (Button) findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playrtc.deleteChannel();
            }
        });
    }

    private PlayRTCConfig createPlayRTCConfig() {
        PlayRTCConfig config = PlayRTCFactory.createConfig();

        // PlayRTC instance have to get the application context.
        config.setAndroidContext(getApplicationContext());

        // T Developers Project Key.
        config.setProjectId(T_DEVELOPERS_PROJECT_KEY);

        config.video.setEnable(true);
        config.video.setCameraType(PlayRTCVideoConfig.CameraType.Front);
        // default resolution 640x480
        config.video.setMaxFrameSize(640, 480);
        config.video.setMinFrameSize(640, 480);

        config.audio.setEnable(true);   /* send video stream */
        /* use PlayRTCAudioManager */
        config.audio.setAudioManagerEnable(true);
        config.data.setEnable(true);    /* use datachannel stream */

        // Console logging setting
        config.log.console.setLevel(PlayRTCConfig.DEBUG);

        // File logging setting
        File logPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Android/data/" + getPackageName() + "/files/log/");
        if (!logPath.exists()) {
            logPath.mkdirs();
        }
        config.log.file.setLogPath(logPath.getAbsolutePath());
        config.log.file.setLevel(PlayRTCConfig.DEBUG);

        return config;
    }

    //function for sdk v2.1.2
    private PlayRTCSettings createPlayRTCConfiguration() {
        PlayRTCSettings settings = new PlayRTCSettings();

        // PlayRTC instance have to get the application context.
        settings.android.setContext(getApplicationContext());

        // T Developers Project Key.
        settings.setTDCProjectId(T_DEVELOPERS_PROJECT_KEY);

        settings.setAudioEnable(true);
        settings.setVideoEnable(true);
        settings.video.setFrontCameraEnable(true);
        settings.video.setBackCameraEnable(false);
        settings.setDataEnable(false);

        // File logging setting
        File logPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Android/data/" + getPackageName() + "/files/log/");
        if (!logPath.exists()) {
            logPath.mkdirs();
        }
        settings.log.file.setLogPath(logPath.getAbsolutePath());
        settings.log.file.setLevel(PlayRTCSettings.DEBUG);

        return settings;
    }

    private void createVideoView() {
        // Set the videoViewGroup which is contained local and remote video views.
        RelativeLayout myVideoViewGroup = (RelativeLayout) findViewById(R.id.video_view_group);

        if (localView != null) {
            return;
        }

        // Give my screen size to child view.
        Point myViewDimensions = new Point();
        myViewDimensions.x = myVideoViewGroup.getWidth();
        myViewDimensions.y = myVideoViewGroup.getHeight();

        if (remoteView == null) {
            createRemoteVideoView(myViewDimensions, myVideoViewGroup);
        }

        if (localView == null) {
            createLocalVideoView(myViewDimensions, myVideoViewGroup);
        }
    }

    private void createLocalVideoView(final Point parentViewDimensions, RelativeLayout parentVideoViewGroup) {
        if (localView == null) {
            // Create the video size variable.
            Point myVideoSize = new Point();
            myVideoSize.x = (int) (parentViewDimensions.x * 0.3);
            myVideoSize.y = (int) (parentViewDimensions.y * 0.7);

            // Create the view parameter.
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(myVideoSize.x, myVideoSize.y);
            param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            param.setMargins(30, 30, 30, 30);

            // Create the localViews.
            localView = new PlayRTCVideoView(parentVideoViewGroup.getContext(), myVideoSize);

            // Set the layout parameters.
            localView.setLayoutParams(param);

            // Add the view to the parentVideoViewGrop.
            parentVideoViewGroup.addView(localView);

            // Set the z-order.
            localView.setZOrderMediaOverlay(true);
        }
    }

    private void createRemoteVideoView(final Point parentViewDimensions, RelativeLayout parentVideoViewGroup) {
        if (remoteView == null) {
            // Create the video size variable.
            Point myVideoSize = new Point();
            myVideoSize.x = parentViewDimensions.x;
            myVideoSize.y = parentViewDimensions.y;

            // Create the view parameters.
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            // Create the remoteView.
            remoteView = new PlayRTCVideoView(parentVideoViewGroup.getContext(), myVideoSize);

            // Set the layout parameters.
            remoteView.setLayoutParams(param);

            // Add the view to the videoViewGroup.
            parentVideoViewGroup.addView(remoteView);
        }
    }

    private void setAudioManager() {
        pAudioManager = PlayRTCAudioManager.create(this, new Runnable() {
            @Override
            public void run() {

                // Search audio devices and send to PlayRTCV
                PlayRTCAudioManager.AudioDevice audioDivece = pAudioManager.getSelectedAudioDevice();
                if (playrtc != null) {
                    if (audioDivece == PlayRTCAudioManager.AudioDevice.WIRED_HEADSET) {
                        // ear-Phone
                        playrtc.notificationAudioType(PlayRTC.PlayRTCAudioType.AudioReceiver);
                    } else if (audioDivece == PlayRTCAudioManager.AudioDevice.SPEAKER_PHONE) {
                        // Speaker-Phone
                        playrtc.notificationAudioType(PlayRTC.PlayRTCAudioType.AudioSpeaker);
                    } else if (audioDivece == PlayRTCAudioManager.AudioDevice.EARPIECE) {
                        // Ear-Speakerphone
                        playrtc.notificationAudioType(PlayRTC.PlayRTCAudioType.AudioEarphone);
                    } else if (audioDivece == PlayRTCAudioManager.AudioDevice.BLUETOOTH) {
                        // Bluetooth
                        playrtc.notificationAudioType(PlayRTC.PlayRTCAudioType.AudioBluetooth);
                    }
                }
            }
        });

        // PlayRTCAudioManager run
        pAudioManager.init();
    }

    @Override
    protected void onDestroy() {
        // The sdk 2.2.0 version is set in PlayRTCConfig.
        // So do not use. pAudioManager is null value
        if (pAudioManager != null) {
            pAudioManager.close();
            pAudioManager = null;
        }
        // instance release
        if (playrtc != null) {
            // If you does not call playrtc.close(), playrtc instence is remaining every new call.
            playrtc.close();
            playrtc = null;
        }
        playrtcObserver = null;
        //android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

//    @Override
//    public void onBackPressed() {
//        if (isCloseActivity) {
//            super.onBackPressed();
//        } else {
//            createCloseAlertDialog();
//            closeAlertDialog.show();
//        }
//    }

    private void createCloseAlertDialog() {
        // Create the Alert Builder.
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set a Alert.
        alertDialogBuilder.setTitle(R.string.alert_title);
        alertDialogBuilder.setMessage(R.string.alert_message);
        alertDialogBuilder.setPositiveButton(R.string.alert_positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                if (isChannelConnected) {
                    isCloseActivity = false;

                    // null means my user id.
                    playrtc.disconnectChannel(null);
                } else {
                    isCloseActivity = true;
                    onBackPressed();
                }
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.alert_negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                isCloseActivity = false;
            }
        });

        // Create the Alert.
        closeAlertDialog = alertDialogBuilder.create();
    }
}
