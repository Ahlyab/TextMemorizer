package com.example.clipboardmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class LocalService extends Service {
    public LocalService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.e("Hello", "run: service running" );

                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e) {
                        Log.e("ERROR_THREAD", "run: failed to sleep" );
                    }
                }

            }
        }).start();
        final String CHANNELID = "ForeGround Service ID";

        NotificationChannel channel = new NotificationChannel(CHANNELID, CHANNELID, NotificationManager.IMPORTANCE_LOW);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNELID)
                .setContentText("Service is running")
                .setContentTitle("Service is Enabled")
                .setSmallIcon(R.drawable.ic_launcher_background);
        startForeground(1001, notification.build());


        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}