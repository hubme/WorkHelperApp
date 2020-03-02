package com.king.app.workhelper.thirdparty.dragger2;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author VanceKing
 * @since 2020/2/28.
 */
@Singleton
@Component(modules = BaseModule.class)
public interface BaseComponent {
    // 之前我们说过Component中的provideXXX是可以不写的，但是如果你想让别的Component依赖该Component，就必须写，不写的话意味着没有向外界暴露该依赖
    Heater provideHeater();
}
