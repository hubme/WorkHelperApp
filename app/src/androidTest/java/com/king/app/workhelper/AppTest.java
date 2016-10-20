package com.king.app.workhelper;

import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;

import com.king.app.workhelper.common.BusProvider;

import static android.os.Environment.DIRECTORY_DCIM;

/**
 * Created by HuoGuangxu on 2016/9/29.
 */

public class AppTest extends AndroidTestCase {
    private static final String TAG = "aaa";

    public void testBusProvider() throws Exception {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override public void run() {
                    Log.i("aaa", BusProvider.getEventBus().toString());
                }
            }).start();
        }
    }

    public void testGetFilePath() throws Exception{
        Log.i(TAG, Environment.getDataDirectory().getAbsolutePath());
        Log.i(TAG, Environment.getDownloadCacheDirectory().getAbsolutePath());
        Log.i(TAG, Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.i(TAG, Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).getPath());
        Log.i(TAG, Environment.getRootDirectory().getAbsolutePath());
    }
}
