/**
 * Copyright (C) 2006-2016 Tuniu All rights reserved
 */
package com.king.applib.base;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 防止内存泄露.声明静态内部类继承此类,重写handle方法
 * Created by HuoGuangxu on 2016/11/17.
 */
public abstract class WeakHandler<T> extends Handler {

    private WeakReference<T> mTargets;

    public WeakHandler(T target) {
        mTargets = new WeakReference<>(target);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T target = mTargets.get();
        if (target != null) {
            handle(target, msg);
        }
    }

    public abstract void handle(T target, Message msg);
}