package com.king.applib.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.king.applib.util.ContextUtil;

/**
 * 基础Fragment
 *
 * @author VanceKing
 * @since 2016/9/29
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected View mRootView;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgumentData();
        initInitialData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

    @Override
    public void onClick(View v) {

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

    @LayoutRes
    protected abstract int getContentLayout();

    protected void initContentView(View view) {
    }

    protected void initData() {

    }

    protected void fetchData() {

    }

    protected void openActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    protected void openActivityForResult(Class<? extends Activity> cls, int requestCode) {
        Intent intent = new Intent(getActivity(), cls);
        startActivityForResult(intent, requestCode);
    }

    protected void showToast(String toast) {
        if (isAdded() && !TextUtils.isEmpty(toast)) {
            Toast.makeText(ContextUtil.getAppContext(), toast, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showToast(@StringRes int resId) {
        if (isAdded()) {
            Toast.makeText(ContextUtil.getAppContext(), resId, Toast.LENGTH_SHORT).show();
        }
    }

    protected void setViewOnClickListener(View... views) {
        if (views == null || views.length == 0) {
            return;
        }
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }
}
