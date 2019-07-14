package com.king.app.workhelper;

import com.king.applib.log.Logger;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author VanceKing
 * @since 2017/6/4 0004.
 */

/*
三类观察者模式：
1.Observable/Observer.(不支持背压)
2.Flowable/Subscriber.(支持背压)
3.Single/SingleObserver Completable/CompletableObserver Maybe/MaybeObserver(前两者的复合体)
 */
public class RxJavaTest extends BaseAndroidJUnit4Test{

    public void testNormalUsage() throws Exception {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onComplete();
                //e.onError(new RuntimeException("我报错啦"));
            }
        });

        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                //这是新加入的方法，在订阅后发送数据之前，
                //回首先调用这个方法，而Disposable可用于取消订阅
                Logger.i("准备好，我要出招了");
            }

            @Override
            public void onNext(Integer integer) {
                Logger.i(integer.toString());
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.toString());
            }

            @Override
            public void onComplete() {
                Logger.i("onComplete");
            }
        };

        observable.subscribe(observer);
    }

    /*
    Flowable是支持背压的.
    一般而言，上游的被观察者会响应下游观察者的数据请求，下游调用request(n)来告诉上游发送多少个数据。
    这样避免了大量数据堆积在调用链上，使内存一直处于较低水平。
     */
    public void testBackPressure() throws Exception {
        Flowable.range(0, 10)
                .subscribe(new Subscriber<Integer>() {
                    private Subscription sub;

                    @Override
                    public void onSubscribe(Subscription s) {
                        //当订阅后，会首先调用这个方法，其实就相当于onStart()，尽量在request之前进行初始化.
                        //传入的Subscription s参数可以用于请求数据或者取消订阅.
                        //不调用request不会发射数据.
                        Logger.i("start");
                        sub = s;
                        sub.request(1);
                        Logger.i("end");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Logger.i(integer.toString());
                        sub.request(2);
                        if (integer == 7) {
                            sub.cancel();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Logger.i(t.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.i("onComplete");
                    }
                });
    }

    public void testBackPressure2() throws Exception {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Logger.i("start");
                        s.request(6);
                        Logger.i("end");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Logger.i(integer.toString());
                    }

                    @Override
                    public void onError(Throwable t) {
                        Logger.i(t.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.i("onComplete");
                    }
                });
    }

    //相比较于Maybe Operator无onComplete方法.
    public void testSingleOperator() throws Exception {
        Single.just(false)
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        Logger.i(aBoolean.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    //相比较于Maybe Operator无onSuccess方法.
    public void testCompletableOperator() throws Exception {
    }

    //这种观察者模式并不用于发送大量数据，而是发送单个数据
    public void testMaybeOperator() throws Exception {
        Maybe.just(false)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        Logger.i(aBoolean.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public void testDoOnDisposeSubscribe() throws Exception {
        Single.just(true)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Logger.i("disposed");
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        Logger.i(aBoolean.toString());
                    }
                });
    }
}
