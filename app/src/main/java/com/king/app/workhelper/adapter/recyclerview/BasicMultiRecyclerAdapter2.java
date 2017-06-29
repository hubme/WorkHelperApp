package com.king.app.workhelper.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.model.entity.StringEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 不带泛型
 *
 * @author huoguangxu
 * @since 2017/6/29.
 */

public class BasicMultiRecyclerAdapter2 extends RecyclerView.Adapter {
    private List<StringEntity> mStrings = new ArrayList<>();

    public BasicMultiRecyclerAdapter2() {
    }

    public BasicMultiRecyclerAdapter2(List<StringEntity> strings) {
        if (strings != null && !strings.isEmpty()) {
            mStrings.addAll(strings);
        }
    }

    public void setAdapterData(List<StringEntity> data) {
        setAdapterData(data, false);
    }

    public void setAdapterData(List<StringEntity> data, boolean append) {
        if (append) {
            mStrings.addAll(data);
        } else {
            mStrings.clear();
            mStrings.addAll(data);
        }
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == StringEntity.ItemType.CONTENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_simple_text_view, parent, false);
            return new ContentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_text_category, parent, false);
            return new CategoryViewHolder(view);
        }
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final StringEntity entity = mStrings.get(position);
        if (holder.getItemViewType() == StringEntity.ItemType.CONTENT) {
            ContentViewHolder contentHolder = (ContentViewHolder) holder;
            contentHolder.content.setText(entity.text);
        } else {
            CategoryViewHolder categoryHolder = (CategoryViewHolder) holder;
            TextView textView = categoryHolder.categoryName;
            textView.setText(entity.text);
        }
    }

    @Override public int getItemViewType(int position) {
        return mStrings.get(position).type;
    }

    @Override public int getItemCount() {
        return mStrings == null ? 0 : mStrings.size();
    }

    private static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryName;

        private CategoryViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.tv_category_name);
        }

    }
    
    private static class ContentViewHolder extends RecyclerView.ViewHolder {
        private final TextView content;

        private ContentViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.tv_item_input);
        }

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
