package com.king.applib;

import android.content.Context;

import androidx.test.filters.SmallTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.king.applib.log.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * @author VanceKing
 * @since 2017/7/9 0009.
 */

@RunWith(AndroidJUnit4ClassRunner.class)
@SmallTest
public class LibBaseAndroidJUnit4Test {
    protected static final String TAG = "aaa";
    protected static Context mContext;

    @BeforeClass
    public static void setUp() {
        Logger.init(TAG).methodCount(1).hideThreadInfo();
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @AfterClass
    public static void tearDown() {
    }

    protected Context getContext() {
        return mContext;
    }
}
