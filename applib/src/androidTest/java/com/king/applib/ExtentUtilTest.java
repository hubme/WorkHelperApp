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

    public void testIsAnyNull() throws Exception {
        Logger.i("results: " + ExtendUtil.isAnyNull(""));
        Logger.i("results: " + ExtendUtil.isAnyNull("", null));
        Logger.i("results: " + ExtendUtil.isAnyNull("", "0"));
        Logger.i("results: " + ExtendUtil.isAnyNull(null));
    }
    
    public void testIsNoneNull() throws Exception {
        Logger.i("results: " + ExtendUtil.isNoneNull(""));
        Logger.i("results: " + ExtendUtil.isNoneNull("", null));
        Logger.i("results: " + ExtendUtil.isNoneNull("", "0"));
        Logger.i("results: " + ExtendUtil.isNoneNull(null));
    }
}
