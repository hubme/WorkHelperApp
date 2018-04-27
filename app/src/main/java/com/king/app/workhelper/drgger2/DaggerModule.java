package com.king.app.workhelper.drgger2;

import com.king.app.workhelper.fragment.Dagger2Fragment;
import com.king.app.workhelper.model.dagger2.DaggerModel;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Module: 模块。用于封装目标对象（Activity、Fragment）需要的对象实例。
 *
 * @author VnaceKing
 * @since 2018/4/26.
 */

@Module
public class DaggerModule {
    private Dagger2Fragment fragment;

    public DaggerModule(Dagger2Fragment fragment) {
        this.fragment = fragment;
    }

    @Named("aaa")
    @Provides
    DaggerModel provideStudent() {
        return new DaggerModel();
    }

    //使用@Inject时也要指定@Named，一一对应的关系，由于多个方法返回同一个类的对象，标记使用哪个方法生成的对象。
    @Named("one")
    @Provides
    DaggerModel provideStudent2() {
        return new DaggerModel("aaa");
    }
    
    /*@AQualifier
    @Provides
    DaggerModel provideStudent3() {
        return new DaggerModel("AQualifier");
    }*/
}
