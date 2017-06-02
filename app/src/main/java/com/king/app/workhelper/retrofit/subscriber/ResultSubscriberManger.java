package com.king.app.workhelper.retrofit.subscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于统一取消订阅,避免内存泄露.
 *
 * @author huoguangxu
 * @since 2017/6/2.
 */

public class ResultSubscriberManger {
    private static final List<HttpResultSubscriber> SUBSCRIBERS = new ArrayList<>();

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
    
    public static List<HttpResultSubscriber> getSubscribers(){
        return SUBSCRIBERS;
    }
}
