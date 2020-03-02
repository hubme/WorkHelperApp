package com.king.app.workhelper.thirdparty.dragger2;

import javax.inject.Inject;

/**
 * @author VanceKing
 * @since 2020/2/27.
 */
public class CoffeeMaker {
    @Inject
    Heater heater;
    @Inject
    Heater heater2;
    @Inject
    Pump pump;
    @Inject
    IceBox iceBox;
    @Inject
    Ice ice;
    @Inject
    @Type("shuiguo") // 使用@Type来制定对应的构造函数
    Milk shuiguoMilk;

    @Inject
    @Type("singleton")
    Milk milk1;
    @Inject
    @Type("singleton")
    Milk milk2;

    /*public CoffeeMaker() {
        heater = new ElectricHeater();
        pump = new Thermosiphon();
        ice = new NanjiIce();
        iceBox = new HaierIceBox(ice);
    }*/

    public CoffeeMaker() {
        DaggerCoffeeComponent.create().inject(this);
    }

    public void brew() {
        /*heater.on();
        pump.pump();
        System.out.println(" [_]P coffee! [_]P ");
        iceBox.addIce();
        shuiguoMilk.addMilk();
        heater.off();*/
        System.out.println(milk1.toString());
        System.out.println(milk2.toString());
    }

    public static void main(String[] args) {
        CoffeeMaker maker = new CoffeeMaker();
        maker.brew();
    }
}
