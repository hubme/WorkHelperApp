package com.king.applib;

import android.app.Activity;

import com.king.applib.log.Logger;
import com.king.applib.util.AppUtil;

/**
 * AppUtil测试类
 * Created by VanceKing on 2016/12/18 0018.
 */

public class AppUtilTest extends BaseTestCase {
    public void testGetActivityProcessName() throws Exception {
        Logger.i(AppUtil.getActivityProcessName(mContext, Activity.class));
    }
}
