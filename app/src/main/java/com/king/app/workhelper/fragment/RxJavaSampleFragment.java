package com.king.app.workhelper.fragment;

import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;
import com.king.applib.util.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * RxJavaSample.http://blog.csdn.net/lzyzsd/article/details/41833541
 * Created by HuoGuangxu on 2016/11/4.
 */

public class RxJavaSampleFragment extends AppBaseFragment {
    @BindView(R.id.btn_send)
    public CheckedTextView mSendBtn;
    @BindView(R.id.et_input)
    public EditText mInputText;

    private Observable<String> mObservable;
    private Observable<String> mObservable2;

    private Subscriber<String> mSubscriber;
    private Subscriber<String> mSubscriber2;
    private Action1<String> mAction;
    private Action0 mActionComplete;
    private Action1<Throwable> mActionError;
    private PublishSubject<Float> mFloabSubject;
    private CompositeSubscription mCompositeSubscription;

    @Override public int getContentLayout() {
        return R.layout.fragment_rxjava_sample;
    }

    @Override protected void initContentView(View rootView) {
        super.initContentView(rootView);
    }

    @Override protected void initData() {
        super.initData();
    }

    @OnClick(R.id.btn_send)
    public void sendBtnClick() {
        testTakeOperator(2);
    }

    /**
     * 只打印前count个.count<=0不显示 >Observable的数量，显示全部.
     * If there are fewer than count it'll just stop early.
     */
    private void testTakeOperator(int count) {
        String nullStr = null;
        Observable.from(new String[]{"Hello", "World", " ", nullStr, "!"})
                .filter(new Func1<String, Boolean>() {
                    @Override public Boolean call(String s) {
                        return !StringUtil.isNullOrEmpty(s);
                    }
                })
                .take(count)//同first()或last()
                //doOnNext() allows us to add extra behavior each time an item is emitted, in this case saving the title.
                .doOnNext(new Action1<String>() {
                    @Override public void call(String s) {
                        Logger.i("save \"" + s + "\" on disk.");
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        Logger.i(s);
                    }
                });
    }

    /**
     * 把数组的item一个一个打印出来.过滤null或""
     */
    private void testFilterOperator() {
        String nullStr = null;
        Observable.from(new String[]{"Hello", "World", " ", nullStr, "!"})
                .filter(new Func1<String, Boolean>() {
                    @Override public Boolean call(String s) {
                        return !StringUtil.isNullOrEmpty(s);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        Logger.i(s);
                    }
                });
    }

