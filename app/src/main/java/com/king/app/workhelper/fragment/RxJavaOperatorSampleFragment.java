package com.king.app.workhelper.fragment;

import android.widget.CheckedTextView;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

/**
 * RxJavaSample 操作符
 * Created by HuoGuangxu on 2016/11/4.
 */

public class RxJavaOperatorSampleFragment extends AppBaseFragment {
    @BindView(R.id.btn_send)
    public CheckedTextView mSendBtn;
    @BindView(R.id.et_input)
    public EditText mInputText;
    //注意解绑，特别是和定时任务相关的，很容易导致内存泄漏。
    private Subscription mSubscription;

    @Override protected int getContentLayout() {
        return R.layout.fragment_rxjava_sample;
    }

    @OnClick(R.id.btn_send)
    public void sendBtnClick() {
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    /*
    GroupBy操作符将原始Observable发射的数据按照key来拆分成一些小的Observable，然后这些小的Observable分别发射其所包含的的数据。
     */
    private void testGroupBy() {
        Observable.just(1, 2, 3, 6, 7)
                .groupBy(new Func1<Integer, Boolean>() {
                    @Override public Boolean call(Integer integer) {
                        return integer % 2 == 0;
                    }
                })
                .subscribe(new Action1<GroupedObservable<Boolean, Integer>>() {
                    @Override public void call(final GroupedObservable<Boolean, Integer> observable) {
                        observable.toList().subscribe(new Action1<List<Integer>>() {
                            @Override public void call(List<Integer> integers) {
                                Logger.i("key=" + observable.getKey() + ",values=" + integers);
                            }
                        });
                    }
                });
    }

    /*
    Buffer操作符定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值。
    Buffer操作符将一个Observable变换为另一个，原来的Observable正常发射数据，变换产生的Observable发射这些数据的缓存集合。
    如果原来的Observable发射了一个onError通知，Buffer会立即传递这个通知，而不是首先发射缓存的数据，即使在这之前缓存中包含了原始Observable发射的数据。
     */
    private void testBuffer() {
        mSubscription = RxView.clicks(mSendBtn)
                .buffer(2, TimeUnit.SECONDS)
                .subscribe(new Action1<List<Void>>() {
                    @Override public void call(List<Void> voids) {
                        Logger.i(voids.size() + "");
                    }
                });
    }

    /*
    创建一个按固定时间间隔发射整数序列的Observable.
    interval默认在computation调度器上执行。你也可以传递一个可选的Scheduler参数来指定调度器。
     */
    private void testInterval() {
        mSubscription = Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override public void call(Long aLong) {
                        Logger.i(aLong.toString());
                    }
                });
    }

    /*
    Timer会在指定时间后发射一个数字0，该操作符运行在Computation Scheduler。
     */
    private void testTimer() {
        mSubscription = Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override public void call(Long aLong) {
                        Logger.i(aLong.toString());//延时2s，输出0
                    }
                });
    }


    /*
    Range操作符根据初始值n和数目m发射一系列大于等于n的m个值
     */
    private void testRange() {
        mSubscription = Observable.range(2, 3)
                .subscribe(new Action1<Integer>() {
                    @Override public void call(Integer integer) {
                        Logger.i(integer.toString());//output:2 3 4
                    }
                });
    }
}
