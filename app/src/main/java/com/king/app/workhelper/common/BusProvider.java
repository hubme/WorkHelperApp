package com.king.app.workhelper.common;


import com.squareup.otto.Bus;

/**
 * 获取Bus的单例类
 * Created by HuoGuangxu on 2016/9/29.
 */

public class BusProvider {
    private BusProvider() {

    }

    private static class BusHolder {
        private static final Bus BUS = new Bus();
    }

    public static Bus getEventBus() {
        return BusHolder.BUS;
    }
}
