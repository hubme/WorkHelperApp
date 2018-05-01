package com.king.app.workhelper.drgger2;

import com.king.app.workhelper.model.dagger2.DaggerModel;
import com.king.applib.log.Logger;

import javax.inject.Named;
import javax.inject.Singleton;

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

    @Named
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

    @Named("single")
    @Provides
    @Singleton//和代码中创建单例不同，Dagger2 创建的单例对象保存在Component中，Component 能创建多。
    DaggerModel provideSingleStudent() {
        return new DaggerModel("single");
    }
    
    @Provides
    @Named("per_activity")
    @PerActivity
    DaggerModel providePerActivityStudent() {
        return new DaggerModel("per_activity");
    }

    @ObjectQualifier
    @Provides
    DaggerModel provideStudent3() {
        return new DaggerModel("AQualifier");
    }

    @ObjectQualifier(1)
    @Provides
    DaggerModel provideStudent4() {
        return new DaggerModel("AQualifier 1");
    }
    
    @ObjectQualifier(2)
    @Provides
    DaggerModel provideStudent5() {
        Logger.i("before instance...");
        return new DaggerModel("AQualifier 2");
    }

}
