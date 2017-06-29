package com.king.app.workhelper.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.king.app.workhelper.model.entity.StringEntity;
import com.king.app.workhelper.ui.recyclerview.CategoryDelegate;
import com.king.app.workhelper.ui.recyclerview.ContentDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huoguangxu
 * @since 2017/6/29.
 */

public class BasicMultiRecyclerAdapter3 extends RecyclerView.Adapter {

    private final CategoryDelegate categoryDelegate;
    private final ContentDelegate contentDelegate;
    private final List<StringEntity> mItems;

    public BasicMultiRecyclerAdapter3(List<StringEntity> items) {
        mItems = items;

        categoryDelegate = new CategoryDelegate();
        contentDelegate = new ContentDelegate();
    }


    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == StringEntity.ItemType.CATEGORY) {
            return categoryDelegate.onCreateViewHolder(parent);
        } else if (viewType == StringEntity.ItemType.CONTENT) {
            return contentDelegate.onCreateViewHolder(parent);
        }
        throw new IllegalArgumentException("不支持的item type");
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoryDelegate.CategoryViewHolder) {
            categoryDelegate.onBindViewHolder(mItems, position, holder);
        } else if (holder instanceof ContentDelegate.CategoryViewHolder) {
            contentDelegate.onBindViewHolder(mItems, position, holder);
        }
    }

    @Override public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).type;
    }

    public static List<StringEntity> fakeMultiTypeData() {
        List<StringEntity> data = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            if (i % 5 == 0) {
                data.add(new StringEntity("category" + i, StringEntity.ItemType.CATEGORY));
            } else {
                data.add(new StringEntity("item " + i, StringEntity.ItemType.CONTENT));
            }
        }
        return data;
    }
}
