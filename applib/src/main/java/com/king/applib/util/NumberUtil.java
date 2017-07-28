package com.king.applib.util;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * 数字相关工具类
 * https://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html
 *
 * @author VanceKing
 * @since 2016/9/14.
 */
public class NumberUtil {
    public static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat();

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

    /**
     * 判断字符串是否为整数。不包含符号<br/>
     *
     * @param number 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(final String number) {
        return isInteger(number, false);
    }

    /**
     * 判断字符串是否为整数。<br/>
     *
     * @param number 传入的字符串
     *               @param symbol 是否包含符号
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(final String number, boolean symbol) {
        if (StringUtil.isNullOrEmpty(number)) {
            return false;
        }
        if (symbol) {
            return Pattern.compile("^[-+]?\\d+$").matcher(number).matches();
        } else {
            return Pattern.compile("^\\d+$").matcher(number).matches();
        }
    }

    /**
     * 判断字符串是否为小数。不包含符号<br/>
     *
     * @param number 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isDecimal(String number) {
        return isDecimal(number, false);
    }

    /**
     * 判断字符串是否为小数。<br/>
     *
     * @param number 传入的字符串
     * @param symbol 是否包含符号
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isDecimal(String number, boolean symbol) {
        if (StringUtil.isNullOrEmpty(number)) {
            return false;
        }
        if (symbol) {
            return Pattern.compile("^[-+]?\\d+\\.\\d+$").matcher(number).matches();
        } else {
            return Pattern.compile("^\\d+\\.\\d+$").matcher(number).matches();
        }
    }

    public static synchronized String format(double number, String pattern) {
        DECIMAL_FORMATTER.applyPattern(pattern);
        return DECIMAL_FORMATTER.format(number);
    }

    public static synchronized String formatFloat(float number, String pattern) {
        DECIMAL_FORMATTER.applyPattern(pattern);
        return DECIMAL_FORMATTER.format(number);
    }


}
