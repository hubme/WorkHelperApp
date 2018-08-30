package com.king.applib.mvp;

import android.arch.lifecycle.Lifecycle;

/**
 * https://hackernoon.com/mvp-android-architecture-components-aef55e15bfe3
 * https://github.com/armcha/MVP-Architecture-Components
 *
 * @author VanceKing
 * @since 2018/8/30.
 */
public interface BaseContract {
    interface View {

    }

    interface Presenter<V extends BaseContract.View> {
        void attachLifecycle(Lifecycle lifecycle);

        void detachLifecycle(Lifecycle lifecycle);

        void attachView(V view);

        void detachView();

        V getView();

        boolean isViewAttached();

        void onPresenterDestroy();
    }
}
