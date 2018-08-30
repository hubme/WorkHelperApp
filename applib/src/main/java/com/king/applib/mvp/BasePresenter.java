package com.king.applib.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;

/**
 * @author VanceKing
 * @since 2017/12/10.
 */

public abstract class BasePresenter<V extends BaseContract.View> implements LifecycleObserver, BaseContract.Presenter<V> {
    private V view;

    @Override public void attachLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    @Override public void detachLifecycle(Lifecycle lifecycle) {
        lifecycle.removeObserver(this);
    }

    @Override public void attachView(V view) {
        this.view = view;
    }

    @Override public void detachView() {
        this.view = null;
    }

    @Override public V getView() {
        return view;
    }

    @Override public boolean isViewAttached() {
        return view != null;
    }
}
