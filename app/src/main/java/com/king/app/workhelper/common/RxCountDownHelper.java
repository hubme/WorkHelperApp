package com.king.app.workhelper.common;

import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * @author huoguangxu
 * @since 2017/7/6.
 */

// FIXME: 2017/7/6 取消后数字不重置
public class RxCountDownHelper {
    public static Disposable startCountDown(final TextView textView, final int duration, final String finishedText, final OnCountDownListener listener) {
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
            @Override public void onNext(Long aLong) {
                if (listener != null) {
                    listener.onCountDown(duration - aLong.intValue());
                }
            }

            @Override public void onError(Throwable e) {
                textView.setEnabled(true);
                textView.setText(finishedText);
            }

            @Override public void onComplete() {
                textView.setText(finishedText);
                textView.setEnabled(true);
            }
        };

        textView.setEnabled(false);
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(duration + 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
        return disposableObserver;
    }

    public interface OnCountDownListener {
        void onCountDown(int value);
    }
}
