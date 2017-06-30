package com.king.applib.util;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期、时间工具类
 * Created by HuoGuangxu on 2016/9/25.
 */

public class DateTimeUtil {
    private DateTimeUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    public static final String INTL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SORT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String FULL_DATE_FORMAT = "yyyy年M月d日 HH:00";
    public static final String MONTH_DAY_HOUR_FORMAT = "MM月dd日 HH:00";

    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(INTL_DATE_FORMAT, Locale.US);
    private static final Date DATE = new Date();

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
        DATE.setTime(System.currentTimeMillis());
        return formatDate(DATE, template);
    }

    /** 获取当前日期 */
    public static Date getCurrentTime() {
        DATE.setTime(System.currentTimeMillis());
        return DATE;
    }

    /**
     * 计算两个日期之间的毫秒数.异常返回-1.<br/>
     * secondDay > firstDay返回正数;secondDay < firstDay返回负数;相等返回0;异常返回-1.
     * @param template 日期格式
     */
    public static long betweenDays(String template, String firstDay, String secondDay) {
        if (StringUtil.isAnyEmpty(template, firstDay, secondDay)) {
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

    /**
     * 毫秒时间格式化
     * @param time 时间，单位毫秒
     */
    public static String getUnitTime(long time) {
        if (time < DateUtils.SECOND_IN_MILLIS) {
            return String.format(Locale.getDefault(), "%d毫秒", time);
        } else if (time < DateUtils.MINUTE_IN_MILLIS) {
            return String.format(Locale.getDefault(), "%d秒", time / DateUtils.SECOND_IN_MILLIS);
        } else if (time < DateUtils.HOUR_IN_MILLIS) {
            return String.format(Locale.getDefault(), "%d分钟", time / DateUtils.MINUTE_IN_MILLIS);
        } else {
            return String.format(Locale.getDefault(), "%d小时", time / DateUtils.HOUR_IN_MILLIS);
        }
    }

    /**
     * 在指定的日期上加上一定的时间
     *
     * @param date   指定的日期
     * @param field  日期的字段
     * @param amount 增加的时间
     * @return 增加后的日期
     */
    public static Date addDate(Date date, int field, int amount) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

}
