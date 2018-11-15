package com.king.app.workhelper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.king.applib.log.Logger;

/**
 * @author VanceKing
 * @since 2018/11/15.
 */
public class CustomReceiver extends BroadcastReceiver {
    public static final String CUSTOM_RECEIVER_ACTION = "com.king.action";

    //context 为注册广播的 Activity
    @Override public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (CUSTOM_RECEIVER_ACTION.equals(action)) {
            Logger.i("onReceive()。 action: " + CUSTOM_RECEIVER_ACTION + " Context: " + context.toString());

            String extra = intent.getStringExtra("key");
            Logger.i("intent extra: " + extra);
        }
    }
}
