package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.util.ContextUtil;
import com.king.applib.util.ExtendUtil;

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
        Logger.i(ExtendUtil.getResourceName(R.string.app_name)+"==="+ExtendUtil.isXhdpi());
    }
}
