package com.king.app.workhelper.retrofit.subscriber;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 用于统一取消订阅,避免内存泄露.
 *
 * @author VanceKing
 * @since 2017/6/2.
 */

public class ResultsSubscriberManger {
    private static final List<Subscription> SUBSCRIPTIONS = new ArrayList<>();

    public static void add(Subscription subscription) {
        if (subscription != null) {
            SUBSCRIPTIONS.add(subscription);
        }
    }

    /** 注意：会取消当前所有的网络请求 */
    public static void cancelAllSubscriptions() {
        if (SUBSCRIPTIONS.isEmpty()) {
            return;
        }
        Iterator<Subscription> iterator = SUBSCRIPTIONS.iterator();
        while (iterator.hasNext()) {
            final Subscription subscription = iterator.next();
            if (subscription != null) {
                subscription.cancel();
                iterator.remove();
            }
        }
    }

    public static List<Subscription> getAllSubscriptions() {
        return SUBSCRIPTIONS;
    }
}
