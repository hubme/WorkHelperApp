package com.king.app.workhelper;

import android.os.SystemClock;
import android.util.Log;

import com.king.applib.log.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;

/**
 * http://www.jianshu.com/p/e3a4dbd748fd
 *
 * @author huoguangxu
 * @since 2017/6/22.
 */

public class RxJavaSampleTest extends BaseTestCase {
    private CompositeDisposable mCompositeDisposable;

    @Override protected void setUp() throws Exception {
        super.setUp();
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override protected void tearDown() throws Exception {
        super.tearDown();
        mCompositeDisposable.dispose();

        if (subscribe != null) {
            if (!subscribe.isDisposed()) {
                subscribe.dispose();
                Logger.i("没有 dispose");
            } else {
                Logger.i("已经 dispose");
            }
        }
    }

    public void testCreateOperator() throws Exception {
        Observable.create((ObservableOnSubscribe<String>) e -> {
            e.onNext("aaa");
            e.onNext("bbb");
            e.onNext("ccc");
            e.onComplete();
//                e.onError(new RuntimeException("哈哈哈"));

        }).subscribe(new DisposableObserver<String>() {//没办法主动取消
            @Override public void onNext(String s) {
                Logger.i("onNext: " + s);
            }

            @Override public void onError(Throwable e) {
                Logger.i("onError: " + e.toString());
            }

            @Override public void onComplete() {
                Logger.i("onComplete");
            }
        });
    }

    private Disposable subscribe;

    public void testCreateOperator2() throws Exception {
        subscribe = Observable.create((ObservableOnSubscribe<String>) e -> {
            e.onNext("aaa");
            e.onNext("bbb");
//            subscribe.dispose();//会抛异常
            e.onNext("ccc");
            e.onComplete();//调用onComplete会自动dispose.
        }).subscribe(s -> Logger.i("results: " + s));

//        mCompositeDisposable.add(subscribe);
    }

    //避免Observable出现错误时异常退出.
    private <T> Observable<T> attachErrorHandler(Observable<T> obs) {
        return obs.onErrorResumeNext(throwable -> {
            Logger.i("an error happened and resume.");
            return Observable.empty();
        });
    }

    public void testCreateOperator3() throws Exception {
        Observable<String> stringObservable = Observable.create(e -> {
            e.onNext("aaa");
            e.onNext("bbb");
            subscribe.dispose();//会抛异常
            e.onNext("ccc");
        });

        attachErrorHandler(stringObservable).subscribe(s -> Logger.i("results: " + s));
    }

    public void testSingleOperator() throws Exception {
        Single.just("哈哈哈")
                .subscribe(new DisposableSingleObserver<String>() {
                    @Override public void onSuccess(@NonNull String s) {
                        Logger.i("onSuccess: " + s);
                    }

                    @Override public void onError(@NonNull Throwable e) {
                        Logger.i("onError: " + e.toString());
                    }
                });

    }

    public void testJustOperator() throws Exception {
        Observable.just("哈哈哈").subscribe(s -> Logger.i(s));
    }

    public void testFromArrayOperator() throws Exception {
        String[] texts = new String[]{"aaa", "bbb"};
        Observable.fromArray(texts).subscribe(s -> Logger.i(s));
    }

    public void testInterval() throws Exception {
        Flowable.interval(1, TimeUnit.SECONDS)
                .onBackpressureDrop()  //加上背压策略
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> result(aLong.toString()));

    }

    public void testDoOnCancelSubscribe() throws Exception {
        Flowable.just(1, 2, 3)
                .doOnCancel(new Action() {
                    @Override public void run() throws Exception {
                        Logger.i("cancel");
                    }
                })
                .take(2)//take新操符会取消后面那些还未被发送的事件，因而会触发doOnCancel
                .subscribe(new Consumer<Integer>() {
                    @Override public void accept(@NonNull Integer integer) throws Exception {
                        Logger.i(integer.toString());
                    }
                });
    }

    public void testTimeInterval() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                for (int i = 0; i < 3; i++) {
                    e.onNext(i + "");
//                    Thread.sleep(1000);
                    SystemClock.sleep(1000);
                }
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .timeInterval()
                .subscribe(new Consumer<Timed<String>>() {
                    @Override public void accept(@NonNull Timed<String> stringTimed) throws Exception {
                        result(String.valueOf(stringTimed.time()));
                    }
                });
    }

    private void result(String result) {
        Logger.i("onNext: " + result);
    }

    private void error(Throwable throwable) {
        Logger.e("onError: " + Log.getStackTraceString(throwable));
    }

    private void complete() {
        Logger.i("onComplete");
    }

}
