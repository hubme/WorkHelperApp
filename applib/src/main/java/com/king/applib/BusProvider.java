package com.king.applib;


import com.squareup.otto.Bus;

/**
 * 获取Bus的单例类
 *
 * @author VanceKing
 * @since 2016/9/29
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
