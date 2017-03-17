package com.king.app.workhelper.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.model.entity.Course;
import com.king.app.workhelper.model.entity.Student;
import com.king.applib.log.Logger;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
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

    @Override
    public int getContentLayout() {
        return R.layout.fragment_rxjava_sample;
    }

    @Override
    protected void initContentView(View rootView) {
        super.initContentView(rootView);
    }

    @Override
    protected void initData() {
        super.initData();
        initSubscriber();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscriber != null) {
            mSubscriber.unsubscribe();
        }
    }

    /*
    create() 方法是 RxJava 最基本的创造事件序列的方法。
    
    这里传入了一个 OnSubscribe 对象作为参数。
    OnSubscribe 会被存储在返回的 Observable 对象中，它的作用相当于一个计划表，当 Observable 被订阅的时候，
    OnSubscribe 的 call() 方法会自动被调用，事件序列就会依照设定依次触发（对于上面的代码，
    就是观察者Subscriber 将会被调用三次 onNext() 和一次 onCompleted()）。
    这样，由被观察者调用了观察者的回调方法，就实现了由被观察者向观察者的事件传递，即观察者模式。
     */
    @OnClick(R.id.tv_basal_use)
    public void onBasalUse() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("RxJava");
                subscriber.onCompleted();
            }
        });
        observable.subscribe(mSubscriber);

        //不关心OnComplete和OnError,可以使用Action1
        /*observable.subscribe(new Action1<String>() {
            @Override public void call(String s) {
                Logger.i(s);
            }
        });*/
    }

    //将会依次调用：// onNext("Hello");// onNext("Hi");// onNext("Aloha");// onCompleted();
    @OnClick(R.id.tv_just_operator)
    public void onJustOperator() {
        Observable<String> just = Observable.just("Hello", "RxJava");
        just.subscribe(mSubscriber);
    }

    @OnClick(R.id.tv_from_operator)
    public void onFromOperator() {
        final String[] arrays = {"Hello", "RxJava"};
        Observable<String> from = Observable.from(arrays);
        from.subscribe(mSubscriber);
    }

    //map适用于一对一的转换。
    @OnClick(R.id.tv_map_operator)
    public void onMapOperator() {
        final Student[] students = {};
        Observable.from(students).map(new Func1<Student, String>() {
            @Override
            public String call(Student student) {
                return student.name;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Logger.i("s");
            }
        });
    }

    /*
    flapMap适用于一对多的转换。
    
    flatMap() 的原理是这样的：
    1. 使用传入的事件对象创建一个 Observable 对象；
    2. 并不发送这个 Observable, 而是将它激活，于是它开始发送事件；
    3. 每一个创建出来的 Observable 发送的事件，都被汇入同一个 Observable ，
    而这个 Observable 负责将这些事件统一交给 Subscriber 的回调方法。
     */
    @OnClick(R.id.tv_flat_map_operator)
    public void onFlatMapOperator() {
        final Student[] students = {};
        Observable.from(students).flatMap(new Func1<Student, Observable<Course>>() {
            @Override
            public Observable<Course> call(Student student) {
                return Observable.from(student.courses);
            }
        }).subscribe(new Action1<Course>() {
            @Override
            public void call(Course course) {
                Logger.i(course.name);
            }
        });
    }

    @OnClick(R.id.tv_mul_action)
    public void onActionOperator() {
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Logger.i(s);
            }
        };

        Action1<Throwable> onError = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.i("onError");
            }
        };

        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Logger.i("onCompleted");
            }
        };

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("RxJava");
                subscriber.onCompleted();
            }
        });

        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onError, onCompletedAction);
    }

    //后台线程取数据，主线程显示
    @OnClick(R.id.tv_main_thread)
    public void onMainThread(final TextView textView) {
        Observable.just("Hello", "RxJava")
                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Logger.i(s);
                        textView.setText(s);
                    }
                });
    }

    // FIXME: 2017/3/16 点击一次无效
    @OnClick(R.id.tv_throttle_first)
    public void onThrottleFirst(TextView textView) {
        RxView.clicks(textView).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override public void accept(@NonNull Object o) throws Exception {
                        
                    }
                });
    }

    /*
    lift() 是针对事件项和事件序列的

    将事件中的 Integer 对象转换成 String 的例子.
    （如 map() flatMap() 等）进行组合来实现需求，因为直接使用 lift() 非常容易发生一些难以发现的错误。
    RxJava 都不建议开发者自定义 Operator 来直接使用 lift()，而是建议尽量使用已有的 lift() 包装方法
     */
    @OnClick(R.id.tv_left)
    public void onLeft() {
        Observable<Integer> integerObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

            }
        });

        integerObservable.lift(new Observable.Operator<String, Integer>() {
            @Override
            public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
                return new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        subscriber.onNext("" + integer);
                    }
                };
            }
        });
    }

    /*
     compose() 是针对 Observable 自身进行变换
     */
    @OnClick(R.id.tv_compose)
    public void onCompose() {
        LiftAllTransformer liftAll = new LiftAllTransformer();
//        observable1.compose(liftAll).subscribe(subscriber1);
//        observable2.compose(liftAll).subscribe(subscriber2);
//        observable3.compose(liftAll).subscribe(subscriber3);
    }

    private class LiftAllTransformer implements Observable.Transformer<Integer, String> {
        @Override
        public Observable<String> call(Observable<Integer> observable) {
//            return observable.lift1().lift2().lift3();
            return null;
        }
    }

    @OnClick(R.id.tv_first)
    public void onFirstOperator() {
        Observable.just("Hello", "RxJava", "哈哈哈").first().subscribe(new Action1<String>() {
            @Override public void call(String s) {
                Logger.i(s);
            }
        });
    }

    @OnClick(R.id.tv_last)
    public void onLastOperator() {
        Observable.just("Hello", "RxJava", "哈哈哈").last().subscribe(new Action1<String>() {
            @Override public void call(String s) {
                Logger.i(s);
            }
        });
    }

    /*
     * 只打印前count个.count<=0不显示 >Observable的数量，显示全部.
     * If there are fewer than count it'll just stop early.
     */
    @OnClick(R.id.tv_take)
    public void onTakeOperator() {
        Observable.just("Hello", "RxJava", "哈哈哈")
                .take(2)//同first()或last()
                .subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        Logger.i(s);
                    }
                });
    }

    @OnClick(R.id.tv_filter)
    public void onFilterOperator() {
        Observable.just("Hello", "RxJava", "")
                .filter(new Func1<String, Boolean>() {
                    @Override public Boolean call(String s) {
                        return s != null && !s.trim().isEmpty();
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        Logger.i(s);
                    }
                });
    }

    /*
    doOnNext()允许我们在每次输出一个元素之前做一些额外的事情
    doOnNext() allows us to add extra behavior each time an item is emitted, in this case saving the title.
     */
    @OnClick(R.id.tv_doOnNext)
    public void onDoOnNext() {
        Observable.just("Hello", "RxJava").doOnNext(new Action1<String>() {
            @Override public void call(String s) {
                Logger.i("save " + s + " to disk!");
            }
        }).subscribe(new Action1<String>() {
            @Override public void call(String s) {
                Logger.i(s);
            }
        });

    }


    private void testRngeOperator() {
        Observable.range(1, 4)
                .delay(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {
                        return null;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.i("doOnSubscribe");
                    }
                })
                .subscribe(new Subscriber<Integer>() {//rx.exceptions.OnErrorNotImplementedException
                    @Override
                    public void onCompleted() {
                        Logger.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Logger.i(integer.toString());
                    }
                });
    }

    private void testCompositeSubscription() {
        //Subscriber组合
        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(Observable.error(new RuntimeException("testing"))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.i("doOnSubscribe");
                    }
                })
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("onError()");
                    }

                    @Override
                    public void onNext(Object o) {
                        Logger.i("onNext()");
                    }
                })

        );
    }

    private void initPublishSubject() {
        mFloabSubject = PublishSubject.create();
        Subscription mFloabSubjectSubscription = mFloabSubject.asObservable().subscribe(new Action1<Float>() {
            @Override
            public void call(Float aFloat) {
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
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return aBoolean;
                    }
                })
                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Logger.i(aBoolean + "");
                    }
                });
    }

    private void testScheduler() {
        Observable.just("Hello World!")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.length();
                    }
                })
                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
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
                    @Override
                    public String call(String s) {
                        return s + " hahaha";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Logger.i(s + "");
                    }
                });

        /*
        map操作符更有趣的一点是它不必返回Observable对象返回的类型，
        你可以使用map操作符返回一个发出新的数据类型的observable对象。
         */
        Observable.just("Hello World !")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.length();
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Logger.i(integer.toString());
                    }
                });

        //多个map同事使用
        Observable.just("Hello World!")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s + "哈哈";
                    }
                })
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.length();
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
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
            @Override
            public void call(Subscriber<? super String> subscriber) {
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
            @Override
            public void onCompleted() {
                Logger.i("mSubscriber#onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Logger.i("mSubscriber#onError()");
            }

            @Override
            public void onNext(String s) {
                Logger.i("mSubscriber#onNext(): " + s);
            }
        };

        //创建观察者2
        mSubscriber2 = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Logger.i("mSubscriber2#onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Logger.i("mSubscriber2#onError()");
            }

            @Override
            public void onNext(String s) {
                Logger.i("mSubscriber2#onNext(): " + s);
            }
        };

        //简化的Subscriber
        mAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Logger.i(s);
            }
        };

        mActionError = new Action1<Throwable>() {
            @Override
            public void call(Throwable s) {
                Logger.i(s.toString());
            }
        };

        mActionComplete = new Action0() {
            @Override
            public void call() {
                Logger.i("complete()");
            }
        };
    }

    private void simpleObservable() {
        //简化的Observable,不关心OnComplete和OnError
        Observable.just("yes, hello too!").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
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
