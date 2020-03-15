package com.king.app.workhelper.retrofit.observer;


import androidx.annotation.CallSuper;

import com.google.gson.stream.MalformedJsonException;
import com.king.app.workhelper.retrofit.HttpResponseCode;
import com.king.app.workhelper.retrofit.exception.ApiException;
import com.king.app.workhelper.retrofit.model.HttpResults;
import com.king.applib.log.Logger;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.CancellationException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 网络回调订阅者.
 *
 * @author VanceKing
 * @since 2017/5/30.
 */

public abstract class HttpResultObserver<T> implements Observer<HttpResults<T>> {
    private Disposable mDisposable;

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
//        ResultsObserverManger.addDisposable(d);
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

    //The Observable will not call this method if it calls onError.
    @Override
    public void onComplete() {
    }

    public abstract void onSuccess(T t, String msg);

    @CallSuper
    public void onFailure(int errorCode, String msg) {
        Logger.e("errorCode: " + errorCode + ", errorMsg: " + msg);
    }

    public void dispose() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
