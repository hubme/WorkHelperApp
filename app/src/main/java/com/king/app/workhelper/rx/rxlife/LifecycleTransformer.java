package com.king.app.workhelper.rx.rxlife;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;

/**
 * @author VanceKing
 * @since 2017/8/8.
 */

public class LifecycleTransformer<T> implements ObservableTransformer<T, T>, FlowableTransformer<T, T>,
        SingleTransformer<T, T>, MaybeTransformer<T, T> {
    private final Observable<?> mObservable;

    public LifecycleTransformer(Observable<?> observable) {
        mObservable = observable;
    }

    @Override public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
        return upstream.takeUntil(mObservable);
    }

    @Override
    public SingleSource<T> apply(@NonNull Single<T> upstream) {
        return upstream.takeUntil(mObservable.firstOrError());//网络请求未返回就主动取消时，会回调fail方法.
    }

    @Override public Publisher<T> apply(@NonNull Flowable<T> upstream) {
        return upstream.takeUntil(mObservable.toFlowable(BackpressureStrategy.LATEST));
    }

    @Override public MaybeSource<T> apply(@NonNull Maybe<T> upstream) {
        return upstream.takeUntil(mObservable.firstElement());
    }
}
