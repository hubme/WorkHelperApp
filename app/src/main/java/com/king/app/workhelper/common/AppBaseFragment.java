package com.king.app.workhelper.common;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

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

    /**
     * 跳转到指定的Activity
     *
     * @param cls 跳转到的Activity
     */
    protected void startActivity(@NonNull Class<?> cls) {
        Intent intent = new Intent(getContext(), cls);
        getContext().startActivity(intent);
    }
}