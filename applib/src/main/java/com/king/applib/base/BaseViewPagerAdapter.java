package com.king.applib.base;

import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * PagerAdapter封装类
 *
 * @author huoguangxu
 * @since 2017/5/12.
 */

public abstract class BaseViewPagerAdapter<T> extends PagerAdapter {
    private final List<T> mAdapterData = new ArrayList<>();
//    private SparseArray<View> mViews = new SparseArray<>();
    @LayoutRes private int mLayoutId;

    public BaseViewPagerAdapter(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
    }
    
    public BaseViewPagerAdapter(@LayoutRes int layoutId, List<T> data) {
        mLayoutId = layoutId;
        if (data != null && !data.isEmpty()) {
            mAdapterData.addAll(data);
        }
    }

    public void setAdapterData(List<T> data) {
        setAdapterData(data, false);
    }

    public void setAdapterData(List<T> data, boolean isAppend) {
        if (data == null || data.isEmpty()) {
            return;
        }
        if (isAppend) {
            mAdapterData.addAll(data);
        } else {
            if (!mAdapterData.isEmpty()) {
                mAdapterData.clear();
            }
            mAdapterData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public List<T> getAdapterData() {
        return mAdapterData;
    }

    public void resetAdapterData() {
        mAdapterData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAdapterData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewHolder holder = ViewHolder.getHolder(null, container, mLayoutId, position);
        View view = holder.getConvertView();
        if (getItem(position) != null) {
            convert(position, holder, getItem(position));
        }
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;//解决notifyDataSetChanged无效
    }

    public T getItem(int position) {
        if (position < 0 || position > mAdapterData.size() - 1) {
            return null;
        }
        return mAdapterData.get(position);
    }

    public abstract void convert(int position, ViewHolder holder, T t);
}
