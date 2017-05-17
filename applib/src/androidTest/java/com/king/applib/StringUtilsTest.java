package com.king.applib;

import android.content.Context;
import android.test.AndroidTestCase;

import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.StringUtil;

import java.util.Arrays;
import java.util.Locale;

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
    
    public void testStringformat() throws Exception {
        Logger.i(String.format(Locale.US, "%,.2f", 1234526.123f));
    }
    
    public void testArraysCopy() throws Exception {
        String[] texts = new String[]{"aaa", "bbb", "ccc"};
        String[] textss = Arrays.copyOf(texts, texts.length);
        ExtendUtil.printArray(textss);
    }

    public void testStringLength() throws Exception {
        Logger.i(String.valueOf("".length()));
        Logger.i(String.valueOf("哈".length()));
        Logger.i(String.valueOf("h".length()));
        Logger.i(String.valueOf("哈哈".length()));
        Logger.i(String.valueOf("hh".length()));
        Logger.i(String.valueOf(" ".length()));
        Logger.i(String.valueOf("0".length()));
        Logger.i(String.valueOf(".".length()));
        Logger.i(String.valueOf("&".length()));
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

    public void testIsNotEmpty() throws Exception {
        String text = null;
        Logger.i(String.valueOf(StringUtil.isNotEmpty(text)));
        Logger.i(String.valueOf(StringUtil.isNotEmpty("")));
        Logger.i(String.valueOf(StringUtil.isNotEmpty(" ")));
        Logger.i(String.valueOf(StringUtil.isNotEmpty("000")));
    }

    public void testNoneEmpty() throws Exception {
        String text = null;
        Logger.i(String.valueOf(StringUtil.isNoneEmpty()));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty("")));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty(" ")));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty(text)));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty("0a")));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty("0a", "")));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty("0a", text)));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty("0a", "ab")));
    }
}
