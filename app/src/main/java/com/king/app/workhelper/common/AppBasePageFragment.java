package com.king.app.workhelper.common;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.king.applib.base.BasePageFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author huoguangxu
 * @since 2017/2/9.
 */

public abstract class AppBasePageFragment extends BasePageFragment {
    private Unbinder unbinder;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    protected void initContentView(View rootView) {
        super.initContentView(rootView);
        unbinder = ButterKnife.bind(this, mRootView);
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
