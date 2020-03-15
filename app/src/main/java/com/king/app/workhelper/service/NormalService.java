package com.king.app.workhelper.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

import com.king.app.workhelper.constant.GlobalConstant;

/**
 * @author VanceKing
 * @since 2018/6/21.
 */
public class NormalService extends Service {
    @Override public void onCreate() {
        Log.i(GlobalConstant.LOG_TAG, "onCreate");
        super.onCreate();
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(GlobalConstant.LOG_TAG, "onStartCommand");
        if (intent != null) {//bindService() intent = null
            String value = intent.getStringExtra("key");
            Log.i(GlobalConstant.LOG_TAG, "value from Intent: " + value);
        }
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
        return new MyBind("Vance");
    }

    @Override public boolean onUnbind(Intent intent) {
        Log.i(GlobalConstant.LOG_TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    public static class MyBind extends Binder {
        private String name;

        public MyBind(String name) {
            this.name = name;
        }

        @Override public String toString() {
            return "MyBind{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
