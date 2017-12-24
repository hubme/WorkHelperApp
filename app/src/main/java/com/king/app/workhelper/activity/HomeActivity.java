package com.king.app.workhelper.activity;

import android.support.v7.app.AppCompatDelegate;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.fragment.EntryFragment;

/**
 * 测试入口界面
 * Created by HuoGuangxu on 2016/11/10.
 */

public class HomeActivity extends AppBaseActivity {
    private long exitTime;

    //解决Button无法使用Vector的问题
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

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
    protected String getActivityTitle() {
        return "测试";
    }

    @Override protected void onNavigationClicked() {
        moveTaskToBack(true);
    }

    @Override public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            //isTaskRoot();//用来判断该Activity是否为任务栈中的根Activity，即启动应用的第一个Activity
            moveTaskToBack(true);//将应用退到后台，不是finish
        }
    }

    // TODO: 2017/7/16 Fragment返回
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
