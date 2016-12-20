package com.king.applib.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgumentData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentLayout(), container, false);
        initContentView(mRootView);
        initData();
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getContext();
    }

    private void getArgumentData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            getIntentData(bundle);
        }
    }

    protected void getIntentData(Bundle bundle) {

    }

    protected abstract int getContentLayout();

    protected void initContentView(View view) {
    }

    protected void initData() {

    }

    protected void openActivity(Class<? extends Activity> cls) {
        Context context = getContext();
        if (context != null) {
            Intent intent = new Intent(context, cls);
            context.startActivity(intent);
        }
    }
}
