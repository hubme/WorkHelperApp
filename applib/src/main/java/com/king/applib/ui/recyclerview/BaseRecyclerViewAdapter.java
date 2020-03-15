package com.king.applib.ui.recyclerview;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

    public void appendList(List<E> appendedList) {
        if (isListEmpty(appendedList)) {
            return;
        }
        int positionStart = mAdapterList.size();
        mAdapterList.addAll(positionStart, appendedList);
        //RecyclerView 嵌套 NestScrollView 无效。notifyDataSetChanged()有效。
        notifyItemRangeInserted(positionStart, appendedList.size());
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

    @NonNull @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = getItemLayoutRes(viewType) > 0 ? LayoutInflater.from(parent.getContext()).inflate(getItemLayoutRes(viewType), parent, false) :
                generateView(parent.getContext(), viewType);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        if (!checkPosition(position)) {
            return;
        }
        final E e = mAdapterList.get(position);
        if (e != null) {
            convert(holder, e, position);
        }
    }

    public abstract void convert(RecyclerHolder holder, E item, int position);

    @Override
    public int getItemCount() {
        return mAdapterList.size();
    }

    public abstract int getItemLayoutRes(int viewType);

    protected View generateView(Context context, int viewType) {
        return null;
    }
}
