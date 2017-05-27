package com.king.app.workhelper.retrofit.subscriber;

import com.google.gson.stream.MalformedJsonException;
import com.king.app.workhelper.retrofit.callback.OnResponseCallback;
import com.king.app.workhelper.retrofit.exception.ApiException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;

/**
 * 封装返回结果的订阅者
 *
 * @author VanceKing
 * @since 2017/5/27.
 */

public class HttpSubscriber<T> implements Observer<T> {
    private OnResponseCallback<T> mResponseCallback;
    private Disposable mDisposable;

    public HttpSubscriber(OnResponseCallback<T> callback) {
        mResponseCallback = callback;
    }

    @Override public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override public void onNext(T t) {
        if (mResponseCallback != null) {
            mResponseCallback.onSuccess(t);
        }
    }

    @Override public void onError(Throwable t) {
        if (t instanceof CompositeException) {
            CompositeException compositeE=(CompositeException)t;
            for (Throwable throwable : compositeE.getExceptions()) {
                if (throwable instanceof SocketTimeoutException) {
                    mResponseCallback.onFailure(ApiException.CODE_TIMEOUT,ApiException.SOCKET_TIMEOUT_EXCEPTION);
                } else if (throwable instanceof ConnectException) {
                    mResponseCallback.onFailure(ApiException.CODE_UNCONNECTED,ApiException.CONNECT_EXCEPTION);
                } else if (throwable instanceof UnknownHostException) {
                    mResponseCallback.onFailure(ApiException.CODE_UNCONNECTED,ApiException.CONNECT_EXCEPTION);
                } else if (throwable instanceof MalformedJsonException) {
                    mResponseCallback.onFailure(ApiException.CODE_MALFORMED_JSON,ApiException.MALFORMED_JSON_EXCEPTION);
                }
            }
        } else if (t instanceof ApiException){
            mResponseCallback.onFailure(((ApiException) t).code, ((ApiException) t).msg);
        } 
    }

    @Override public void onComplete() {

    }

    public void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
