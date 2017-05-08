package com.king.app.workhelper.activity;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.king.applib.convenientbanner.holder.ViewHolder;
import com.king.applib.util.AppUtil;

import java.util.Locale;

/**
 * Created by Sai on 15/8/4.
 * 本地图片Holder例子
 */
public class LocalImageHolderView implements ViewHolder<Integer> {
    private SimpleDraweeView imageView;
    @Override
    public View createView(Context context) {
        imageView = new SimpleDraweeView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        imageView.setImageURI(String.format(Locale.US,"res://%s/%d", AppUtil.getAppInfo().getPackageName(), data));
    }
}
