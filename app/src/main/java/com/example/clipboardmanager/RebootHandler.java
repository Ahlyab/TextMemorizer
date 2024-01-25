package com.example.clipboardmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RebootHandler extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, LocalService.class);
            context.startForegroundService(serviceIntent);
        }
    }
}
