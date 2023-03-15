package com.king.app.workhelper.common;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;

import com.king.app.workhelper.R;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.app.workhelper.rx.rxlife.components.RxLifeActivity;
import com.king.applib.util.ContextUtil;
import com.king.applib.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 应用基础Activity
 *
 * @author VanceKing
 * @since 2017/8/21
 */
public abstract class AppBaseActivity extends RxLifeActivity implements View.OnClickListener {
    protected static final String TAG = GlobalConstant.LOG_TAG;

    //子类可能没有引用common_header.xml
    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    protected void handleMessage(Message msg) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    public void onClick(View v) {

    }

    protected void initToolbar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.arrow_left_blue);
        toolbar.setTitle(getActivityTitle());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavigationClicked();
            }
        });
        //setSupportActionBar(mToolbar);
    }

    protected void onNavigationClicked() {
        finish();
    }

    protected String getActivityTitle() {
        return getString(R.string.test);
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

    protected static void logMessage(String message) {
        Log.i(GlobalConstant.LOG_TAG, message);
    }
}