    /*
    把数组的item一个一个打印出来
     */
    private void testFromOperator() {
        Observable.from(new String[]{"Hello", "World", "!"})
                .subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        Logger.i(s);
                    }
                });
    }

    private void testRngeOperator() {
        Observable.range(1, 4)
                .delay(new Func1<Integer, Observable<Integer>>() {
                    @Override public Observable<Integer> call(Integer integer) {
                        return null;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                        Logger.i("doOnSubscribe");
                    }
                })
                .subscribe(new Subscriber<Integer>() {//rx.exceptions.OnErrorNotImplementedException
                    @Override public void onCompleted() {
                        Logger.i("onCompleted");
                    }

                    @Override public void onError(Throwable e) {
                        Logger.i("onError");
                    }

                    @Override public void onNext(Integer integer) {
                        Logger.i(integer.toString());
                    }
                });
    }

    private void testCompositeSubscription() {
        //Subscriber组合
        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(Observable.error(new RuntimeException("testing"))
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                        Logger.i("doOnSubscribe");
                    }
                })
                .subscribe(new Observer<Object>() {
                    @Override public void onCompleted() {
                        Logger.i("onCompleted()");
                    }

                    @Override public void onError(Throwable e) {
                        Logger.i("onError()");
                    }

                    @Override public void onNext(Object o) {
                        Logger.i("onNext()");
                    }
                })

        );
    }

    private void initPublishSubject() {
        mFloabSubject = PublishSubject.create();
        Subscription mFloabSubjectSubscription = mFloabSubject.asObservable().subscribe(new Action1<Float>() {
            @Override public void call(Float aFloat) {
                Logger.i(aFloat.toString());
            }
        });
    }

    private void testPublishSubject() {
        mFloabSubject.onNext(111f);
    }

    private void test1() {
        Observable.just(true)
                .map(new Func1<Boolean, Boolean>() {
                    @Override public Boolean call(Boolean aBoolean) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return aBoolean;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override public void call(Boolean aBoolean) {
                        Logger.i(aBoolean + "");
                    }
                });
    }

    private void testScheduler() {
        Observable.just("Hello World!")
                .map(new Func1<String, Integer>() {
                    @Override public Integer call(String s) {
                        return s.length();
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override public void call(Integer integer) {
                        Logger.i(integer.toString());
                    }
                });
    }

    /*
    操作符就是为了解决对Observable对象的变换的问题，操作符用于在Observable和最终的Subscriber之间修改Observable发出的事件。
    1.map()操作符就是用于变换Observable对象的，map操作符返回一个Observable对象，
   这样就可以实现链式调用，在一个Observable对象上多次使用map操作符，最终将最简洁的数据传递给Subscriber对象。
     */
    private void testMapOperators() {
        Observable.just("Hello World!")
                .map(new Func1<String, String>() {
                    @Override public String call(String s) {
                        return s + " hahaha";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        Logger.i(s + "");
                    }
                });

        /*
        map操作符更有趣的一点是它不必返回Observable对象返回的类型，
        你可以使用map操作符返回一个发出新的数据类型的observable对象。
         */
        Observable.just("Hello World !")
                .map(new Func1<String, Integer>() {
                    @Override public Integer call(String s) {
                        return s.length();
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override public void call(Integer integer) {
                        Logger.i(integer.toString());
                    }
                });

        //多个map同事使用
        Observable.just("Hello World!")
                .map(new Func1<String, String>() {
                    @Override public String call(String s) {
                        return s + "哈哈";
                    }
                })
                .map(new Func1<String, Integer>() {
                    @Override public Integer call(String s) {
                        return s.length();
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override public void call(Integer integer) {
                        Logger.i(integer.toString());
                    }
                });
    }

    private void testObservable() {
        //Subscriber的回调函数会按注册时的顺序执行.
        // TODO: 2016/11/1 注册多个Observable，为什么只有第一个有用？
//        mObservable.subscribe(mSubscriber);
//        mObservable.subscribe(mSubscriber2);

        //有三个重装方法
        mObservable2.subscribe(mAction);
    }

    private void initObservable() {
        //创建被观察者
        mObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(Subscriber<? super String> subscriber) {
                //被观察者向被观察者发送消息
                subscriber.onNext("Hello World! 哈哈");
                //告诉观察者结束，观察者会在onCompleted()回调中收到消息。
                subscriber.onCompleted();
            }
        });
    }

    private void initSubscriber() {
        //创建观察者1
        mSubscriber = new Subscriber<String>() {
            @Override public void onCompleted() {
                Logger.i("mSubscriber#onCompleted()");
            }

            @Override public void onError(Throwable e) {
                Logger.i("mSubscriber#onError()");
            }

            @Override public void onNext(String s) {
                Logger.i("mSubscriber#onNext(): " + s);
            }
        };

        //创建观察者2
        mSubscriber2 = new Subscriber<String>() {
            @Override public void onCompleted() {
                Logger.i("mSubscriber2#onCompleted()");
            }

            @Override public void onError(Throwable e) {
                Logger.i("mSubscriber2#onError()");
            }

            @Override public void onNext(String s) {
                Logger.i("mSubscriber2#onNext(): " + s);
            }
        };

        //简化的Subscriber
        mAction = new Action1<String>() {
            @Override public void call(String s) {
                Logger.i(s);
            }
        };

        mActionError = new Action1<Throwable>() {
            @Override public void call(Throwable s) {
                Logger.i(s.toString());
            }
        };

        mActionComplete = new Action0() {
            @Override public void call() {
                Logger.i("complete()");
            }
        };
    }

    private void simpleObservable() {
        //简化的Observable,不关心OnComplete和OnError
        Observable.just("yes, hello too!").subscribe(new Action1<String>() {
            @Override public void call(String s) {
                Logger.i(s);
            }
        });
    }

    /*总结：
      1.Observable和Subscriber可以做任何事情
     Observable可以是一个数据库查询，Subscriber用来显示查询结果；Observable可以是屏幕上的点击事件，Subscriber用来响应点击事件；Observable可以是一个网络请求，Subscriber用来显示请求结果。

     2.Observable和Subscriber是独立于中间的变换过程的。
     在Observable和Subscriber中间可以增减任何数量的map。整个系统是高度可组合的，操作数据是一个很简单的过程。

     */
}
