package com.king.applib.convenientbanner.holder;


import android.content.Context;
import android.view.View;

/**
 * Created by Sai on 15/12/14.
 *
 * @param <T> 任何你指定的对象
 */
public interface ViewHolder<T> {
    View createView(Context context);

    void UpdateUI(Context context, int position, T data);
}