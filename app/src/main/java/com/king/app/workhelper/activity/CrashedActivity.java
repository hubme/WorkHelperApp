package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.OnClick;

/**
 * 应用crash后显示给用户的界面
 * Created by HuoGuangxu on 2016/12/13.
 */
// HGX: 2016/12/17 应不应该放到单独的进程中.单独进程更合理一些。
public class CrashedActivity extends AppBaseActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_crashed;
    }

    @OnClick(R.id.btn_restart_app)
    public void restartApp() {
        startActivity(HomeActivity.class);
        finish();
    }
}
