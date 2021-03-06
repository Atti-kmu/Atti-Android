package com.atti.atti_android.gcm;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.atti.atti_android.R;
import com.atti.atti_android.data.UsersDataManager;
import com.atti.atti_android.service.CallingService;
import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by BoWoon on 2016-04-07.
 */
public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";

    /**
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
//        String message = data.getString("message");
        String message = data.getString("channel");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Message: " + message);

        Log.i(TAG, "From: " + from);
        Log.i(TAG, "Title: " + title);
        Log.i(TAG, "Message: " + data.get("channel"));

        // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
        if (!UsersDataManager.connection)
            sendNotification(data);
    }

    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     *
     * @param message
     */
    private void sendNotification(Bundle message) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(message.getString("channel"))
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        Intent serviceIntent = new Intent(getApplicationContext(), CallingService.class);
        serviceIntent.putExtra("Bundle", message);
        getApplicationContext().startService(serviceIntent);
//        getApplicationContext().startActivity(serviceIntent);
    }
}
