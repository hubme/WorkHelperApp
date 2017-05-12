package com.king.applib.simplebanner;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.king.applib.simplebanner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * banner 适配器
 *
 * @author huoguangxu
 * @since 2017/5/8.
 */
class BannerAdapter extends PagerAdapter {
    private final List<ImageView> mBannerViews = new ArrayList<>();
    private OnBannerClickListener mOnBannerClickListener;

    BannerAdapter() {

    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImageView imageView = mBannerViews.get(position);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mOnBannerClickListener != null && !mBannerViews.isEmpty()) {
                    mOnBannerClickListener.onBannerClick(mBannerViews.size() == 1 ? 0 : position - 1);
                }
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override public int getCount() {
        return mBannerViews.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void update(List<ImageView> bannerViews) {
        if (bannerViews == null || bannerViews.isEmpty()) {
            return;
        }
        if (!mBannerViews.isEmpty()) {
            mBannerViews.clear();
        }
        mBannerViews.addAll(bannerViews);
        notifyDataSetChanged();
    }

    public void setOnBannerClickListener(OnBannerClickListener listener) {
        mOnBannerClickListener = listener;
    }

}
