package com.king.app.workhelper.activity;

import android.content.Intent;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.fragment.EntryFragment;

/**
 * 测试入口界面
 * Created by HuoGuangxu on 2016/11/10.
 */

public class HomeActivity extends AppBaseActivity {
    private long exitTime;

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
//        String value = getStringExtra("aaa", "000");
//        Logger.i(value);
    }

    @Override
    protected String getActivityTitle() {
        return "测试";
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 1500) {
                ToastUtil.showShort(R.string.one_more_exit);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/
}
