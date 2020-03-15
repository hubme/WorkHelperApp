package com.king.applib.base;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
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

    public BaseViewPagerAdapter() {
        
    }
    
    public BaseViewPagerAdapter(List<T> data) {
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

    public void clearAdapterData() {
        if (!mAdapterData.isEmpty()) {
            mAdapterData.clear();
        }
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
    public View instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView;
        int itemLayoutRes = getItemLayoutRes();
        if (itemLayoutRes > 0) {
            itemView = LayoutInflater.from(container.getContext()).inflate(itemLayoutRes, container, false);
        } else {
            itemView = generateView(container.getContext());
        }
        if (itemView == null) {
            throw new IllegalArgumentException("Item View must not be null!");
        }
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

    protected abstract int getItemLayoutRes();

    protected View generateView(Context context) {
        return null;
    }

    protected abstract void convert(int position, ViewHolder holder, T t);
}
