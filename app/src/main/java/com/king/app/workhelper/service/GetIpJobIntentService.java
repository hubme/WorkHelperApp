package com.king.app.workhelper.service;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import com.king.applib.log.Logger;
import com.king.applib.util.HttpUtil;

import java.util.concurrent.Executors;

public class GetIpJobIntentService extends JobIntentService {
    private Runnable getIpTask;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i("onCreate()");
        getIpTask = () -> {
            String result = HttpUtil.get("https://ifconfig.me/all.json");
            Logger.i("result: " + result);
        };
        enqueueWork(this);
    }

    public static void enqueueWork(Context context) {
        enqueueWork(context, GetIpJobIntentService.class, 100, new Intent());
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Logger.i("onHandleWork()。intent: " + intent.toString());
        getIpOnThread();
    }


    private void getIpOnThread() {
        Executors.newFixedThreadPool(1).submit(getIpTask);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("onDestroy()");
    }
}
