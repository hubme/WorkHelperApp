package com.king.app.workhelper.ui.recyclerview;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础RecyclerView.Adapter
 *
 * @author huoguangxu
 * @since 2017/3/30.
 */

public abstract class BaseRecyclerViewAdapter<E> extends RecyclerView.Adapter<RecyclerHolder> {
    //不允许赋值，防止NPE.
    private final List<E> mAdapterList = new ArrayList<>();
    @LayoutRes private int mLayoutRes;

    public BaseRecyclerViewAdapter(@LayoutRes int layoutRes) {
        mLayoutRes = layoutRes;
    }

    public BaseRecyclerViewAdapter(@LayoutRes int layoutRes, List<E> adapterData) {
        mLayoutRes = layoutRes;
        if (adapterData != null && !adapterData.isEmpty()) {
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

    public void resetAdapterData() {
        mAdapterList.clear();
        notifyDataSetChanged();
    }

    public void addData(int position, E entity) {
        if (position >= 0 && position <= mAdapterList.size()) {
            mAdapterList.add(position, entity);
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

    public abstract void convert(RecyclerHolder holder, E item, int position);

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutRes, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        E e = mAdapterList.get(position);
        if (e != null) {
            convert(holder, e, position);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapterList.size();
    }
}
