package com.king.app.workhelper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.king.applib.log.Logger;

/**
 * 亮屏和息屏广播。需要 android.permission.WAKE_LOCK 权限。只能动态注册。
 *
 * @author VanceKing
 * @since 2022/2/9
 */
public class ScreenOnOffReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Logger.i("onReceive：" + intent.getAction());
    }
}