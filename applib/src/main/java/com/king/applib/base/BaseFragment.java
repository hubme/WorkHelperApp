package com.king.applib.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基础Fragment
 * Created by VanceKing on 2016/9/29.
 */

public abstract class BaseFragment extends Fragment {

    protected View mRootView;
    protected Context mContext;
    protected FragmentActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getContext();
        mActivity = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgumentData();
        initInitialData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentLayout(), container, false);
        initContentView(mRootView);
        initData();
        fetchData();
        return mRootView;
    }

    @Override 
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private void getArgumentData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            getBundleData(bundle);
        }
    }

    protected void getBundleData(Bundle bundle) {

    }

    /**
     * 初始化不耗时的数据,为了在View初始化以后直接使用<br/>
     * eg:设置默认值、从SP读取数据等。
     */
    protected void initInitialData() {
    }

    @LayoutRes protected abstract int getContentLayout();

    protected void initContentView(View view) {
    }

    protected void initData() {

    }

    protected void fetchData() {
        
    }

    protected void openActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(mContext, cls);
        mContext.startActivity(intent);
    }
}
