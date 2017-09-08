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

    public static final String I18N_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String INTL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD_FORMAT = "yyyy-MM-dd";

    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(INTL_DATE_FORMAT, Locale.US);
    private static final Date DATE = new Date();
    private static final Calendar CALENDAR = Calendar.getInstance();

    /**
     * 把日期格式化为指定格式
     *
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
     *
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
     *
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
     * 计算两个日期之间的毫秒数.
     *
     * @param template 日期格式
     * @return 相隔的毫秒数;相等返回0;异常返回-1.
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
        return Math.abs(secondDate.getTime() - firstDate.getTime());
    }

    /**
     * 计算两个日期之间的毫秒数.
     *
     * @return 相隔的毫秒数;相等返回0;异常返回-1.
     */
    public static long betweenDays(String template, Date firstDate, Date secondDate) {
        if (StringUtil.isNullOrEmpty(template) || firstDate == null || secondDate == null) {
            return -1;
        }
        DATE_FORMATTER.applyPattern(template);
        return betweenDays(template, DATE_FORMATTER.format(firstDate), DATE_FORMATTER.format(secondDate));
    }

    /**
     * 计算两个日期之间的毫秒数.<br/>
     * eg: betweenDurations(DateTimeUtil.INTL_DATE_FORMAT, "yyyy-MM-dd HH:mm:ss", "2017-10-23 13:01:01", "2017-10-23 12:00:00") ---> 3661000
     * betweenDurations(DateTimeUtil.INTL_DATE_FORMAT, "yyyy-MM-dd HH", "2017-10-23 12:00:00", "2017-10-23 13:01:01") ---> 3600000
     * betweenDurations(DateTimeUtil.INTL_DATE_FORMAT, "mm", "2017-10-23 12:00:00", "2017-10-23 13:01:01") --> 60000
     *
     * @param template         firstDate和secondDate的日期格式
     * @param comparedTemplate 比较时的日期格式
     * @param firstDate        第一个日期
     * @param secondDate       第二个日期
     * @return 相隔的毫秒数;相等返回0;异常返回-1.
     */
    public static long betweenDurations(String template, String comparedTemplate, String firstDate, String secondDate) {
        if (StringUtil.isAnyEmpty(template, comparedTemplate, firstDate, secondDate)) {
            return -1;
        }
        return betweenDays(comparedTemplate, parseDate(firstDate, template), parseDate(secondDate, template));
    }

    /**
     * 毫秒时间格式化
     *
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
        CALENDAR.setTime(date);
        CALENDAR.add(field, amount);
        return CALENDAR.getTime();
    }

}
