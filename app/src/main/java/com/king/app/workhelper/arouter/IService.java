package com.king.app.workhelper.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author VanceKing
 * @since 2019/2/19.
 */
public interface IService extends IProvider {
    void sayHello(Context context);
}
