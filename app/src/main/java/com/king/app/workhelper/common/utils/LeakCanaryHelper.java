package com.king.app.workhelper.common.utils;

import android.app.Application;

import com.king.app.workhelper.BuildConfig;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.ExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * LeakCanary帮助类
 *
 * @author VanceKing
 * @since 2017/4/6.
 */
public class LeakCanaryHelper {
    private static RefWatcher mRefWatcher;

    //leakcanary/leakcanary-android/src/main/java/com/squareup/leakcanary/AndroidExcludedRefs.java
    public static RefWatcher enableRefWatcher(Application app) {
//        return LeakCanary.install(app);
        if (mRefWatcher == null) {
            ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults()
                    .instanceField("android.animation.LayoutTransition$1", "val$parent")
                    .staticField("android.app.Instrumentation", "mContext")
                    .reason("Instrumentation would leak com.android.app.Instrumentation#mContext in Meizu FlymeOS")
                    .build();
            mRefWatcher = LeakCanary.refWatcher(app)
                    .listenerServiceClass(DisplayLeakService.class)
                    .excludedRefs(excludedRefs)
                    .buildAndInstall();
        }
        return mRefWatcher;
    }

    public static void initLeakCanary(Application application) {
        if (!LeakCanary.isInAnalyzerProcess(application)) {
            enableRefWatcher(application);
        }
    }

    public static RefWatcher getRefWatcher(Application application) {
        return BuildConfig.DEBUG ? LeakCanaryHelper.enableRefWatcher(application) :
                RefWatcher.DISABLED;
    }

}
