package com.king.app.workhelper.common.utils;

/**
 * 字符串工具类
 * Created by HuoGuangxu on 2016/9/29.
 */

public class StringUtil {
    private StringUtil() {

    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }
}
