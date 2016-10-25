package com.king.app.workhelper.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.king.applib.log.Logger;

/**
 * Created by HuoGuangxu on 2016/10/21.
 */

public class AppDownloadService extends IntentService {
    public AppDownloadService() {
        super("AppDownloadService");
    }

    public AppDownloadService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i("onCreate");

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Logger.i("onStart");















    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.i("onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("onDestroy");

    }

    @Nullable @Override
    public IBinder onBind(Intent intent) {
        Logger.i("onBind");

        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.i("onHandleIntent");
    }
}
