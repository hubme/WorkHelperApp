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
import com.king.app.workhelper.common.utils.LeakCanaryHelper;
import com.king.app.workhelper.okhttp.CacheInterceptor;
import com.king.app.workhelper.okhttp.LogInterceptor;
import com.king.app.workhelper.okhttp.MockInterceptor;
import com.king.app.workhelper.okhttp.SimpleOkHttp;
import com.king.applib.base.BaseApplication;
import com.king.applib.log.Logger;
import com.king.applib.util.AppUtil;
import com.king.applib.util.ContextUtil;
import com.king.applib.util.FileUtil;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.smtt.sdk.QbSdk;
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
        initMineOkHttp();
        initFresco();
        FreelineCore.init(this, this);
        AppManager.getInstance().init(this);
        initCrash();

//        Stetho.initializeWithDefaults(this);
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
        initX5();

        initLeakCanary();
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
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

    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(AppConfig.HTTP_CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(AppConfig.HTTP_READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(AppConfig.HTTP_WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new CacheInterceptor())
                .addNetworkInterceptor(new CacheInterceptor())
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
            mRefWatcher = LeakCanaryHelper.getEnableRefWatcher(this);
        } else {
            mRefWatcher = LeakCanaryHelper.getDisabledRefWatcher();
        }
    }

    public static RefWatcher getRefWatcher() {
        WorkHelperApp application = (WorkHelperApp) ContextUtil.getAppContext().getApplicationContext();
        return application.mRefWatcher;
    }

    private void initX5() {
        QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override public void onCoreInitFinished() {
                Logger.i("onCoreInitFinished");
            }

            @Override public void onViewInitFinished(boolean success) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Logger.i("x5 init success");
            }
        });
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
