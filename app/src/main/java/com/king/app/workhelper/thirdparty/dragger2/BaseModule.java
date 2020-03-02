package com.king.app.workhelper.thirdparty.dragger2;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author VanceKing
 * @since 2020/2/28.
 */
@Module
public class BaseModule {
    @Singleton
    @Provides
    public Heater provideHeater() {
        return new ElectricHeater();
    }
}
