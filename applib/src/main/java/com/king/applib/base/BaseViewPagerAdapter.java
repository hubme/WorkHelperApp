package com.king.applib.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * PagerAdapter封装类
 *
 * @author VanceKing
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
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(mLayoutId, container, false);
        T itemData = getItem(position);
        if (itemData != null) {
            convert(position, ViewHolder.create(itemView), itemData);
        }
        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
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
