/*
 * Copyright 2016 GuDong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.king.applib.base;

import android.app.Fragment;
import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * BaseView 基类。模块化，抽取公共逻辑。
 *
 * @author VanceKing
 * @since 2017/12/5.
 */
public abstract class BaseViewController<T> {
    private T mData;
    private View mView;

    protected Context mContext;
    protected AppCompatActivity mActivity;
    protected Fragment mFragment;
    protected androidx.fragment.app.Fragment mV4Fragment;

    public BaseViewController(Context context) {
        mContext = context;
    }
    
    public BaseViewController(AppCompatActivity activity) {
        mActivity = activity;
        mContext = activity;
    }

    public BaseViewController(Fragment fragment) {
        mFragment = fragment;
        mContext = fragment.getActivity();
    }

    public BaseViewController(androidx.fragment.app.Fragment fragment) {
        mV4Fragment = fragment;
        mContext = fragment.getContext();
    }

    public void attachView(ViewGroup parent) {
        mView = initView(parent);
        parent.addView(mView);
        onCreatedView(mView);
    }

    public void attachView(ViewGroup parent, int index) {
        mView = initView(parent);
        parent.addView(mView, index);
        onCreatedView(mView);
    }

    public void attachView(ViewGroup parent, int width, int height) {
        mView = initView(parent);
        parent.addView(mView, width, height);
        onCreatedView(mView);
    }

    public void attachView(ViewGroup parent, ViewGroup.LayoutParams params) {
        mView = initView(parent);
        parent.addView(mView, params);
        onCreatedView(mView);
    }

    public void attachView(ViewGroup parent, int index, ViewGroup.LayoutParams params) {
        mView = initView(parent);
        parent.addView(mView, index, params);
        onCreatedView(mView);
    }

    protected View initView(ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(getLayoutResId(), parent, false);
    }

    public void populateData(T data) {
        mData = data;
        if (mData != null) {
            onBindView(data);
        }
    }

    public T getData() {
        return mData;
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    protected View generateView() {
        return null;
    }

    protected abstract void onCreatedView(View view);

    protected abstract void onBindView(T data);

    public void detachView() {
        onDestroyView(mView);
    }

    protected void onDestroyView(View view) {

    }

    public Context getContext() {
        return mContext;
    }

    public View getRootView() {
        return mView;
    }

}
