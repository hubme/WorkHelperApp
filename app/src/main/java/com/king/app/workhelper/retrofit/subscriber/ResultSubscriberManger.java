package com.king.app.workhelper.retrofit.subscriber;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 用于统一取消订阅,避免内存泄露.
 *
 * @author huoguangxu
 * @since 2017/6/2.
 */

public class ResultSubscriberManger {
    private static final List<HttpResultSubscriber> SUBSCRIBERS = new ArrayList<>();
    private static final CompositeDisposable COMPOSITE_DISPOSABLE = new CompositeDisposable();

    public static <T> HttpResultSubscriber<T> create(HttpResultSubscriber<T> subscriber) {
        SUBSCRIBERS.add(subscriber);
        return subscriber;
    }

    public static void unSubscribe() {
        if (SUBSCRIBERS.isEmpty()) {
            return;
        }
        SUBSCRIBERS.stream().filter(subscriber -> subscriber != null).forEach(subscriber -> {
            subscriber.unSubscribe();
            SUBSCRIBERS.remove(subscriber);
        });
    }

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

    public static List<HttpResultSubscriber> getSubscribers() {
        return SUBSCRIBERS;
    }
}
