package com.king.applib;

import android.content.Context;
import android.test.AndroidTestCase;

import com.king.applib.log.Logger;
import com.king.applib.util.StringUtil;

/**
 * StringUtils测试类
 * Created by HuoGuangxu on 2016/11/30.
 */

public class StringUtilsTest extends AndroidTestCase {
    public Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getContext();
        Logger.init("aaa").methodCount(1).hideThreadInfo();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testIsNumber() throws Exception {
        Logger.i(String.valueOf(StringUtil.isNumber("")));
        Logger.i(String.valueOf(StringUtil.isNumber(" ")));
        Logger.i(String.valueOf(StringUtil.isNumber("a")));
        Logger.i(String.valueOf(StringUtil.isNumber("1")));
        Logger.i(String.valueOf(StringUtil.isNumber("0a")));
        Logger.i(String.valueOf(StringUtil.isNumber(" 1 d 0")));
        Logger.i(String.valueOf(StringUtil.isNumber("0123")));
        Logger.i(String.valueOf(StringUtil.isNumber("%$012df")));
        Logger.i(String.valueOf(StringUtil.isNumber("%%&**)")));
    }
}
