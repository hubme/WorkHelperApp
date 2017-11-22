package com.king.app.workhelper;

import com.google.gson.Gson;
import com.king.applib.log.Logger;

import org.junit.Before;
import org.junit.Test;

/**
 * http://www.jianshu.com/p/e740196225a4
 *
 * @author huoguangxu
 * @since 2017/11/22.
 */

public class GsonTest extends BaseAndroidJUnit4Test {

    private static Gson mGson;

    private static class User {

    }

    @Before
    public void before() {
        mGson = new Gson();
    }

    @Test
    public void sample1() {
        Logger.i(mGson.fromJson("VanceKing", String.class));
        Logger.i("" + mGson.fromJson("1024.123", Double.class));
        Logger.i("" + mGson.fromJson("100", Integer.class));
    }
}
