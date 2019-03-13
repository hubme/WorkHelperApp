package com.king.app.workhelper.common;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import com.king.app.workhelper.app.WorkHelperApp;
import com.king.app.workhelper.common.utils.LeakCanaryHelper;
import com.king.app.workhelper.rx.rxlife.components.RxLifeFragment;
import com.king.applib.util.ContextUtil;
import com.king.applib.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * App Fragment 基类。
 *
 * @author VanceKing
 * @since 2016/9/29.
 */
public abstract class AppBaseFragment extends RxLifeFragment {
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
        LeakCanaryHelper.getRefWatcher(WorkHelperApp.getApplication()).watch(this);
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