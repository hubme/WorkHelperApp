package com.king.applib;

import android.test.AndroidTestCase;

import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;

/**
 * Lib测试类
 * Created by HuoGuangxu on 2016/9/30.
 */

public class LibTest extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Logger.init(true, "aaa").methodCount(1).hideThreadInfo();
    }

    public void testPrintArray() throws Exception {
        ExtendUtil.printArray(new String[]{"我", "也", "不", "知", "道"});
        ExtendUtil.printArray(new Integer[]{1, 1, 5, 2, 0});
    }
}
