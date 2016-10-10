package com.king.app.workhelper.app;


import com.king.app.workhelper.BuildConfig;
import com.king.applib.base.BaseApplication;
import com.king.applib.log.Logger;

/**
 * 全局Application
 * Created by VanceKing on 2016/9/28.
 */

public class WorkHelperApp extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(AppConfig.LOG_TAG).setShowLog(BuildConfig.LOG_DEBUG).methodCount(1).hideThreadInfo();
    }
}
