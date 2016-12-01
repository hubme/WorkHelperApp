package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.util.UserUtil;

/**
 * UserUtil工具类
 * Created by HuoGuangxu on 2016/11/30.
 */

public class UserUtilTest extends BaseTestCase {

    public void testVerifyIDCard() throws Exception {
        Logger.i(String.valueOf(UserUtil.verifyIDCard("")));
        Logger.i(String.valueOf(UserUtil.verifyIDCard("000000000000000")));
        Logger.i(String.valueOf(UserUtil.verifyIDCard("412722198904207778")));
        Logger.i(String.valueOf(UserUtil.verifyIDCard("371481198510180920")));
    }
}
