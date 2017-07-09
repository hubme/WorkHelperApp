package com.king.app.workhelper;

import com.king.applib.log.Logger;

import org.junit.Test;

/**
 * https://github.com/googlesamples/android-testing
 *
 * @author VanceKing
 * @since 2017/7/9.
 */

public class SampleAndroidJUnit4Test extends BaseAndroidJUnit4Test {
    @Test
    public void getScreenWidth() {
        Logger.i("width: " + mAppContext.getResources().getDisplayMetrics().widthPixels);
    }
}
