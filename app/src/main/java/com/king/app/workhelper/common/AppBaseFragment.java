package com.king.app.workhelper.common;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import com.king.app.workhelper.app.WorkHelperApp;
import com.king.applib.base.BaseFragment;
import com.king.applib.util.ContextUtil;
import com.king.applib.util.StringUtil;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * App基础Fragment
 * Created by VanceKing on 2016/9/29.
 */

public abstract class AppBaseFragment extends BaseFragment {
    private Unbinder unbinder;

    @Override
    protected void initContentView(View rootView) {
        super.initContentView(rootView);
        unbinder = ButterKnife.bind(this, mRootView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //检测Fragment内存泄露
        RefWatcher refWatcher = WorkHelperApp.getRefWatcher();
        refWatcher.watch(this);
    }

    protected void showToast(String toast) {
        if (isAdded() && !StringUtil.isNullOrEmpty(toast)) {
            Toast.makeText(ContextUtil.getAppContext(), toast, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showToast(@StringRes int resId) {
        if (isAdded()) {
            Toast.makeText(ContextUtil.getAppContext(), resId, Toast.LENGTH_SHORT).show();
        }
    }
}