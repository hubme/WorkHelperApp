package com.king.app.workhelper.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

import com.king.app.workhelper.BuildConfig;
import com.king.app.workhelper.thirdparty.tinker.MyLogImp;
import com.king.app.workhelper.thirdparty.tinker.TinkerManager;
import com.king.applib.log.Logger;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.ApplicationLifeCycle;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * because you can not use any other class in your application, we need to
 * move your implement of Application to {@link ApplicationLifeCycle}
 * As Application, all its direct reference class should be in the main dex.
 * <p>
 * We use tinker-android-anno to make sure all your classes can be patched.
 * <p>
 * application: if it is start with '.', we will add SampleApplicationLifeCycle's package name
 * <p>
 * flags:
 * TINKER_ENABLE_ALL: support dex, lib and resource
 * TINKER_DEX_MASK: just support dex
 * TINKER_NATIVE_LIBRARY_MASK: just support lib
 * TINKER_RESOURCE_MASK: just support resource
 * <p>
 * loaderClass: define the tinker loader class, we can just use the default TinkerLoader
 * <p>
 * loadVerifyFlag: whether check files' md5 on the load time, defualt it is false.
 * <p>
 * Created by HuoGuangxu on 2016/10/8.
 */

@DefaultLifeCycle(application = "com.king.app.workhelper.app.WorkHelperApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class WorkHelperAppLike extends DefaultApplicationLike {

    public WorkHelperAppLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime,
                             Intent tinkerResultIntent, Resources[] resources, ClassLoader[] classLoader, AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent, resources, classLoader, assetManager);
    }

    @Override public void onCreate() {
        super.onCreate();
        Logger.init(AppConfig.LOG_TAG).setShowLog(BuildConfig.LOG_DEBUG).methodCount(1).hideThreadInfo();
    }

    @Override public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        TinkerManager.setTinkerApplicationLike(this);
        TinkerManager.initFastCrashProtect();
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log
        TinkerInstaller.setLogIml(new MyLogImp());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);
    }

    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }
}
