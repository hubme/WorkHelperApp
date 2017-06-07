package com.king.applib.base;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView通用适配器
 * created by HuoGuangXu at 2016/5/27.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    private final List<T> mAdapterData = new ArrayList<>();
    @LayoutRes private int mLayoutId;

    public BaseListAdapter(@LayoutRes int layoutId) {
        this.mLayoutId = layoutId;
    }

    public BaseListAdapter(@LayoutRes int layoutId, List<T> dataList) {
        this.mLayoutId = layoutId;
        if (!isDataListEmpty(dataList)) {
            mAdapterData.addAll(dataList);
        }
    }

    public void setAdapterData(List<T> adapterData) {
        setAdapterData(adapterData, false);
    }

    public void setAdapterData(List<T> adapterData, boolean isAppend) {
        if (isDataListEmpty(adapterData)) {
            return;
        }
        if (isAppend) {
            mAdapterData.addAll(adapterData);
        } else {
            if (!isDataListEmpty(mAdapterData)) {
                mAdapterData.clear();
            }
            mAdapterData.addAll(adapterData);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        mAdapterData.clear();
        notifyDataSetChanged();
    }

    public void removeData(T t) {
        if (t != null) {
            mAdapterData.remove(t);
            notifyDataSetChanged();
        }
    }

    private boolean isDataListEmpty(List<T> dataData) {
        return dataData == null || dataData.isEmpty();
    }

    public List<T> getAdapterData() {
        return mAdapterData;
    }

    @Override
    public int getCount() {
        return mAdapterData.size();
    }

    @Override
    public T getItem(int position) {
        if (position >= 0 && position < mAdapterData.size()) {
            return mAdapterData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getHolder(convertView, parent, mLayoutId, position);
        if (getItem(position) != null) {
            convert(position, holder, getItem(position));
        }
        return holder.getConvertView();
    }

    public abstract void convert(int position, ViewHolder holder, T t);
}
