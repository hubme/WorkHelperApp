package com.king.app.workhelper.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.king.app.workhelper.constant.GlobalConstant;

/**
 * @author VanceKing
 * @since 2018/6/21.
 */
public class NormalService extends Service {
    @Override public void onCreate() {
        super.onCreate();
        Log.i(GlobalConstant.LOG_TAG, "onCreate");
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(GlobalConstant.LOG_TAG, "onStartCommand");
        //设置服务为前台Service
//        Notification notification = null;
//        startForeground(0x11, notification);
        return Service.START_STICKY;
    }

    @Override public void onDestroy() {
        Log.i(GlobalConstant.LOG_TAG, "onDestroy");
        //停止前台服务的通知
//        stopForeground(true);
        super.onDestroy();
    }

    @Nullable @Override public IBinder onBind(Intent intent) {
        Log.i(GlobalConstant.LOG_TAG, "onBind");
        return null;
    }


}
