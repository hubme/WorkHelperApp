package com.king.applib;

import com.king.applib.log.Logger;

/**
 * @author VanceKing
 * @since 2017/4/24.
 */

public class MathTest extends BaseTestCase {
    public void testSin() throws Exception {
        Logger.i("" + Math.sin(Math.toRadians(0)));//0
        Logger.i("" + Math.sin(Math.toRadians(45)));//0.7
        Logger.i("" + Math.sin(Math.toRadians(90)));//1
        
        Logger.i("" + Math.sin(Math.toRadians(135)));//0.7
        Logger.i("" + Math.sin(Math.toRadians(180)));//0
        
        Logger.i("" + Math.sin(Math.toRadians(225)));//-0.7
        Logger.i("" + Math.sin(Math.toRadians(270)));//-1
        
        Logger.i("" + Math.sin(Math.toRadians(315)));//-0.7
        Logger.i("" + Math.sin(Math.toRadians(360)));//0
        
    }

    public void testCos() throws Exception {
        Logger.i("" + Math.cos(Math.toRadians(0)));//1
        Logger.i("" + Math.cos(Math.toRadians(45)));//0.7
        Logger.i("" + Math.cos(Math.toRadians(90)));//0
    }
    
    public void testTan() throws Exception {
        Logger.i("" + Math.tan(Math.toRadians(0)));//0
        Logger.i("" + Math.tan(Math.toRadians(45)));//0.999999
        Logger.i("" + Math.tan(Math.toRadians(90)));//错误。1.633123935319537E16
    }
}
