package com.king.app.workhelper.fragment;

import android.content.Context;
import android.os.UserManager;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.OnClick;

/**
 * 反射使用
 *
 * @author VanceKing
 * @since 2017/1/19 0019.
 */

public class ReflectFragment extends AppBaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_reflect;
    }

    @OnClick(R.id.tv_reflect)
    public void reflectClick() {
        try {
            final Method method = UserManager.class.getMethod("get", Context.class);
            method.setAccessible(true);
            method.invoke(null, this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
