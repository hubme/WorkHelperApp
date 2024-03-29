package com.king.applib.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.king.applib.util.StringUtil;

import java.util.List;

/**
 * 基础Activity
 *
 * @author VanceKing
 * @since 2016/9/29.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

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
        View rootView = getContentView();
        if (rootView != null) {
            setContentView(rootView);
        } else {
            setContentView(getContentLayout());
        }
        initContentView();
        initData();
        fetchData();
    }

    @Override
    public void onClick(View v) {

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
    protected void getIntentData(Intent intent) {

    }

    /**
     * 获取布局资源的id.
     */
    @LayoutRes
    protected int getContentLayout() {
        return 0;
    }

    @Nullable
    protected View getContentView() {
        return null;
    }

    /**
     * 给View填充数据。
     */
    protected void initData() {

    }

    /**
     * 访问网络数据。
     */
    protected void fetchData() {

    }

    /**
     * 初始化View
     */
    protected void initContentView() {

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

    protected void openActivityFinish(Class<? extends Activity> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
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
        final String value = intent.getStringExtra(key);
        return StringUtil.isNullOrEmpty(value) ? defaultValue : value;
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

    /**
     * View设置OnClick事件
     */
    protected void setViewClickListeners(@IdRes int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
    }

    /**
     * View设置OnClick事件
     */
    protected void setViewClickListeners(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
    }
}
