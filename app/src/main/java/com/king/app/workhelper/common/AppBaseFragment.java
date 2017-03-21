package com.king.app.workhelper.common;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import com.king.applib.base.BaseFragment;
import com.king.applib.util.StringUtil;

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

    protected void showToast(String toast) {
        if (isAdded() && !StringUtil.isNullOrEmpty(toast)) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showToast(@StringRes int resId) {
        if (isAdded()) {
            Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
        }
    }
}