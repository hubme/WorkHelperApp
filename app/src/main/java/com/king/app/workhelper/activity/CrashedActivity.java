package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.OnClick;

/**
 * 应用crash后显示给用户的界面
 * Created by HuoGuangxu on 2016/12/13.
 */

public class CrashedActivity extends AppBaseActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_crashed;
    }

    @OnClick(R.id.btn_restart_app)
    public void restartApp() {
        startActivity(HomeActivity.class);
        finish();
        killCurrentProcess();
    }

    private static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
