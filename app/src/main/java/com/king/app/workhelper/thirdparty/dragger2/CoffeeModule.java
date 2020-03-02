package com.king.app.workhelper.thirdparty.dragger2;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 使用 @Module 标记此类向外界提供注入的对象。
 * 而 @Module 类中的方法使用 @Provides 注解，名称的命名惯例是以 provide 作为前缀。@Provides 注解的方法返回注解对象实例。
 * 类命名惯例是以 Module 作为类名称结尾。
 *
 * @author VanceKing
 * @since 2020/2/27.
 */
@Module
public class CoffeeModule {

    @Provides
    Heater provideHeater() {
        return new ElectricHeater();
    }

    @Provides
    Pump providePump() {
        return new Thermosiphon();
    }

    @Provides
    Ice provideIce() {
        return new NanjiIce();
    }

    @Provides
    IceBox provideIceBox(Ice ice) {
        return new HaierIceBox(ice);
    }

    @Provides
    @Type("normal")
    Milk provideNormalMilk() {
        return new Milk();
    }

    @Provides
    @Type("shuiguo")
    Milk provideShuiGuoMilk(String shuiguo) {
        return new Milk(shuiguo);
    }

    //@Singleton 表明以单例的方式提供对象
    @Singleton
    @Provides
    @Type("singleton")
    Milk provideSingletonMilk(String shuiguo) {
        return new Milk(shuiguo);
    }

    //由于Milk构造函数里使用了String,所以这里要管理这个String，否则报错
    //int等基本数据类型是不需要这样做的
    @Provides
    public String provideString() {
        return "caomei";
    }
}
