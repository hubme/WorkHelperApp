package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.util.ContextUtil;
import com.king.applib.util.DateTimeUtil;
import com.king.applib.util.ExtendUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * @author huoguangxu
 * @since 2017/2/13.
 */

public class ExtentUtilTest extends BaseTestCase {
    @Override protected void setUp() throws Exception {
        super.setUp();
        ContextUtil.init(mContext);
    }

    public void testScreenWidthHeight() throws Exception {
        Logger.i("width: " + ExtendUtil.getScreenWidth() + "");
        Logger.i("height: " + ExtendUtil.getScreenHeight() + "");
    }

    public void testGetResourceName() throws Exception {
        Logger.i(ExtendUtil.getResourceName(R.string.app_name) + "===" + ExtendUtil.isXhdpi());
    }

    public void testGetResourceId() throws Exception {
        Logger.i(ExtendUtil.getDrawableResId("app_name") + "");
        Logger.i(ExtendUtil.getDrawableResId("home") + "");
        Logger.i(ExtendUtil.getDrawableResId("") + "");
    }

    public void testIsNotificationEnable() throws Exception {
        Logger.i(ExtendUtil.isNotificationEnable(mContext) ? "可用" : "不可用");
    }

    public void testCalendar() throws Exception{
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
}
