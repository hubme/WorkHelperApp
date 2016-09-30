package com.king.app.workhelper.common;

import com.king.applib.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * App基础Fragment
 * Created by VanceKing on 2016/9/29.
 */

public abstract class AppBaseFragment extends BaseFragment {
    private Unbinder unbinder;

    @Override
    protected void initContentView() {
        unbinder = ButterKnife.bind(this, mRootView);
        super.initContentView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}