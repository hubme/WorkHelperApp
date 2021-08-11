package com.king.app.workhelper.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.king.app.workhelper.R;
import com.king.app.workhelper.constant.GlobalConstant;

/**
 * @author VanceKing
 * @since 2018/6/21.
 */
public class NormalService extends Service {
    @Override
    public void onCreate() {
        Log.i(GlobalConstant.LOG_TAG, "onCreate");
        super.onCreate();
        prepareNotificationChannel();
        startNotification();
    }

    private void prepareNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyChannelId", "MyChannelName",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("ReadPhoneIp");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void startNotification() {
        Notification notification = new NotificationCompat.Builder(this,
                "MyChannelId").setContentTitle("ContentTitle")
                .setContentText("ContentText")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        startForeground(1024, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

    @Override
    public void onDestroy() {
        Log.i(GlobalConstant.LOG_TAG, "onDestroy");
        //停止前台服务的通知
//        stopForeground(true);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(GlobalConstant.LOG_TAG, "onBind");
        return new MyBind("Vance");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(GlobalConstant.LOG_TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    public static class MyBind extends Binder {
        private String name;

        public MyBind(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "MyBind{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
