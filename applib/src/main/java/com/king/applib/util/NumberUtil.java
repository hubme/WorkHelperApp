package com.king.applib.util;

/**
 * 数字操作工具类
 * Created by HuoGuangXu on 2016/9/14.
 */

public class NumberUtil {
    private NumberUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /**
     * 字符串转整型,转换出错返回-1
     */
    public static int getInt(String intString) {
        return getInt(intString, -1);
    }

    /**
     * 字符串转整型
     */
    public static int getInt(String intString, int defaultValue) {
        if (StringUtil.isNullOrEmpty(intString)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(intString);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     * 字符串转长整型,转换出错返回-1
     */
    public static long getLong(String longString) {
        return getLong(longString, -1L);
    }

    /**
     * 字符串转长整型
     */
    public static long getLong(String longString, long defaultValue) {
        if (StringUtil.isNullOrEmpty(longString)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(longString);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     * 字符串转float类型, 转换出错返回-1.0
     */
    public static float getFloat(String floatString) {
        return getFloat(floatString, -1F);
    }

    /**
     * 字符串转float类型
     */
    public static float getFloat(String floatString, float defaultValue) {
        if (StringUtil.isNullOrEmpty(floatString)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(floatString);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 字符串转double类型, 转换出错返回-1.0
     */
    public static double getDouble(String doubleString) {
        return getDouble(doubleString, -1);
    }

    /**
     * 字符串转double类型
     */
    public static double getDouble(String doubleString, float defaultValue) {
        if (StringUtil.isNullOrEmpty(doubleString)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(doubleString);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
