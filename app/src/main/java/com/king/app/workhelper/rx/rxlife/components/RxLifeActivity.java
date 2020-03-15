package com.king.app.workhelper.rx.rxlife.components;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.king.app.workhelper.rx.rxlife.event.ActivityLifeEvent;
import com.king.app.workhelper.rx.rxlife.LifecycleTransformer;
import com.king.app.workhelper.rx.rxlife.RxLifecycle;
import com.king.applib.base.BaseActivity;

import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.BehaviorSubject;

/**
 * 监听 Activity 生命周期事件的基类.
 *
 * @author VanceKing
 * @since 2017/8/9.
 */

public abstract class RxLifeActivity extends BaseActivity {
    private final BehaviorSubject<ActivityLifeEvent> mLifeSubject = BehaviorSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mLifeSubject.onNext(ActivityLifeEvent.CREATE);
        super.onCreate(savedInstanceState);
    }

    @Override protected void onStart() {
        mLifeSubject.onNext(ActivityLifeEvent.START);
        super.onStart();
    }

    @Override protected void onResume() {
        mLifeSubject.onNext(ActivityLifeEvent.RESUME);
        super.onResume();
    }

    @Override protected void onPause() {
        mLifeSubject.onNext(ActivityLifeEvent.PAUSE);
        super.onPause();
    }

    @Override protected void onStop() {
        mLifeSubject.onNext(ActivityLifeEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mLifeSubject.onNext(ActivityLifeEvent.DESTROY);
        super.onDestroy();
    }

    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityLifeEvent event) {
        return RxLifecycle.bindUntilEvent(mLifeSubject, event);
    }
}
