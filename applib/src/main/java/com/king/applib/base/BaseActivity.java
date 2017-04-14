package com.king.applib.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

/**
 * 基础Activity
 * created by VanceKing at 2016/9/29
 */

public abstract class BaseActivity extends AppCompatActivity {

    private View mContentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        beforeCreateView();
        super.onCreate(savedInstanceState);
        /*
        去掉Actionbar有两种方式。1.使用NoActionbar主题；
        2.代码设置。一般在BaseActivity(继承AppCompatActivity)设置supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        如果继承Activity，要使用requestWindowFeature(Window.FEATURE_NO_TITLE);
         */
        initInitialData();
        if (getIntent() != null) {
            getIntentData(getIntent());
        }
        mContentLayout = LayoutInflater.from(this).inflate(getContentLayout(), null);
        setContentView(mContentLayout);
        initContentView();
        setViewListeners();
        initData();
        loadingData();
    }

    /**
     * 在super.onCreate()之前执行
     */
    protected void beforeCreateView() {

    }

    /**
     * 初始化不耗时的数据,为了在View初始化以后直接使用<br/>
     * eg:设置默认值、从SP读取数据等。
     */
    protected void initInitialData() {
    }

    /**
     * 获取从上个页面传递来的数据
     */
    public void getIntentData(Intent intent) {

    }

    /**
     * 获取布局资源的id.
     */
    public abstract @LayoutRes int getContentLayout();

    /**
     * 给View填充数据。
     */
    protected void initData() {

    }

    /**
     * 访问网络数据。
     */
    protected void loadingData() {

    }

    /**
     * 初始化View
     */
    protected void initContentView() {

    }

    /**
     * 设置View的事件监听
     */
    protected void setViewListeners() {

    }

    /**
     * 跳转到指定的Activity
     *
     * @param cls 跳转到的Activity
     */
    protected void openActivity(@NonNull Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param flags 启动的Activity标志
     * @param cls   跳转到的Activity
     */
    protected void openActivity(int flags, @NonNull Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.setFlags(flags);
        startActivity(intent);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param bundle 传递的数据
     * @param cls    跳转到的Activity
     */
    protected void openActivity(@NonNull Bundle bundle, @NonNull Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected String getStringExtra(String key) {
        return getStringExtra(key, "");
    }

    /**
     * 从上一个Activity获取Intent中String类型的数据
     *
     * @param defaultValue 指定的key为null,返回defaultValue
     */
    protected String getStringExtra(String key, @NonNull String defaultValue) {
        Intent intent = getIntent();
        if (intent == null) {
            return defaultValue;
        }
        String value = intent.getStringExtra(key);
        return value == null ? defaultValue : value;
    }

    /**
     * 从上一个Activity获取Intent中T类型的数据
     *
     * @param defaultValue 指定的key为null,返回defaultValue
     */
    @SuppressWarnings("unchecked")
    protected <T> T getSerializableExtra(String key, T defaultValue) {
        Intent intent = getIntent();
        if (intent == null) {
            return defaultValue;
        }
        T value = (T) intent.getSerializableExtra(key);
        return value == null ? defaultValue : value;
    }

    /**
     * 从上一个Activity获取Intent中List的数据
     *
     * @param defaultValue 指定的key为null,返回defaultValue
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> getSerializableListExtra(String key, @NonNull List<T> defaultValue) {
        Intent intent = getIntent();
        if (intent == null) {
            return defaultValue;
        }
        List<T> list = (List<T>) intent.getSerializableExtra(key);
        return list == null ? defaultValue : list;
    }
}
