package com.valentineapp.lovetogether.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.activities.LockActivity;
import com.valentineapp.lovetogether.utils.Constant;

/**
 * Created by lequy on 2/7/2017.
 */

public class LockScreenReceiver extends BroadcastReceiver {
    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        sharedPreferences = MyApplication.getSharedPreferences();

        //If the screen was just turned on or it just booted up, start your Lock Activity
        if (sharedPreferences.getBoolean("lockscreen", false)) {
            if (action.equals(Intent.ACTION_SCREEN_OFF) || action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                Intent i = new Intent(context, LockActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

                Intent mainIntent = new Intent(Constant.CREATE_LOCK_VIEW);
                context.sendBroadcast(mainIntent);
            }
        }
    }
}
