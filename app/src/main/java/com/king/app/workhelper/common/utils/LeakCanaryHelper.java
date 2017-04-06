package com.king.app.workhelper.common.utils;

import android.app.Application;

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
    public static RefWatcher getDisabledRefWatcher() {
        return RefWatcher.DISABLED;
    }

    //leakcanary/leakcanary-android/src/main/java/com/squareup/leakcanary/AndroidExcludedRefs.java
    public static RefWatcher getEnableRefWatcher(Application app) {
//        return LeakCanary.install(app);
        ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults()
                .instanceField("android.animation.LayoutTransition$1", "val$parent")
                .staticField("android.app.Instrumentation", "mContext")
                .reason("Instrumentation would leak com.android.app.Instrumentation#mContext in Meizu FlymeOS")
                .build();
        return LeakCanary.refWatcher(app)
                .listenerServiceClass(DisplayLeakService.class)
                .excludedRefs(excludedRefs)
                .buildAndInstall();
    }

    private void initLeakCanary() {
            /*LeakCanary.enableDisplayLeakActivity(this);
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
                    if (activity instanceof SplashActivity) return;
                    mRefWatcher.watch(activity);
                }

                @Override
                public void onActivityResumed(Activity activity) {

                }
            });*/
    }
}
