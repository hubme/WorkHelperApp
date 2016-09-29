package com.king.applib.base;

import android.content.Context;
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
    protected Context mContext;
    protected List<T> mAdapterData;
    private int mLayoutId;

    public BaseListAdapter(Context mContext, int layoutId) {
        this.mLayoutId = layoutId;
        this.mContext = mContext;
        mAdapterData = new ArrayList<>();
    }

    public BaseListAdapter(Context context, int layoutId, List<T> dataList) {
        this.mContext = context;
        this.mAdapterData = dataList;
        this.mLayoutId = layoutId;
        if (mAdapterData == null) {
            mAdapterData = new ArrayList<>();
        }
    }

    public void setAdapterData(List<T> adapterData) {
        if (adapterData != null) {
            mAdapterData = adapterData;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mAdapterData != null ? mAdapterData.size() : 0;
    }

    @Override
    public T getItem(int position) {
        if (mAdapterData != null && position >= 0 && position < mAdapterData.size()) {
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
        ViewHolder holder = ViewHolder.getHolder(mContext, convertView, parent, mLayoutId, position);
        if (getItem(position) != null) {
            convert(position, holder, getItem(position));
        }
        return holder.getConvertView();
    }

    public abstract void convert(int position, ViewHolder holder, T t);
}
