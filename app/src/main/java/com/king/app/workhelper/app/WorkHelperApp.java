package com.king.app.workhelper.app;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;

import com.antfortune.freeline.FreelineCore;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.king.app.workhelper.BuildConfig;
import com.king.app.workhelper.activity.CrashedActivity;
import com.king.app.workhelper.common.AppManager;
import com.king.app.workhelper.common.CrashHandler;
import com.king.applib.base.BaseApplication;
import com.king.applib.log.Logger;
import com.king.applib.util.AppUtil;
import com.king.applib.util.ContextUtil;
import com.king.applib.util.FileUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
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
    3.多进程下回执行多次。解决办法是根据进程做相应的操作
     */
    @Override
    public void onCreate() {
        super.onCreate();
        if (!isMainProcess()) {
            return;
        }
        Logger.init(AppConfig.LOG_TAG).setShowLog(BuildConfig.LOG_DEBUG).methodCount(1);
        Logger.i("WorkHelperApp#onCreate()");
        mRefWatcher = LeakCanary.install(this);
        
        ContextUtil.init(this);
        initOkHttp();
        FreelineCore.init(this);
        Fresco.initialize(this);
        AppManager.getInstance().init(this);
        initCrash();

//        Stetho.initializeWithDefaults(this);
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());

    }

    private boolean isMainProcess() {
        return AppUtil.getCurrentProcessName(this).equals(AppUtil.getAppInfo(this).getPackageName());
    }

    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(AppConfig.HTTP_CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(AppConfig.HTTP_READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(AppConfig.HTTP_WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .cache(new Cache(FileUtil.createDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/cache"), AppConfig.HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));

        if (BuildConfig.LOG_DEBUG) {
//            builder.addInterceptor(new LoggingInterceptor());
//            builder.addInterceptor(new MockInterceptor());
        }
        OkHttpClient okHttpClient = builder.build();
        OkHttpUtils.initClient(okHttpClient);
    }

    private void initCrash() {
        CrashHandler.setCrashedActivity(CrashedActivity.class);
        CrashHandler.setCrashInBackground(true);

        String logSavedDir;
        if (BuildConfig.DEBUG) {
            logSavedDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000CrashLog";
        } else {
            logSavedDir = getCacheDir().getAbsolutePath() + "/CrashLog";
        }
        CrashHandler.getInstance().init(this, logSavedDir);
    }

    public static RefWatcher getRefWatcher(Context context) {
        WorkHelperApp application = (WorkHelperApp) context.getApplicationContext();
        return application.mRefWatcher;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //Logger.i("WorkHelperApp#onTerminate()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Logger.i("WorkHelperApp#onConfigurationChanged()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //Logger.i("WorkHelperApp#onLowMemory()");
    }

    /*
    trim unneeded memory from its process
    回到后台时调用(返回键或home键)
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        //Logger.i("WorkHelperApp#onTrimMemory()");
    }
}
