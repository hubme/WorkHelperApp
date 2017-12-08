package com.king.applib.ui.loading;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 加载View。
 *
 * @author VanceKing
 * @since 2017/12/6.
 */

public class FlowerLoadingView extends View {
    private LoadingDrawable mLoadingDrawable;

    public FlowerLoadingView(Context context) {
        this(context, null);
    }

    public FlowerLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowerLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLoadingDrawable = new LoadingDrawable(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(mLoadingDrawable);
        } else {
            setBackgroundDrawable(mLoadingDrawable);
        }
    }

    //1.Activity onVisibilityChanged -> onAttachedToWindow
    //2.Fragment onAttachedToWindow
    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startLoading();
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoading();
    }
    
    //3.按home键进入后台会调用此方法。
    @Override protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startLoading();
        } else {
            stopLoading();
        }
    }

    public void startLoading() {
        if (!mLoadingDrawable.isRunning()) {
            mLoadingDrawable.start();
        }
    }

    public void stopLoading() {
        if (mLoadingDrawable.isRunning()) {
            mLoadingDrawable.stop();
        }
    }

    public boolean isRunning() {
        return mLoadingDrawable.isRunning();
    }
}
