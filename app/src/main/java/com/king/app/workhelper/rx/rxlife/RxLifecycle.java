package com.king.app.workhelper.rx.rxlife;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

/**
 * @author VanceKing
 * @since 2017/8/8.
 */

public class RxLifecycle {
    private RxLifecycle() {
        throw new IllegalStateException("No instances");
    }

    public static <T, R> LifecycleTransformer<T> bindUntilEvent(final Observable<R> lifecycle, R event) {
        return bind(takeUntilEvent(lifecycle, event));
    }

    public static <T, R> LifecycleTransformer<T> bind(final Observable<R> lifecycle) {
        return new LifecycleTransformer<>(lifecycle);
    }

    private static <R> Observable<R> takeUntilEvent(final Observable<R> lifecycle, final R event) {
        return lifecycle.filter(new Predicate<R>() {
            @Override
            public boolean test(R lifecycleEvent) throws Exception {
                return lifecycleEvent.equals(event);
            }
        });
    }
}
