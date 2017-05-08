package com.king.applib.simplebanner;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.king.applib.convenientbanner.holder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * banner 适配器
 *
 * @author huoguangxu
 * @since 2017/5/8.
 */
public abstract class BannerAdapter<T> extends PagerAdapter {
    private static final int MULTIPLE_COUNT = 2;
    private boolean canLoop = true;
    private final List<T> mBannerModels = new ArrayList<>();

    public BannerAdapter(List<T> bannerModels) {
        if (bannerModels == null || bannerModels.isEmpty()) {
            return;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = convert2RealPosition(position);

        View view = getView(realPosition, null, container);
        container.addView(view);
        return view;
    }

    @Override public int getCount() {
        return mBannerModels.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    public ImageView getView(int position, View view, ViewGroup container) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    private int convert2RealPosition(int position) {
        final int realCount = getRealSize();
        if (realCount == 0){
            return 0;
        }
        return position % realCount;
    }

    private int getRealSize() {
        return mBannerModels == null ? 0 : mBannerModels.size();
    }

    public abstract void convert(int position, ViewHolder holder, T t);
}
