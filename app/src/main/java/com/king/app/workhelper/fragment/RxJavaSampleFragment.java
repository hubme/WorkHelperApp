package com.king.app.workhelper.fragment;

import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.model.entity.Course;
import com.king.app.workhelper.model.entity.Student;
import com.king.applib.log.Logger;
import com.king.applib.util.StringUtil;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

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
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJavaSample.http://blog.csdn.net/lzyzsd/article/details/41833541
 * 操作符：http://reactivex.io/documentation/operators.html#alphabetical
 *
 * @author VanceKing
 * @since 2016/11/4.
 */
public class RxJavaSampleFragment extends AppBaseFragment {
    @BindView(R.id.et_name) EditText mNameEt;
    @BindView(R.id.et_age) EditText mAgeEt;

    private Consumer<String> mSubscriber;
    private CompositeDisposable mCompositeDisposable;

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
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
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

    @OnClick(R.id.tv_basal_use)
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
    private void basalUse2() {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                //这一步是必须，我们通常可以在这里做一些初始化操作，调用request()方法表示初始化工作已经完成
                //调用request()方法去请求资源，参数就是要请求的数量，会立即触发onNext()方法
                //在onComplete()方法完成，才会再执行request()后边的代码
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
            }
        }, BackpressureStrategy.BUFFER).subscribe(subscriber);

        //更简洁的用法.因为返回Void，无法取消
//        Flowable.just("Hello RxJava").subscribe(subscriber);

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

    @OnClick(R.id.tv_just_operator)
    public void onJustOperator() {
        Disposable subscribe = Observable.just("Hello", "RxJava").subscribe(mSubscriber);
        mCompositeDisposable.add(subscribe);
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
        //        observable1.compose(liftAll).subscribe(subscriber1);
        //        observable2.compose(liftAll).subscribe(subscriber2);
        //        observable3.compose(liftAll).subscribe(subscriber3);
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
    doOnNext() allows us to add extra behavior each time an item is emitted, in this case saving the title.
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
                    @Override public void accept(@NonNull Object o) throws Exception {
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
        Observable<CharSequence> nameObservable = RxTextView.textChanges(mNameEt).skip(1);
        Observable<CharSequence> ageObservable = RxTextView.textChanges(mAgeEt).skip(1);
        Observable.combineLatest(nameObservable, ageObservable, new BiFunction<CharSequence, CharSequence, Boolean>() {
            @Override public Boolean apply(@NonNull CharSequence name, @NonNull CharSequence age) throws Exception {
                return !StringUtil.isNullOrEmpty(name.toString()) && !StringUtil.isNullOrEmpty(age.toString());
            }
        }).subscribe(new DisposableObserver<Boolean>() {
            @Override public void onNext(Boolean aBoolean) {
                showToast(aBoolean ? "合法" : "非法");
                Logger.i(aBoolean.toString());
            }

            @Override public void onError(Throwable e) {
                Logger.i("onError");
            }

            @Override public void onComplete() {
                Logger.i("onComplete");
            }
        });
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
    public void onIntervalOperator() {
        Disposable intervalDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Logger.i("哈哈哈");
                    }
                });
        mCompositeDisposable.add(intervalDisposable);
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
