package com.king.app.workhelper.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.model.entity.Student;
import com.king.applib.log.Logger;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJavaSample.http://blog.csdn.net/lzyzsd/article/details/41833541
 * Created by HuoGuangxu on 2016/11/4.
 */

public class RxJavaSampleFragment extends AppBaseFragment {
    @BindView(R.id.et_input)
    public EditText mInputText;

    private Consumer mSubscriber;

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
    }

    private void initSubscriber() {
        mSubscriber = new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Logger.i(s);
            }
        };
    }

    @OnClick(R.id.tv_basal_use)
    public void onBasalUse() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                observableEmitter.onNext("Hello");
                observableEmitter.onNext("RxJava");
                observableEmitter.onComplete();
            }
        }).subscribe(mSubscriber);
    }

    @OnClick(R.id.tv_just_operator)
    public void onJustOperator() {
        Observable.just("Hello", "RxJava").subscribe(mSubscriber);
    }

    @OnClick(R.id.tv_from_operator)
    public void onFromOperator() {
        final String[] arrays = {"Hello", "RxJava"};
        Observable.fromArray(arrays).subscribe(mSubscriber);
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
        Observable.fromArray(students).map(new Function<Student, String>() {
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
        /*final Student[] students = {};
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

        Observable.fromArray(students).flatMap(new Function<Student, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@NonNull Student student) throws Exception {
                return Observable.fromArray(student.courses);
            }
        });*/
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
        observable.subscribe(onNextAction, onError, onCompletedAction);
    }

    //后台线程取数据，主线程显示
    @OnClick(R.id.tv_main_thread)
    public void onMainThread(final TextView textView) {
        Observable.just("Hello", "RxJava")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
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
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Logger.i("哈哈哈");
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
        Observable.just("Hello", "RxJava").first("default").subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Logger.i(s);
            }
        });
    }

    @OnClick(R.id.tv_last)
    public void onLastOperator() {
        Observable.just("Hello", "RxJava", "哈哈哈").last("default").subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
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
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Logger.i(s);
                    }
                });
    }

    @OnClick(R.id.tv_filter)
    public void onFilterOperator() {
        Observable.just("Hello", "RxJava", "").filter(new Predicate<String>() {
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
    }

    /*
    doOnNext()允许我们在每次输出一个元素之前做一些额外的事情
    doOnNext() allows us to add extra behavior each time an item is emitted, in this case saving the title.
     */
    @OnClick(R.id.tv_doOnNext)
    public void onDoOnNext() {
        Observable.just("Hello", "RxJava").doOnNext(new Consumer<String>() {
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

    }


    /*总结：
      1.Observable和Subscriber可以做任何事情
     Observable可以是一个数据库查询，Subscriber用来显示查询结果；Observable可以是屏幕上的点击事件，Subscriber用来响应点击事件；Observable可以是一个网络请求，Subscriber用来显示请求结果。

     2.Observable和Subscriber是独立于中间的变换过程的。
     在Observable和Subscriber中间可以增减任何数量的map。整个系统是高度可组合的，操作数据是一个很简单的过程。

     */
}
