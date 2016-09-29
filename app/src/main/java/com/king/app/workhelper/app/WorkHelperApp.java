package com.king.app.workhelper.app;


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
        Logger.init(true, AppConfig.LOG_TAG);
    }
}
