package com.king.applib;


import android.text.format.DateUtils;

import com.king.applib.log.Logger;
import com.king.applib.util.DateTimeUtil;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;


/**
 * 日期测试类
 * Created by VanceKing on 2016/12/9.
 */

public class DateTimeUtilTest extends BaseTestCase {
    public void testGetUnitTime() throws Exception {
        Logger.i(DateTimeUtil.getUnitTime(380L));
        Logger.i(DateTimeUtil.getUnitTime(38L * 1000));
        Logger.i(DateTimeUtil.getUnitTime(38L * 60 * 1000));
        Logger.i(DateTimeUtil.getUnitTime(38L * 60 * 60 * 1000));
    }

    public void testGetCurrentTime() throws Exception {
        Date currentTime = DateTimeUtil.getCurrentTime();
        Logger.i(DateTimeUtil.formatDate(currentTime, DateTimeUtil.INTL_DATE_FORMAT));
    }

    public void testGetCurrentTime2() throws Exception {
        Logger.i(DateTimeUtil.getCurrentTime(DateTimeUtil.INTL_DATE_FORMAT));
    }

    public void testBetweenDays() throws Exception {
        long day = DateTimeUtil.betweenDays(DateTimeUtil.INTL_DATE_FORMAT, "2017-10-23 12:00:00", "2017-10-23 13:01:01");
        Logger.i(day + "");
    }

    public void testBetweenDurations() throws Exception {
        Logger.i("全部比较: " + DateTimeUtil.betweenDurations(DateTimeUtil.INTL_DATE_FORMAT, "yyyy-MM-dd HH:mm:ss", "2017-10-23 13:01:01", "2017-10-23 12:00:00"));//timeMillis000 mils
        Logger.i("不比较秒: " + DateTimeUtil.betweenDurations(DateTimeUtil.INTL_DATE_FORMAT, "yyyy-MM-dd HH:mm", "2017-10-23 12:00:00", "2017-10-23 13:01:01"));//3660000 mils
        Logger.i("不比较分秒: " + DateTimeUtil.betweenDurations(DateTimeUtil.INTL_DATE_FORMAT, "yyyy-MM-dd HH", "2017-10-23 12:00:00", "2017-10-23 13:01:01"));//3600000
        Logger.i("只比较小时: " + DateTimeUtil.betweenDurations(DateTimeUtil.INTL_DATE_FORMAT, "HH", "2017-10-23 12:00:00", "2017-10-23 13:01:01"));//3600000
        Logger.i("只比较分钟: " + DateTimeUtil.betweenDurations(DateTimeUtil.INTL_DATE_FORMAT, "mm", "2017-10-23 12:00:00", "2017-10-23 13:01:01"));//60000
        Logger.i("只比较秒: " + DateTimeUtil.betweenDurations(DateTimeUtil.INTL_DATE_FORMAT, "ss", "2017-10-23 12:00:00", "2017-10-23 13:01:01"));//1000
    }

    @Test
    public void testAddDate() {
        Date currentDate = DateTimeUtil.getCurrentTime();
        Logger.i("currentDate: " + DateTimeUtil.formatDate(currentDate, DateTimeUtil.INTL_DATE_FORMAT));
        StopWatch.begin("aaa");
        Date date = DateTimeUtil.addDate(currentDate, Calendar.HOUR_OF_DAY, -1);
        StopWatch.end("aaa");
        Logger.i("Calendar.HOUR_OF_DAY, -1: " + DateTimeUtil.formatDate(date, DateTimeUtil.INTL_DATE_FORMAT));
        Logger.i("Calendar.DAY_OF_MONTH, 35: " + DateTimeUtil.formatDate(
                DateTimeUtil.addDate(currentDate, Calendar.DAY_OF_MONTH, 35), DateTimeUtil.INTL_DATE_FORMAT));
        Logger.i("Calendar.DAY_OF_YEAR, -1: " + DateTimeUtil.formatDate(
                DateTimeUtil.addDate(currentDate, Calendar.DAY_OF_YEAR, 35), DateTimeUtil.INTL_DATE_FORMAT));
    }

    public void testCalendar() throws Exception {
        Date date = new Date();//2016-06-09 16:46:30
        String currentTime = DateTimeUtil.formatDate(date, DateTimeUtil.INTL_DATE_FORMAT);
        Logger.i("currentTime: " + currentTime);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Logger.i("aaa: " + DateTimeUtil.formatDate(calendar.getTime(), DateTimeUtil.INTL_DATE_FORMAT));

        calendar.add(Calendar.MONTH, 1);
        Date afterDate = calendar.getTime();

        String afterDateStr = DateTimeUtil.formatDate(afterDate, DateTimeUtil.INTL_DATE_FORMAT);
        Logger.i("afterDateStr: " + afterDateStr);
    }

    /*
     * FORMAT_SHOW_DATE：3月3日
     * FORMAT_SHOW_TIME：10:37
     * FORMAT_SHOW_WEEKDAY：星期五
     * FORMAT_SHOW_YEAR：2017年3月3日
     * FORMAT_NUMERIC_DATE：3/3
     * FORMAT_NO_MONTH_DAY：三月
     */
    public void testDateUtils() throws Exception {
        Calendar calendar = Calendar.getInstance();
        //month 会+1
        calendar.set(2018, 2, 8, 10, 4, 8);//2018-03-08 10:04:08
        Date date = calendar.getTime();
        long timeMillis = date.getTime();
        Logger.i("FORMAT_SHOW_DATE: " + DateUtils.formatDateTime(getContext(), timeMillis, DateUtils.FORMAT_SHOW_DATE) +
                "\nFORMAT_SHOW_TIME: " + DateUtils.formatDateTime(getContext(), timeMillis, DateUtils.FORMAT_SHOW_TIME) +
                "\nFORMAT_SHOW_WEEKDAY: " + DateUtils.formatDateTime(getContext(), timeMillis, DateUtils.FORMAT_SHOW_WEEKDAY) +
                "\nFORMAT_SHOW_YEAR: " + DateUtils.formatDateTime(getContext(), timeMillis, DateUtils.FORMAT_SHOW_YEAR) +
                "\nFORMAT_NUMERIC_DATE: " + DateUtils.formatDateTime(getContext(), timeMillis, DateUtils.FORMAT_NUMERIC_DATE) +
                "\nFORMAT_NO_MONTH_DAY: " + DateUtils.formatDateTime(getContext(), timeMillis, DateUtils.FORMAT_NO_MONTH_DAY)

        );


    }
}
