package com.king.app.workhelper.retrofit.subscriber;

import com.google.gson.stream.MalformedJsonException;
import com.king.app.workhelper.retrofit.HttpResponseCode;
import com.king.app.workhelper.retrofit.exception.ApiException;
import com.king.app.workhelper.retrofit.model.HttpResults;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.CancellationException;

/**
 * 接口数据订阅者.
 *
 * @author VanceKing
 * @since 2017/6/4.
 */

public abstract class HttpResultsSubscriber<T> implements Subscriber<HttpResults<T>> {
    private Subscription mSubscription;

    @Override
    public void onSubscribe(Subscription s) {
        mSubscription = s;
        s.request(Long.MAX_VALUE);
        ResultsSubscriberManger.add(s);
    }

    @Override
    public void onNext(HttpResults<T> httpResults) {
        if (httpResults != null) {
            if (httpResults.code == HttpResponseCode.SUCCESS) {
                onSuccess(httpResults.results, httpResults.desc != null ? httpResults.desc : "");
            } else {
                onFailure(httpResults.code, httpResults.desc);
            }
        } else {
            onFailure(ApiException.CODE_ERROR_DEFAULT, ApiException.MSG_RESULTS_NULL);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e != null) {
            if (!(e instanceof CancellationException)) {
                if (e instanceof SocketTimeoutException) {
                    onFailure(ApiException.CODE_ERROR_TIMEOUT, ApiException.MSG_ERROR_SOCKET_TIMEOUT);
                } else if (e instanceof ConnectException) {
                    onFailure(ApiException.CODE_ERROR_UNCONNECTED, ApiException.MSG_ERROR_CONNECT);
                } else if (e instanceof UnknownHostException) {
                    onFailure(ApiException.CODE_ERROR_UNKNOWN_HOST, ApiException.MSG_ERROR_UNKNOWN_HOST);
                } else if (e instanceof MalformedJsonException) {
                    onFailure(ApiException.CODE_ERROR_MALFORMED_JSON, ApiException.MSG_ERROR_MALFORMED_JSON);
                } else {
                    onFailure(ApiException.CODE_ERROR_DEFAULT, e.getMessage());
                }
            }
        } else {
            onFailure(ApiException.CODE_ERROR_DEFAULT, ApiException.MSG_ERROR_EMPTY);
        }
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(T t, String msg);

    public abstract void onFailure(int errorCode, String msg);

    public Subscription getSubscription() {
        return mSubscription;
    }

    public void cancelSubscription() {
        if (mSubscription != null) {
            mSubscription.cancel();
        }
    }
}
