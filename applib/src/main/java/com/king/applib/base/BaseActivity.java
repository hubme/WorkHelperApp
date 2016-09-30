package com.king.applib.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 基础Activity
 * created by VanceKing at 2016/9/29
 */

public abstract class BaseActivity extends AppCompatActivity {

    private View mContentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInitialData();
        if (getIntent() != null) {
            getIntentData(getIntent());
        }
        mContentLayout = LayoutInflater.from(this).inflate(getContentLayout(), null);
        setContentView(mContentLayout);
        initContentView();
        initData();
    }

    /**
     * 初始化数据，获取不需要访问网络的数据。<br/>
     * eg:new ArrayList()、从SP读取数据等。
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
    public abstract int getContentLayout();

    /**
     * 可以访问网络，获取数据。给View填充数据。
     */
    protected void initData() {

    }

    /**
     * 初始化View
     */
    protected void initContentView() {

    }

    /**
     * 跳转到指定的Activity
     *
     * @param cls 指定的Activity
     */
    protected void startActivity(@NonNull Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param flags 启动的Activity标志
     * @param cls   指定的Activity
     */
    protected void startActivity(int flags, @NonNull Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.setFlags(flags);
        startActivity(intent);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param bundle 传递的数据
     * @param cls    指定的Activity
     */
    protected void startActivity(@NonNull Bundle bundle, @NonNull Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
