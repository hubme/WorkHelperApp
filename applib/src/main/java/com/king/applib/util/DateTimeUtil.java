package com.king.applib.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期、时间工具类
 * Created by HuoGuangxu on 2016/9/25.
 */

public class DateTimeUtil {
    public static final String INTL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FULL_DATE_FORMAT = "yyyy年M月d日 HH:00";
    public static final String MONTH_DAY_HOUR_FORMAT = "MM月dd日 HH:00";

    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(INTL_DATE_FORMAT, Locale.US);

    /**
     * 把日期格式化为指定格式
     * @param date 日期
     * @return 格式化后的字符串
     */
    public static String formatDate(Date date, String template) {
        if (date == null || StringUtil.isNullOrEmpty(template)) {
            return "";
        }
        DATE_FORMATTER.applyPattern(template);
        return DATE_FORMATTER.format(date);
    }

    /**
     * 字符串转化为Date,转换出错返回null.使用返回的对象时要判null
     * @param dateText 日期字符串
     * @param template 日期格式
     * @return 日期
     */
    public static Date parseDate(String dateText, String template) {
        if (StringUtil.isNullOrEmpty(dateText) || StringUtil.isNullOrEmpty(template)) {
            return null;
        }
        DATE_FORMATTER.applyPattern(template);
        try {
            return DATE_FORMATTER.parse(dateText);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取手机当前时间
     * @param template 日期格式
     * @return 手机当前时间
     */
    public static String getCurrentTime(String template) {
        return formatDate(new Date(), template);
    }

    /**
     * 计算两个日期之间的毫秒数.异常返回-1.<br/>
     * secondDay > firstDay返回正数;secondDay < firstDay返回负数;相等返回0;异常返回-1.
     * @param template 日期格式
     */
    public static long betweenDays(String template, String firstDay, String secondDay) {
        if (StringUtil.containsNullOrEmpty(template, firstDay, secondDay)) {
            return -1;
        }
        DATE_FORMATTER.applyPattern(template);
        Date firstDate = parseDate(firstDay, template);
        Date secondDate = parseDate(secondDay, template);
        if (firstDate == null || secondDate == null) {
            return -1;
        }
        return secondDate.getTime() - firstDate.getTime();
    }
}
