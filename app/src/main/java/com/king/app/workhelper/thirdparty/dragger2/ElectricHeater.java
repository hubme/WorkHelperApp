package com.king.app.workhelper.thirdparty.dragger2;

/**
 * @author VanceKing
 * @since 2020/2/27.
 */
class ElectricHeater implements Heater {
    @Override
    public void on() {
        System.out.println("~ ~ ~ heating ~ ~ ~");
    }

    @Override
    public void off() {

    }
}
