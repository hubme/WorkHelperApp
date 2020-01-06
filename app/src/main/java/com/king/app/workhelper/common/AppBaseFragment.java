package com.king.app.workhelper.common;

import android.view.View;

import com.king.app.workhelper.rx.rxlife.components.RxLifeFragment;

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
}