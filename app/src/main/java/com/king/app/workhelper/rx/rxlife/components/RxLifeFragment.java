package com.king.app.workhelper.rx.rxlife.components;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.king.app.workhelper.rx.rxlife.event.FragmentLifeEvent;
import com.king.app.workhelper.rx.rxlife.LifecycleTransformer;
import com.king.app.workhelper.rx.rxlife.RxLifecycle;
import com.king.applib.base.BaseFragment;

import io.reactivex.subjects.BehaviorSubject;

/**
 * 监听 Fragment 生命周期事件的基类.
 *
 * @author VanceKing
 * @since 2017/8/9.
 */
public abstract class RxLifeFragment extends BaseFragment {
    private final BehaviorSubject<FragmentLifeEvent> mLifeSubject = BehaviorSubject.create();

    @Override public void onAttach(Context context) {
        mLifeSubject.onNext(FragmentLifeEvent.ATTACH);
        super.onAttach(context);
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        mLifeSubject.onNext(FragmentLifeEvent.CREATE);
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLifeSubject.onNext(FragmentLifeEvent.CREATE_VIEW);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override public void onStart() {
        mLifeSubject.onNext(FragmentLifeEvent.START);
        super.onStart();
    }

    @Override public void onResume() {
        mLifeSubject.onNext(FragmentLifeEvent.RESUME);
        super.onResume();
    }

    @Override public void onPause() {
        mLifeSubject.onNext(FragmentLifeEvent.PAUSE);
        super.onPause();
    }

    @Override public void onStop() {
        mLifeSubject.onNext(FragmentLifeEvent.STOP);
        super.onStop();
    }

    @Override public void onDestroyView() {
        mLifeSubject.onNext(FragmentLifeEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        mLifeSubject.onNext(FragmentLifeEvent.DESTROY);
        super.onDestroy();
    }

    @Override public void onDetach() {
        mLifeSubject.onNext(FragmentLifeEvent.DETACH);
        super.onDetach();
    }

    public <T> LifecycleTransformer<T> bindUntilEvent(FragmentLifeEvent event) {
        return RxLifecycle.bindUntilEvent(mLifeSubject, event);
    }
}
