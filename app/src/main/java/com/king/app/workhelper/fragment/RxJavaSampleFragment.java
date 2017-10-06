package com.king.app.workhelper.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.common.RxCountDownHelper;
import com.king.app.workhelper.model.entity.Course;
import com.king.app.workhelper.model.entity.Student;
import com.king.applib.log.Logger;
import com.king.applib.util.NetworkUtil;
import com.king.applib.util.StringUtil;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;

/**
 * RxJavaSample.http://blog.csdn.net/lzyzsd/article/details/41833541
 * 操作符：http://reactivex.io/documentation/operators.html#alphabetical
 *
 * @author VanceKing
 * @since 2016/11/4.
 */
public class RxJavaSampleFragment extends AppBaseFragment {
    @BindView(R.id.et_input) EditText mInputEt;

    private Consumer<String> mSubscriber;
    private CompositeDisposable mCompositeDisposable;
    /** 网络变化发布者 */
    private PublishProcessor<Boolean> mNetworkChangedProcessor;
    private Disposable subscribe;

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

        //EditText变化时，每隔一秒打印EditText的内容,不打印"空".
        Disposable mTextChangeSubscribe = RxTextView.textChanges(mInputEt)
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(str -> StringUtil.isNotNullOrEmpty(str.toString()))
                .subscribe(str -> Logger.i(str.toString()));
        mCompositeDisposable.add(mTextChangeSubscribe);

        mNetworkChangedProcessor = PublishProcessor.create();
        mNetworkChangedProcessor.startWith(NetworkUtil.isNetworkAvailable())
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(online -> showToast(online ? "联上网啦" : "断网了，shit"));

        listenToNetworkConnectivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    private void listenToNetworkConnectivity() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent intent) {
                mNetworkChangedProcessor.onNext(NetworkUtil.isNetworkAvailable());
            }
        };
        final IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void initSubscriber() {
        mCompositeDisposable = new CompositeDisposable();

        mSubscriber = new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Logger.i(s);
            }
        };
    }

    //    @OnClick(R.id.tv_basal_use)
    public void onBasalUse() {
        //1.创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(String s) {
                Logger.i("onNext." + s);
            }

            @Override
            public void onError(Throwable e) {
                Logger.i("onError");
            }

            @Override
            public void onComplete() {
                Logger.i("onComplete");
            }
        };

        //2.创建被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("Hello RxJava!");
            }
        });

        //3.订阅
        observable.subscribe(observer);

    }

    //onBasalUse中最好这样写
    @OnClick(R.id.tv_basal_use)
    public void basalUse2() {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                //这一步是必须，否则无法触发onNext().我们通常可以在这里做一些初始化操作，调用request()方法表示初始化工作已经完成
                //调用request()方法去请求资源，参数就是要请求的数量，会立即触发onNext()方法
                s.request(Long.MAX_VALUE);
                Logger.i("呵呵呵");
            }

            @Override
            public void onNext(String s) {
                Logger.i("onNext." + s);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                //由于Reactive-Streams的兼容性，1.0中方法onCompleted被重命名为onComplete
                Logger.i("onComplete");
            }
        };

        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("Hello RxJava");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER).subscribe(subscriber);

        //更简洁的用法.因为返回Void，无法取消
