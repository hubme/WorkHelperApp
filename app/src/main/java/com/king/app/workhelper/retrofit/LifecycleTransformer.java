package com.king.app.workhelper.retrofit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;

/**
 * @author VanceKing
 * @since 2017/8/8 0008.
 */

public class LifecycleTransformer<T> implements SingleTransformer<T, T> {
    private final Observable<?> mObservable;

    public LifecycleTransformer(Observable<?> observable) {
        mObservable = observable;
    }

    @Override
    public SingleSource<T> apply(@NonNull Single<T> upstream) {
        return upstream.takeUntil(mObservable.firstOrError());
    }
}
