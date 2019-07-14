package com.king.app.workhelper;

import com.king.applib.log.Logger;
import com.king.applib.util.HttpUtil;

/**
 * @author VanceKing
 * @since 2017/3/12 0012.
 */

public class HttpUtilTest extends BaseAndroidJUnit4Test {
    public static final String BAI_DU_URL = "http://www.baidu.com";

    public void testHttpUtilGet() throws Exception{
        String result = HttpUtil.get(BAI_DU_URL);
        Logger.i(result);
    }
}