package com.king.applib;


import com.king.applib.log.Logger;
import com.king.applib.util.DateTimeUtil;

import java.util.Date;


/**
 * 日期测试类
 * Created by HuoGuangxu on 2016/12/9.
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
}
