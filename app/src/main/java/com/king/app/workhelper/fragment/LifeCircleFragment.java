package com.king.app.workhelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(AppConfig.LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(AppConfig.LOG_TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(AppConfig.LOG_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    //普通Fragment不调用此方法
    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(AppConfig.LOG_TAG, "setUserVisibleHint");
    }
}
