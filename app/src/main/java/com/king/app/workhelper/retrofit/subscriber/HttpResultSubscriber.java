package com.king.app.workhelper.retrofit.subscriber;

import com.king.app.workhelper.retrofit.HttpResponseCode;
import com.king.app.workhelper.retrofit.model.HttpResults;
import com.king.applib.log.Logger;

import java.util.concurrent.CancellationException;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * @author VanceKing
 * @since 2017/5/30 0030.
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
                onSuccessFull(httpResults.results);
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
                Logger.i(e.toString());
                if (e.getMessage() != null) {
                    onFailure(1001, e.getMessage());
                } else {
                    onFailure(1002, "empty error msg.");
                }
            }
        } else {
            onFailure(1002, "empty error msg.");
        }
    }

    public abstract void onSuccessFull(T t);

    public abstract void onFailure(int errorCode, String msg);

    public void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
