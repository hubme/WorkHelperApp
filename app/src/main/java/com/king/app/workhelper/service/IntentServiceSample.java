package com.king.app.workhelper.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author VanceKing
 * @since 2018/5/11.
 */
public class IntentServiceSample extends IntentService {
    public IntentServiceSample() {
        super("IntentServiceSample");
        Log.i("aaa", "IntentServiceSample");
    }

    @Override public void onCreate() {
        Log.i("aaa", "onCreate");
        super.onCreate();

    }

    @Override public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.i("aaa", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override public void onDestroy() {
        Log.i("aaa", "onDestroy");
        super.onDestroy();
    }

    @Override protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        String name = intent.getStringExtra("name");
        switch (name) {
            case "aaa":
                SystemClock.sleep(5000);
                Log.i("aaa","任务aaa执行完成");
                break;
            case "bbb":
                SystemClock.sleep(2000);
                Log.i("aaa","任务bbb执行完成");
                break;
        }
    }
}
