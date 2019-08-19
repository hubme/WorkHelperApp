package com.king.app.workhelper.app;


import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.stetho.Stetho;
import com.github.moduth.blockcanary.BlockCanary;
import com.king.app.workhelper.BuildConfig;
import com.king.app.workhelper.activity.CrashedActivity;
import com.king.app.workhelper.common.AppManager;
import com.king.app.workhelper.common.CrashHandler;
import com.king.app.workhelper.common.utils.LeakCanaryHelper;
import com.king.app.workhelper.okhttp.OkHttpProvider;
import com.king.app.workhelper.okhttp.SimpleOkHttp;
import com.king.app.workhelper.okhttp.interceptor.LogInterceptor;
import com.king.applib.base.BaseApplication;
import com.king.applib.log.Logger;
import com.king.applib.util.AppUtil;
import com.king.applib.util.ContextUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 全局Application
 * Created by VanceKing on 2016/9/28.
 */

public class WorkHelperApp extends BaseApplication {

    private static Application sApplication;

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
        Log.i("---", BuildConfig.GIT_SHA);
        sApplication = this;
        Logger.init(AppConfig.LOG_TAG).setShowLog(BuildConfig.LOG_DEBUG).methodCount(1);
        Logger.i("WorkHelperApp#onCreate()");
//        initStrictMode();

        ContextUtil.init(this);
        initARouter();
        initMineOkHttp();
        initFresco();
        AppManager.getInstance().init(this);
        initCrash();

//        Stetho.initializeWithDefaults(this);
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
//        initX5();

        LeakCanaryHelper.initLeakCanary(this);
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        setDefaultSystemTextSize();
        registerLifecycle();
    }

    public static Application getApplication() {
        return sApplication;
    }

    private boolean isMainProcess() {
        return AppUtil.getCurrentProcessName(this).equals(getPackageName());
    }

    private void initMineOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LogInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        SimpleOkHttp.init(okHttpClient);
    }

    private void initFresco() {
//        Fresco.initialize(this);
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(this, 
                OkHttpProvider.getInstance().getOkHttpClient()).build();
        Fresco.initialize(this, config);
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
        CrashHandler.getInstance().init(logSavedDir);
    }

    ///使更改系统字体大小无效
    private void setDefaultSystemTextSize() {
        Resources res = getResources();
        Configuration config = new Configuration();
//        config.setToDefaults();//此方法影响的属性较多。
        if (config.fontScale != 1.0f) {
            config.fontScale = 1.0f;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
    }

    private void initStrictMode() {
        //线程方面的策略
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        //VM方面的策略
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()//检查 Activity 的内存泄露
                .detectLeakedSqlLiteObjects()//检查 SQLiteCursor 或者 其他 SQLite 对象是否被正确关闭
                .detectLeakedClosableObjects()//检测资源有没有释放
                //.detectLeakedRegistrationObjects() //用来检查 BroadcastReceiver 或者 ServiceConnection 注册类对象是否被正确释放
                .penaltyLog()
                .penaltyDeath()//当触发违规条件时，直接Crash
                .build());
    }

    private void initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    private void registerLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                logMessage(activity, "onCreate()");
            }

            @Override public void onActivityStarted(Activity activity) {
                logMessage(activity, "onStart()");
            }

            @Override public void onActivityResumed(Activity activity) {
                logMessage(activity, "onResume()");
            }

            @Override public void onActivityPaused(Activity activity) {
                logMessage(activity, "onPause()");
            }

            @Override public void onActivityStopped(Activity activity) {
                logMessage(activity, "onStop()");
            }

            @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                logMessage(activity, "onSaveInstanceState()");
            }

            @Override public void onActivityDestroyed(Activity activity) {
                logMessage(activity, "onDestroy()");
            }
        });
    }


    private static void logMessage(Activity activity, String message) {
        Log.i("lifecycle", String.format("%1s#%2s", activity.getComponentName().getShortClassName(), message));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /*
    trim unneeded memory from its process
    回到后台时调用(返回键或home键)
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN: // 进行资源释放操作  
                break;
        }
    }
}
