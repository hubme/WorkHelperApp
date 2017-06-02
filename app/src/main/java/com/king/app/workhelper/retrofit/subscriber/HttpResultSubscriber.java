package com.king.app.workhelper.retrofit.subscriber;


import com.google.gson.stream.MalformedJsonException;
import com.king.app.workhelper.retrofit.HttpResponseCode;
import com.king.app.workhelper.retrofit.exception.ApiException;
import com.king.app.workhelper.retrofit.model.HttpResults;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.CancellationException;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * 网络回调订阅者.
 *
 * @author HuoGuangXu
 * @since 2017/5/30.
 */

public abstract class HttpResultSubscriber<T> implements SingleObserver<HttpResults<T>> {
    private Disposable mDisposable;

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onSuccess(HttpResults<T> httpResults) {
        if (httpResults != null) {
            if (httpResults.code == HttpResponseCode.SUCCESS) {
                onSuccess(httpResults.results, httpResults.desc != null ? httpResults.desc : "");
            } else {
                onFailure(httpResults.code, httpResults.desc);
            }
        } else {
            onFailure(-1, "results is null");
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

    public abstract void onSuccess(T t, String msg);

    public abstract void onFailure(int errorCode, String msg);

    public void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
