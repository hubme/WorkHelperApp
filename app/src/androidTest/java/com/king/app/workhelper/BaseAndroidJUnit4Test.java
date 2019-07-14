package com.king.app.workhelper;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.king.applib.log.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * @author VanceKing
 * @since 2017/7/9 0009.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class BaseAndroidJUnit4Test {
    protected static final String TAG = "aaa";

    static Context mContext;

    @BeforeClass
    public static void setUp() {
        Logger.init(TAG).methodCount(1).hideThreadInfo();
        mContext = InstrumentationRegistry.getTargetContext();
    }

    @AfterClass
    public static void tearDown() {
    }

    protected void logMessage(String message) {
        Log.i(TAG, message);
    }
}
