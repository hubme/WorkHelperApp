package com.king.applib.simplebanner;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * banner 适配器
 *
 * @author huoguangxu
 * @since 2017/5/8.
 */
class BannerAdapter extends PagerAdapter {
    private static final int MULTIPLE_COUNT = 2;
    private boolean canLoop = true;
    private final List<ImageView> mImageViews = new ArrayList<>();
    private OnBannerClickListener mOnBannerClickListener;
    
    BannerAdapter() {
        
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImageView imageView = mImageViews.get(position);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mOnBannerClickListener != null) {
                    mOnBannerClickListener.onBannerClick(position);
                }
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override public int getCount() {
        return mImageViews.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void update(List<ImageView> imageViews) {
        mImageViews.addAll(imageViews);
        notifyDataSetChanged();
    }

    public interface OnBannerClickListener {
        void onBannerClick(int position);
    }

    public void setOnBannerClickListener(OnBannerClickListener listener) {
        mOnBannerClickListener = listener;
    }

    public ImageView getView(int position, View view, ViewGroup container) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    private int convert2RealPosition(int position) {
        final int realCount = getRealSize();
        if (realCount == 0) {
            return 0;
        }
        return position % realCount;
    }

    private int getRealSize() {
        return 0;
    }

}
