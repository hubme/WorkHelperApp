package com.king.app.workhelper.app;


import android.content.Context;
import android.content.res.Configuration;

import com.king.app.workhelper.BuildConfig;
import com.king.applib.base.BaseApplication;
import com.king.applib.log.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 全局Application
 * Created by VanceKing on 2016/9/28.
 */

public class WorkHelperApp extends BaseApplication {

    private RefWatcher mRefWatcher;

    /*
    1.第一次安装会运行。
    2.应用在后台被杀死.ps:热启动、冷启动
     */
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        mRefWatcher = LeakCanary.install(this);

        initOkHttp();
        Logger.init(AppConfig.LOG_TAG).setShowLog(BuildConfig.LOG_DEBUG).methodCount(1);

    }

    private void initOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public static RefWatcher getRefWatcher(Context context) {
        WorkHelperApp application = (WorkHelperApp) context.getApplicationContext();
        return application.mRefWatcher;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //        Logger.i("WorkHelperApp#onTerminate()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //        Logger.i("WorkHelperApp#onConfigurationChanged()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //        Logger.i("WorkHelperApp#onLowMemory()");
    }

    /*
    trim unneeded memory from its process
    回到后台时调用(返回键或home键)
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        //        Logger.i("WorkHelperApp#onTrimMemory()");
    }
}
