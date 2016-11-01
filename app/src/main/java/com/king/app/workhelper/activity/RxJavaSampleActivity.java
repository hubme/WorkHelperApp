package com.king.app.workhelper.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.log.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * RxJava.http://blog.csdn.net/lzyzsd/article/details/41833541
 * Created by HuoGuangxu on 2016/11/1.
 */

public class RxJavaSampleActivity extends AppBaseActivity {

    @BindView(R.id.btn_send)
    public Button mSendBtn;
    @BindView(R.id.et_input)
    public EditText mInputText;

    private Observable<String> mObservable;
    private Observable<String> mObservable2;

    private Subscriber<String> mSubscriber;
    private Subscriber<String> mSubscriber2;
    private Action1<String> mAction;
    private Action0 mActionComplete;
    private Action1<Throwable> mActionError;

    @Override public int getContentLayout() {
        return R.layout.activity_sample_rxjava;
    }

    @Override protected void initData() {
        super.initData();

        //创建被观察者
        mObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(Subscriber<? super String> subscriber) {
                //被观察者向被观察者发送消息
                subscriber.onNext("Hello World! 哈哈");
                //告诉观察者结束，观察者会在onCompleted()回调中收到消息。
                subscriber.onCompleted();
            }
        });

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

        //简化的Observable,不关心OnComplete和OnError
        Observable.just("yes, hello too!").subscribe(new Action1<String>() {
            @Override public void call(String s) {
                Logger.i(s);
            }
        });

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

    @OnClick(R.id.btn_send)
    public void sendBtnClick() {
        //Subscriber的回调函数会按注册时的顺序执行.
        // TODO: 2016/11/1 注册多个Observable，只有第一个有用？
//        mObservable.subscribe(mSubscriber);
//        mObservable.subscribe(mSubscriber2);

        //有三个重装方法
//        mObservable2.subscribe(mAction);
        mInputText.setVisibility(View.GONE);
        Logger.i(mInputText.isShown()+"");
    }
}
