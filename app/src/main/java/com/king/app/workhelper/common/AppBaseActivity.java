package com.king.app.workhelper.common;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.applib.base.BaseActivity;
import com.king.applib.util.ContextUtil;
import com.king.applib.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 应用基础Activity
 * Created by VanceKing on 2016/9/29.
 */

public abstract class AppBaseActivity extends BaseActivity implements View.OnClickListener {
    //子类可能没有引用common_header.xml
    @Nullable @BindView(R.id.toolbar)
    protected Toolbar mToolbar;
    protected com.king.applib.base.WeakHandler mHandler = new WeakHandler(AppBaseActivity.this);
    
    private static class WeakHandler extends com.king.applib.base.WeakHandler<AppBaseActivity>{

        private WeakHandler(AppBaseActivity target) {
            super(target);
        }

        @Override public void handle(AppBaseActivity target, Message msg) {
            target.handleMessage(msg);
        }
    }

    protected void handleMessage(Message msg){
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getEventBus().register(this);
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        ButterKnife.bind(this);
        
        if (mToolbar != null) {
            initToolbar(mToolbar);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getEventBus().unregister(this);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onClick(View v) {

    }

    protected void initToolbar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.arrow_left_blue);
        toolbar.setTitle(getActivityTitle());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //setSupportActionBar(mToolbar);
    }

    protected String getActivityTitle() {
        return getString(R.string.test);
    }

    /**
     * View设置OnClick事件
     */
    protected void setViewClickListeners(@IdRes int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
    }

    /**
     * View设置OnClick事件
     */
    protected void setViewClickListeners(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
    }

    /**
     * 默认采用short toast
     *
     * @param toast str to toast
     */
    protected void showToast(String toast) {
        if (!StringUtil.isNullOrEmpty(toast)) {
            Toast.makeText(ContextUtil.getAppContext(), toast, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showToast(String toast, String defaultToast) {
        if (!StringUtil.isNullOrEmpty(toast)) {
            Toast.makeText(ContextUtil.getAppContext(), toast, Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isNullOrEmpty(defaultToast)) {
            Toast.makeText(ContextUtil.getAppContext(), defaultToast, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 默认采用short toast
     *
     * @param toast str to toast
     */
    protected void showToast(@StringRes int toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String toast, int resId) {
        if (!StringUtil.isNullOrEmpty(toast)) {
            Toast.makeText(ContextUtil.getAppContext(), toast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ContextUtil.getAppContext(), resId, Toast.LENGTH_SHORT).show();
        }
    }
}
