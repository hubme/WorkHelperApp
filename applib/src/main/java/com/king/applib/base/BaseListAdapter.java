package com.king.applib.base;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView通用适配器
 *
 * @author huoguangxu
 * @since 2016/5/27.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    private final List<T> mAdapterData = new ArrayList<>();
    @LayoutRes private int mLayoutId;

    public BaseListAdapter(@LayoutRes int layoutId) {
        this.mLayoutId = layoutId;
    }

    public BaseListAdapter(@LayoutRes int layoutId, List<T> dataList) {
        this.mLayoutId = layoutId;
        if (!isListEmpty(dataList)) {
            mAdapterData.addAll(dataList);
        }
    }

    public void setAdapterData(List<T> adapterData) {
        setAdapterData(adapterData, false);
    }

    public void setAdapterData(List<T> adapterData, boolean isAppend) {
        if (isListEmpty(adapterData)) {
            return;
        }
        if (isAppend) {
            mAdapterData.addAll(adapterData);
        } else {
            if (!isListEmpty(mAdapterData)) {
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

    private boolean isListEmpty(List<T> dataList) {
        return dataList == null || dataList.isEmpty();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        }
        T itemData = getItem(position);
        if (itemData != null) {
            convert(position, ViewHolder.create(convertView), itemData);
        }
        return convertView;
    }

    public abstract void convert(int position, ViewHolder holder, T t);
}
