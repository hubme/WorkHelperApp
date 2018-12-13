package com.king.app.workhelper.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.king.app.workhelper.app.AppConfig;

/**
 * @author huoguangxu
 * @since 2017/4/27.
 */

public class LifeCircleFragment extends Fragment {
    private boolean mLogEnable = true;

    @Override public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        log("onInflate");
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        log("onAttach");
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("onCreate");
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        log("onViewCreated");
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log("onActivityCreated");
    }

    @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        log("onActivityCreated");
    }

    @Override public void onStart() {
        super.onStart();
        log("onStart");
    }

    @Override public void onResume() {
        super.onResume();
        log("onResume");
    }

    @Override public void onPause() {
        super.onPause();
        log("onPause");
    }

    @Override public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        log("onSaveInstanceState");
    }

    @Override public void onStop() {
        super.onStop();
        log("onStop");
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        log("onDestroyView");
    }

    @Override public void onDestroy() {
        super.onDestroy();
        log("onDestroy");
    }

    @Override public void onDetach() {
        super.onDetach();
        log("onDetach");
    }

    //普通Fragment不调用此方法
    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        log("setUserVisibleHint");
    }

    private void log(String message) {
        if (mLogEnable) {
            Log.i(AppConfig.LOG_TAG, "Fragment#" + message);
        }
    }
}
