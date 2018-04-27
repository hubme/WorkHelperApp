package com.king.app.workhelper.drgger2;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * 表明本类属于Module，封装实例的访问。
 *
 * @author VanceKing
 * @since 2018/4/27.
 */
@Singleton
@Module
public class AppModule {
    private final Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    //表明为外部提供对象
    @Provides
    public Application getApplication() {
        return mApplication;
    }

    @Provides
    OkHttpClient providerOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.build();
    }

}
