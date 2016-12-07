package com.king.app.workhelper.activity;

import android.content.Intent;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.fragment.EntryFragment;
import com.king.applib.log.Logger;

/**
 * 测试入口界面
 * Created by HuoGuangxu on 2016/11/10.
 */

public class HomeActivity extends AppBaseActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
        super.initData();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.layout_container, new EntryFragment())
                .commit();
    }

    @Override
    public void getIntentData(Intent intent) {
        super.getIntentData(intent);
        String value = getStringExtra("aaa", "000");
        Logger.i(value);
    }

    @Override
    protected int getTitleResource() {
        return R.string.about;
    }
}
