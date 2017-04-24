package com.king.applib;

import com.king.applib.log.Logger;

/**
 * @author huoguangxu
 * @since 2017/4/24.
 */

public class MathTest extends BaseTestCase {
    public void testSin() throws Exception {
        Logger.i(""+Math.tan(45));
        Logger.i("" + Math.tan(Math.PI / 4));
        Logger.i("" + Math.tan(Math.toDegrees(45)));
        Logger.i("" + Math.tan(Math.toRadians(45)));
    }
}
