package com.king.app.workhelper;

import android.test.AndroidTestCase;
import android.util.Log;

import com.king.app.workhelper.common.BusProvider;

/**
 * Created by HuoGuangxu on 2016/9/29.
 */

public class AppTest extends AndroidTestCase {

    public void testBusProvider() throws Exception {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override public void run() {
                    Log.i("aaa", BusProvider.getEventBus().toString());
                }
            }).start();
        }
    }
}
