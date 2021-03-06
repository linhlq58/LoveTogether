package com.valentineapp.lovetogether.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by lequy on 1/16/2017.
 */

public class CallReceiver extends PhoneCallReceiver {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
        removeLock(ctx);
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {

    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        addLock(ctx);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        removeLock(ctx);
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        addLock(ctx);
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        addLock(ctx);
    }

    private void addLock(Context context) {
        Intent i = new Intent("have_a_call");
        i.putExtra("command", "add");
        context.sendBroadcast(i);
    }

    private void removeLock(Context context) {
        Intent i = new Intent("have_a_call");
        i.putExtra("command", "remove");
        context.sendBroadcast(i);
    }
}
