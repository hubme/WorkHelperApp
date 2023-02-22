package com.example.biz_export;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author VanceKing
 * @since 2019/2/19.
 */
public interface IHomeService extends IProvider {
    void sayHello(Context context);
}
