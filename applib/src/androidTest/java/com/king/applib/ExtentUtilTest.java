package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;

import org.junit.Test;

/**
 * @author huoguangxu
 * @since 2017/2/13.
 */

public class ExtentUtilTest extends LibBaseAndroidJUnit4Test {
    @Test
    public void testIsNotificationEnable() {
        Logger.i("results: " + ExtendUtil.isNotificationEnable(mAppContext));
    }
}
