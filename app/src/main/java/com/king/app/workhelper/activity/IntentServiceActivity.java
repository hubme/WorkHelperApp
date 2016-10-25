package com.king.app.workhelper.activity;

import android.content.Intent;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.service.AppDownloadService;
import com.king.applib.log.Logger;
import com.king.applib.util.NetworkUtil;

import butterknife.OnClick;

/**
 * IntentServcie测试类
 * Created by HuoGuangxu on 2016/10/21.
 */

public class IntentServiceActivity extends AppBaseActivity {
    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.hello_world)
    public void startDownloadApk() {
        Logger.i(NetworkUtil.isNetworkAvailable(this) + "");
        Intent intent = new Intent(this, AppDownloadService.class);
        startService(intent);
    }
}
