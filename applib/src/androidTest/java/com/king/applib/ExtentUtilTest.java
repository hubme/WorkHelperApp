package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;

/**
 * @author huoguangxu
 * @since 2017/2/13.
 */

public class ExtentUtilTest extends BaseTestCase {
    public void testScreenWidthHeight() throws Exception {
        Logger.i("width: " + ExtendUtil.getScreenWidth(mContext) + "");
        Logger.i("height: " + ExtendUtil.getScreenHeight(mContext) + "");
    }
}
