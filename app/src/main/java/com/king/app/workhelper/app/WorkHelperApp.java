package com.king.app.workhelper.app;


import android.content.res.Configuration;
import android.os.Environment;

import com.antfortune.freeline.FreelineCore;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.stetho.Stetho;
import com.github.moduth.blockcanary.BlockCanary;
import com.king.app.workhelper.BuildConfig;
import com.king.app.workhelper.activity.CrashedActivity;
import com.king.app.workhelper.common.AppManager;
import com.king.app.workhelper.common.CrashHandler;
import com.king.app.workhelper.okhttp.LogInterceptor;
import com.king.app.workhelper.okhttp.MockInterceptor;
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
        
        ContextUtil.init(this);
        initOkHttp();
        initFresco();
        FreelineCore.init(this);
        AppManager.getInstance().init(this);
        initCrash();

//        Stetho.initializeWithDefaults(this);
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());

        initLeakCanary();
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }

    private boolean isMainProcess() {
        return AppUtil.getCurrentProcessName(this).equals(getPackageName());
    }

    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(AppConfig.HTTP_CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(AppConfig.HTTP_READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(AppConfig.HTTP_WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .cache(new Cache(FileUtil.createDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/cache"), AppConfig.HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));

        if (BuildConfig.LOG_DEBUG) {
            builder.addInterceptor(new LogInterceptor());
            builder.addInterceptor(new MockInterceptor());
        }
        OkHttpClient okHttpClient = builder.build();
        OkHttpUtils.initClient(okHttpClient);
    }

    private void initFresco() {
//        Fresco.initialize(this);
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(this, OkHttpUtils.getInstance().getOkHttpClient()).build();
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

    private void initLeakCanary() {
        if (BuildConfig.DEBUG) {
            mRefWatcher = LeakCanary.install(this);
            /*ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults()
                    .instanceField("android.animation.LayoutTransition$1", "val$parent")
                    .build();
            LeakCanary.enableDisplayLeakActivity(this);
            mRefWatcher = LeakCanary.refWatcher(this)
                    .listenerServiceClass(DisplayLeakService.class)
                    .excludedRefs(excludedRefs)
                    .buildAndInstall();
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    //IGNORE Activities: Update or add the class name here to ingore the memory leaks from those actvities 
//                    if (activity instanceof ThirdPartyOneActivity) return;
                    mRefWatcher.watch(activity);
                }

                @Override
                public void onActivityResumed(Activity activity) {

                }
            });*/
        } else {
            mRefWatcher = RefWatcher.DISABLED;
        } 
    }
    public static RefWatcher getRefWatcher() {
        WorkHelperApp application = (WorkHelperApp) ContextUtil.getAppContext().getApplicationContext();
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