//        Flowable.just("Hello RxJava").mTextChangeSubscribe(subscriber);

        //ResourceSubscriber实现Disposable接口,可以通过CompositeDisposable取消订阅
        /*ResourceSubscriber<String> resourceSubscriber = Flowable.just("Hello RxJava")
                .subscribeWith(new ResourceSubscriber<String>() {
                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mCompositeDisposable.add(resourceSubscriber);*/

    }

    private void sample1() {
        File[] files = new File[]{};
        Flowable.fromArray(files)
                //遍历文件数组，筛选出png文件
                .filter(new AppendOnlyLinkedArrayList.NonThrowingPredicate<File>() {
                    @Override public boolean test(File file) {
                        return file != null && file.getName().toLowerCase().endsWith(".png");
                    }
                })
                //将这每一个png文件都转成Bitmap传递
                .map(new Function<File, Bitmap>() {
                    @Override public Bitmap apply(@NonNull File file) throws Exception {
                        return null;
                    }
                })
                //耗时操作运行在子线程
                .subscribeOn(Schedulers.io())
                //修改UI的操作放在主线程
                .observeOn(AndroidSchedulers.mainThread())
                //订阅，展示
                .subscribe(new Consumer<Bitmap>() {
                    @Override public void accept(@NonNull Bitmap bitmap) throws Exception {
                        //处理bitmap
                    }
                });
    }

    @OnClick(R.id.tv_just_operator)
    public void onJustOperator() {
//        Disposable subscribe = Observable.just("Hello", "RxJava").subscribe(mSubscriber);
//        mCompositeDisposable.add(subscribe);

        testMerge();
    }

    public void testMerge() {
        Observable o1 = Observable.create((ObservableOnSubscribe<Integer>) e -> new Thread(() -> {
            SystemClock.sleep(1000);
            e.onNext(1);
            e.onComplete();
        }).start());

        Observable o2 = Observable.create((ObservableOnSubscribe<Integer>) e -> {
            e.onNext(2);
            e.onComplete();
        });

        Observable.merge(o1, o2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer o) throws Exception {
                        Log.i("aaa", o.toString());
                    }
                });
    }

    @OnClick(R.id.tv_from_operator)
    public void onFromOperator() {
        final String[] arrays = {"Hello", "RxJava"};
        Disposable subscribe = Observable.fromArray(arrays).subscribe(mSubscriber);
        mCompositeDisposable.add(subscribe);
    }

    /*
    map适用于一对一的转换。
    操作符就是为了解决对Observable对象的变换的问题，操作符用于在Observable和最终的Subscriber之间修改Observable发出的事件。
    1.map()操作符就是用于变换Observable对象的，map操作符返回一个Observable对象，
   这样就可以实现链式调用，在一个Observable对象上多次使用map操作符，最终将最简洁的数据传递给Subscriber对象。
     */
    @OnClick(R.id.tv_map_operator)
    public void onMapOperator() {
        final Student[] students = {};
        Disposable subscribe = Observable.fromArray(students).map(new Function<Student, String>() {
            @Override
            public String apply(@NonNull Student student) throws Exception {
                return student.name;
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(@NonNull String s) throws Exception {
                return "name: " + s;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Logger.i("s");
            }
        });
        mCompositeDisposable.add(subscribe);
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
        final List<Student> students = Arrays.asList(new Student("VanceKing"), new Student("fei"));

        Flowable.fromIterable(students).flatMap(new Function<Student, Publisher<Course>>() {
            @Override
            public Publisher<Course> apply(@NonNull Student student) throws Exception {
                return Flowable.fromIterable(student.courses);
            }
        }).subscribe(new Consumer<Course>() {
            @Override
            public void accept(@NonNull Course course) throws Exception {
                Logger.i(course.name);
            }
        });
    }

    @OnClick(R.id.tv_mul_action)
    public void onActionOperator() {
        Consumer<String> onNextAction = new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Logger.i(s);
            }
        };

        Consumer<Throwable> onError = new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Logger.i("onError");
            }
        };

        Action onCompletedAction = new Action() {
            @Override
            public void run() throws Exception {
                Logger.i("onCompleted");
            }
        };

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                observableEmitter.onNext("Hello");
                observableEmitter.onNext("RxJava");
                observableEmitter.onComplete();
            }
        });

        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        Disposable subscribe = observable.subscribe(onNextAction, onError, onCompletedAction);
        mCompositeDisposable.add(subscribe);
    }

    //后台线程取数据，主线程显示
    @OnClick(R.id.tv_main_thread)
    public void onMainThread(final TextView textView) {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("将会在3秒后显示");
                SystemClock.sleep(3000);
                e.onNext("Hello RxJava!");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        textView.setText(s);
                    }
                });
    }

    // FIXME: 2017/3/16 点击一次无效
    @OnClick(R.id.tv_throttle_first)
    public void onThrottleFirst(TextView textView) {
        Disposable subscribe = RxView.clicks(textView).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Logger.i("哈哈哈");
                    }
                });
        mCompositeDisposable.add(subscribe);
    }

    /*
    lift() 是针对事件项和事件序列的

    将事件中的 Integer 对象转换成 String 的例子.
    （如 map() flatMap() 等）进行组合来实现需求，因为直接使用 lift() 非常容易发生一些难以发现的错误。
    RxJava 都不建议开发者自定义 Operator 来直接使用 lift()，而是建议尽量使用已有的 lift() 包装方法
     */
    @OnClick(R.id.tv_left)
    public void onLeft() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {

            }
        }).lift(new ObservableOperator<String, Integer>() {
            @Override
            public Observer<? super Integer> apply(final Observer<? super String> observer) throws Exception {
                return new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        observer.onNext(integer.toString());
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

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
        //        LiftAllTransformer liftAll = new LiftAllTransformer();
        //        observable1.compose(liftAll).mTextChangeSubscribe(subscriber1);
        //        observable2.compose(liftAll).mTextChangeSubscribe(subscriber2);
        //        observable3.compose(liftAll).mTextChangeSubscribe(subscriber3);
    }

    /*private class LiftAllTransformer implements Observable.Transformer<Integer, String> {
        @Override
        public Observable<String> call(Observable<Integer> observable) {
            //            return observable.lift1().lift2().lift3();
            return null;
        }
    }*/

    @OnClick(R.id.tv_first)
    public void onFirstOperator() {
        Disposable subscribe = Observable.just("Hello", "RxJava").first("default").subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Logger.i(s);
            }
        });
        mCompositeDisposable.add(subscribe);
    }

    @OnClick(R.id.tv_last)
    public void onLastOperator() {
        Disposable subscribe = Observable.just("Hello", "RxJava", "哈哈哈").last("default").subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Logger.i(s);
            }
        });
        mCompositeDisposable.add(subscribe);
    }

    /*
     * 只打印前count个.count<=0不显示 >Observable的数量，显示全部.
     * If there are fewer than count it'll just stop early.
     */
    @OnClick(R.id.tv_take)
    public void onTakeOperator() {
        Disposable subscribe = Observable.just("Hello", "RxJava", "哈哈哈")
                .take(2)//同first()或last()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Logger.i(s);
                    }
                });
        mCompositeDisposable.add(subscribe);
    }

    @OnClick(R.id.tv_filter)
    public void onFilterOperator() {
        Disposable subscribe = Observable.just("Hello", "RxJava", "").filter(new Predicate<String>() {
            @Override
            public boolean test(@NonNull String s) throws Exception {
                return s != null && !s.trim().isEmpty();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Logger.i(s);
            }
        });
        mCompositeDisposable.add(subscribe);
    }

    /*
    doOnNext()允许我们在每次输出一个元素之前做一些额外的事情
    doOnNext() allows us to add extra mBottomSheetBehavior each time an item is emitted, in this case saving the title.
     */
    @OnClick(R.id.tv_doOnNext)
    public void onDoOnNext() {
        Disposable subscribe = Observable.just("Hello", "RxJava").doOnNext(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Logger.i("save " + s + " to disk!");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Logger.i(s);
            }
        });
        mCompositeDisposable.add(subscribe);
    }

    @OnClick(R.id.tv_range)
    public void onRange() {
        Observable.range(5, 3).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                Logger.i(integer.toString());
            }
        });
    }

    @OnClick(R.id.tv_debounce)
    public void onDebounce(TextView textView) {
        RxView.clicks(textView).debounce(2, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())//添加报错
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        showToast("每两秒弹一次");
                    }
                });

    }

    /*
    使用combineLatest合并最近N个结点.
    例如：注册的时候所有输入信息（邮箱、密码、电话号码等）合法才点亮注册按钮。
     */
    @OnClick(R.id.tv_combineLatest)
    public void onCombineLatest() {
        clickedOn(new CombineLatestSample());
    }

    @OnClick(R.id.tv_merge_sample)
    public void onMergeOperator() {
        clickedOn(new MergeSampleFragment());
    }

    @OnClick(R.id.tv_timer)
    public void onTimerOperator() {
        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Logger.i("哈哈哈");
                    }
                });
    }

    @OnClick(R.id.tv_interval)
    public void onIntervalOperator(TextView textView) {
        /*Disposable intervalDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Logger.i("哈哈哈."+aLong.toString());
                    }
                });
        mCompositeDisposable.add(intervalDisposable);*/

        /*Subject<Object> objectSubject = PublishSubject.create().toSerialized();
        objectSubject.throttleFirst(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override public void accept(@NonNull Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });*/

        Disposable disposable = RxCountDownHelper.startCountDown(textView, 10, "结束", value -> textView.setText(String.valueOf(value + "秒")));
        mCompositeDisposable.add(disposable);
    }

    @OnClick(R.id.tv_buffer)
    public void onBufferOperator(TextView textView) {
        Disposable subscribe = RxView.clicks(textView)
                .map(onClickEvent -> 1)
                .buffer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integers -> {
                    if (!integers.isEmpty()) {
                        Logger.i("size: " + integers.size());
                    } else {
                        Logger.i("No taps received");
                    }
                });
        mCompositeDisposable.add(subscribe);
    }

    @OnClick(R.id.tv_PublishProcessor)
    public void onPublishProcessor() {
        PublishProcessor<String> publishProcessor = PublishProcessor.create();
        publishProcessor.subscribe(str -> Logger.i(str));
        publishProcessor.onNext("哈哈哈");
        publishProcessor.onNext("呵呵呵");
        publishProcessor.subscribe(str -> Logger.i(str));
        publishProcessor.onNext("000");
        publishProcessor.onNext("111");
    }

    @OnClick(R.id.tv_retry)
    public void onRetryWhenClick() {
        clickedOn(new RxRetryFragment());
    }

    @OnClick(R.id.tv_network_changed)
    public void onNetworkChanged() {

    }

    /*
    将一个发射数据的Observable转换为发射那些数据发射时间间隔的Observable.
    操作符拦截原始Observable发射的数据项，替换为发射表示相邻发射物时间间隔的对象。
     */
    @OnClick(R.id.tv_timeInterval)
    public void onTimeInterval() {
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
                        Logger.i(String.valueOf(stringTimed.time()));
                    }
                });
    }

    @OnClick(R.id.tv_count_down)
    public void onCountDown(TextView textView) {
        final int duration = 6;
        subscribe = Observable.interval(1, 1, TimeUnit.SECONDS)
                .doOnDispose(() -> Logger.i("doOnDispose"))
                .doOnNext(aLong -> {
                    Logger.i("onOnNext: " + aLong.toString());
                    if (aLong == 3) {
                        subscribe.dispose();
                    }
                })
                .doOnComplete(() -> Logger.i("doOnComplete"))
                .doFinally(() -> Logger.i("doFinally"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .takeUntil(aLong -> aLong.intValue() == duration)
                .map(aLong -> duration - aLong)
                .subscribe(aLong -> textView.setText(String.valueOf(aLong.intValue())));
    }

    public void testPublishSubject() {
        Flowable.interval(0, 2, TimeUnit.SECONDS)
                .takeUntil(Flowable.timer(5, TimeUnit.SECONDS))
                .subscribe(new Consumer<Long>() {
                    @Override public void accept(@NonNull Long aLong) throws Exception {
                        Logger.i(aLong.toString());
                    }
                });
    }

    private void clickedOn(@NonNull Fragment fragment) {
        final String tag = fragment.getClass().toString();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.layout_container, fragment, tag)
                .commit();
    }

    /*总结：
      1.Observable和Subscriber可以做任何事情
     Observable可以是一个数据库查询，Subscriber用来显示查询结果；Observable可以是屏幕上的点击事件，Subscriber用来响应点击事件；Observable可以是一个网络请求，Subscriber用来显示请求结果。

     2.Observable和Subscriber是独立于中间的变换过程的。
     在Observable和Subscriber中间可以增减任何数量的map。整个系统是高度可组合的，操作数据是一个很简单的过程。

     */
}
