package com.king.applib.ui.recyclerview;

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
 * @author VanceKing
 * @since 2017/3/30.
 */

public abstract class BaseRecyclerViewAdapter<E> extends RecyclerView.Adapter<RecyclerHolder> {
    private final List<E> mAdapterList = new ArrayList<>();
    private OnItemClickListener<E> mOnItemClickListener;

    public BaseRecyclerViewAdapter() {
    }
    
    public BaseRecyclerViewAdapter(List<E> adapterData) {
        addDataList(adapterData, false);
    }

    public List<E> getAdapterData() {
        return mAdapterList;
    }

    public void setAdapterData(List<E> adapterData) {
        setAdapterData(adapterData, false);
    }

    public void setAdapterData(List<E> adapterData, boolean append) {
        addDataList(adapterData, append);
    }

    protected void addDataList(List<E> adapterData, boolean append) {
        if (isListEmpty(adapterData)) {
            return;
        }
        if (append) {
            mAdapterList.addAll(adapterData);
        } else {
            if (!isListEmpty(mAdapterList)) {
                mAdapterList.clear();
            }
            mAdapterList.addAll(adapterData);
        }
        notifyDataSetChanged();
    }

    public void clearAdapterData() {
        mAdapterList.clear();
        notifyDataSetChanged();
    }

    private boolean isListEmpty(List<E> list) {
        return list == null || list.isEmpty();
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

    public E getItem(int position) {
        return mAdapterList.get(position);
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutRes(viewType), parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if (!checkPosition(position)) {
            return;
        }
        final E e = mAdapterList.get(position);
        if (e != null) {
            convert(holder, e, position);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(e);
                }
            }
        });
    }

    public abstract void convert(RecyclerHolder holder, E item, int position);

    @Override
    public int getItemCount() {
        return mAdapterList.size();
    }

    @LayoutRes public abstract int getItemLayoutRes(int viewType);

    public void setOnItemClickListener(OnItemClickListener<E> listener) {
        mOnItemClickListener = listener;
    }


    public interface OnItemClickListener<E> {
        void onItemClick(E e);
    }
}
