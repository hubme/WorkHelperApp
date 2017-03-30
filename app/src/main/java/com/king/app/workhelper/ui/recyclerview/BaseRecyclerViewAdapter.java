package com.king.app.workhelper.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础RecyclerView.Adapter
 *
 * @author huoguangxu
 * @since 2017/3/30.
 */

public abstract class BaseRecyclerViewAdapter<E, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> {
    //不允许直接赋值，防止NPE.
    private List<E> mAdapterList = new ArrayList<>();

    public BaseRecyclerViewAdapter() {
    }

    public BaseRecyclerViewAdapter(List<E> adapterData) {
        if (adapterData != null && adapterData.isEmpty()) {
            mAdapterList.addAll(adapterData);
        }
    }

    public List<E> getAdapterData() {
        return mAdapterList;
    }

    public void setAdapterData(List<E> adapterData) {
        setAdapterData(adapterData, false);
    }

    public void setAdapterData(List<E> adapterData, boolean append) {
        if (adapterData == null) {
            return;
        }
        if (append) {
            mAdapterList.addAll(adapterData);
        } else {
            if (!mAdapterList.isEmpty()) {
                mAdapterList.clear();
            }
            mAdapterList.addAll(adapterData);
        }
    }

    public void addData(int position, E entity) {
        if (position >= 0 && position <= mAdapterList.size()) {
            mAdapterList.add(entity);
            notifyItemInserted(position);
        }
    }

    public void deleteData(int position) {
        if (checkPosition(position)) {
            mAdapterList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void modifyData(int position, E e) {
        if (checkPosition(position)) {
            mAdapterList.remove(position);
            mAdapterList.add(position, e);
            notifyItemChanged(position);
        }
    }

    private boolean checkPosition(int position) {
        return position >= 0 && position < mAdapterList.size();
    }

    @Override
    public abstract K onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(K holder, int position);

    @Override
    public int getItemCount() {
        return mAdapterList.size();
    }
}
