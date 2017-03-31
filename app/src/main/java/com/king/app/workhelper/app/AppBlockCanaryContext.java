package com.king.app.workhelper.app;

import com.github.moduth.blockcanary.BlockCanaryContext;
import com.king.app.workhelper.BuildConfig;

/**
 * @author huoguangxu
 * @since 2017/3/31.
 */

public class AppBlockCanaryContext extends BlockCanaryContext {
    @Override
    public boolean displayNotification() {
        return BuildConfig.LOG_DEBUG;
    }

    @Override public int provideBlockThreshold() {
        return 500;
    }
}
