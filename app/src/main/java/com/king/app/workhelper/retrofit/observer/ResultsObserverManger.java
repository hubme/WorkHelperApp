package com.king.app.workhelper.retrofit.observer;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 用于统一取消订阅,避免内存泄露.
 *
 * @author VanceKing
 * @since 2017/6/2.
 */

public class ResultsObserverManger {
    private static final CompositeDisposable COMPOSITE_DISPOSABLE = new CompositeDisposable();

    public static void addDisposable(Disposable d) {
        if (d != null) {
            COMPOSITE_DISPOSABLE.add(d);
        }
    }

    public static void removeDisposable(Disposable d) {
        if (d != null) {
            COMPOSITE_DISPOSABLE.remove(d);
        }
    }

    public static void disposeAll() {
        if (COMPOSITE_DISPOSABLE.size() <= 0) {
            return;
        }
        COMPOSITE_DISPOSABLE.clear();
    }
}
