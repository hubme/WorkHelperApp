package com.king.app.workhelper.common;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.king.applib.base.BasePageFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author VanceKing
 * @since 2017/2/9.
 */

public abstract class AppBasePageFragment extends BasePageFragment {
    private Unbinder mUnBinder;

    @Override
    protected void initContentView(View rootView) {
        super.initContentView(rootView);
        mUnBinder = ButterKnife.bind(this, mRootView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnBinder != null) {
            mUnBinder.unbind();
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
