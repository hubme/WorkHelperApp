package com.king.app.workhelper;

import android.util.Log;

import com.king.applib.util.DateTimeUtil;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * @author VanceKing
 * @since 2020/4/9.
 */
public class DateTimeUtilTest extends BaseAndroidJUnit4Test {

    @Test
    public void testCalendar() {
        Calendar calendar = Calendar.getInstance();
        //calendar.set(2020, 3, 16);//2020-4-16
        Log.i(TAG, "date: " + DateTimeUtil.formatDate(calendar.getTime(), DateTimeUtil.YMD_FORMAT));
        Log.i(TAG, "DATE: " + calendar.get(Calendar.DATE));
        Log.i(TAG, "DAY_OF_YEAR: " + calendar.get(Calendar.DAY_OF_YEAR));
        Log.i(TAG, "DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
        Log.i(TAG, "DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
        Log.i(TAG, "DAY_OF_WEEK_IN_MONTH: " + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
    }

    @Test
    public void addDate() {
        assertEquals("2020-03-01", DateTimeUtil.addDate("2020-02-28", DateTimeUtil.YMD_FORMAT, Calendar.DATE, 2));

        assertEquals("2020-04-11", DateTimeUtil.addDate("2020-04-09", DateTimeUtil.YMD_FORMAT, Calendar.DAY_OF_YEAR, 2));
        assertEquals("2020-04-11", DateTimeUtil.addDate("2020-04-09", DateTimeUtil.YMD_FORMAT, Calendar.DAY_OF_MONTH, 2));
        assertEquals("2020-04-11", DateTimeUtil.addDate("2020-04-09", DateTimeUtil.YMD_FORMAT, Calendar.DAY_OF_WEEK, 2));
        //加两周
        assertEquals("2020-04-23", DateTimeUtil.addDate("2020-04-09", DateTimeUtil.YMD_FORMAT, Calendar.DAY_OF_WEEK_IN_MONTH, 2));
        assertEquals("2021-01-09", DateTimeUtil.addDate("2020-11-09", DateTimeUtil.YMD_FORMAT, Calendar.MONTH, 2));

        //assertEquals("2021-6-9", DateTimeUtil.addDate("2020-4-9", DateTimeUtil.YMD_FORMAT, Calendar.MONTH, 2));
        //assertEquals("2021-6-9", DateTimeUtil.addDate("2020-4-9", "yyyy-MM", Calendar.MONTH, 2));
        String aaa = DateTimeUtil.addDate("2020-4-9", "111", Calendar.MONTH, 2);
        Log.i(TAG, "aaa: " + aaa);
    }

}
