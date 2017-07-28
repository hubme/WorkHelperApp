package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.util.NumberUtil;

import org.junit.Test;

/**
 * @author huoguangxu
 * @since 2017/7/28.
 */

public class NumberUtilTest extends LibBaseAndroidJUnit4Test {

    @Test
    public void testIsNumber() throws Exception {
        Logger.i(String.valueOf(NumberUtil.isInteger("")));
        Logger.i(String.valueOf(NumberUtil.isInteger(" ")));
        Logger.i(String.valueOf(NumberUtil.isInteger("a")));
        Logger.i(String.valueOf(NumberUtil.isInteger("1")));
        Logger.i(String.valueOf(NumberUtil.isInteger("+1")));
        Logger.i(String.valueOf(NumberUtil.isInteger("-1")));
        Logger.i(String.valueOf(NumberUtil.isInteger("1.2")));
        Logger.i(String.valueOf(NumberUtil.isInteger("11")));
        Logger.i(String.valueOf(NumberUtil.isInteger("1 1")));
        Logger.i(String.valueOf(NumberUtil.isInteger("0a")));
        Logger.i(String.valueOf(NumberUtil.isInteger(" 1 d 0")));
        Logger.i(String.valueOf(NumberUtil.isInteger("0123")));
        Logger.i(String.valueOf(NumberUtil.isInteger("%$012df")));
        Logger.i(String.valueOf(NumberUtil.isInteger("%%&**)")));
    }

    @Test
    public void formatDouble() {
        final double number = 4567.9876;
        Logger.i("double number: " + NumberUtil.format(number, "###,###.00"));//case1: 1234567.9876-->1,234,567.99 case2: 4567.9876-->4,567.99
    }

    @Test
    public void formatFloat() {
        final float number = 4567.9876f;
        Logger.i("float number: " + NumberUtil.formatFloat(number, "###,###.00"));//case1: 1234567.9876f-->1,234,568.00 case2: 4567.9876f-->4,567.99
    }

    @Test
    public void formatInteger() {
        final float number = 1234567;
        Logger.i("float number: " + NumberUtil.format(number, "###,###.##"));
    }
}
