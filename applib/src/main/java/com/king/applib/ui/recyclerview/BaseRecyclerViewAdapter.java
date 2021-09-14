package com.king.applib.ui.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView 基础适配器。
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
            appendItems(adapterData);
        } else {
            if (!isListEmpty(mAdapterList)) {
                mAdapterList.clear();
            }
            mAdapterList.addAll(adapterData);
            notifyDataSetChanged();
        }
    }

    /**
     * 清空 Adapter 数据。
     */
    public void clearAdapterData() {
        mAdapterList.clear();
        notifyDataSetChanged();
    }

    /**
     * 在 position 位置插入一条元素。
     *
     * @param position 插入元素的索引
     * @param entity   插入的元素
     */
    public void insertItem(int position, E entity) {
        if (checkPosition(position)) {
            mAdapterList.add(position, entity);
            notifyItemInserted(position);
        }
    }

    /**
     * 在 position 位置插入元素列表。
     *
     * @param position     插入元素位置的索引
     * @param appendedList 插入的元素列表
     */
    public void insertItems(int position, List<E> appendedList) {
        mAdapterList.addAll(position, appendedList);
        //RecyclerView 嵌套 NestScrollView 无效。notifyDataSetChanged()有效。
        notifyItemRangeInserted(position, appendedList.size());
    }

    /**
     * 在列表末尾插入元素列表。
     *
     * @param appendedList 插入的元素列表
     */
    public void appendItems(List<E> appendedList) {
        if (isListEmpty(appendedList)) {
            return;
        }
        insertItems(getItemCount(), appendedList);
    }

    /**
     * 删除 position 位置的元素。
     *
     * @param position 指定位置的索引
     */
    public void deleteItem(int position) {
        deleteItems(position, 1);
    }

    /**
     * 删除 position（包含）位置后的 itemCount 个元素。
     *
     * @param position  指定位置的索引
     * @param itemCount 删除元素的数量
     */
    public void deleteItems(int position, int itemCount) {
        if (checkPosition(position) && itemCount > 0 && itemCount <= getItemCount() - position) {
            for (int i = 0; i < itemCount; i++) {
                mAdapterList.remove(position);
            }
            notifyItemRangeRemoved(position, itemCount);
        }
    }

    /**
     * 更新 position 位置的元素。
     *
     * @param position 指定位置的索引
     * @param e        更新的元素
     */
    public void updateItem(int position, E e) {
        if (checkPosition(position)) {
            mAdapterList.remove(position);
            mAdapterList.add(position, e);
            notifyItemChanged(position);
        }
    }

    private boolean checkPosition(int position) {
        return position >= 0 && position < mAdapterList.size();
    }

    @Nullable
    public E getItem(int position) {
        if (!checkPosition(position)) {
            return null;
        }
        return mAdapterList.get(position);
    }

    @NonNull
    @Override
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

    protected boolean isListEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }
}
