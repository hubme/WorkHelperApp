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
 * 带泛型
 *
 * @author huoguangxu
 * @since 2017/6/29.
 */

public class BasicMultipleRecyclerAdapter extends RecyclerView.Adapter<BasicMultipleRecyclerAdapter.ContentViewHolder> {
    private List<StringEntity> mStrings = new ArrayList<>();

    public BasicMultipleRecyclerAdapter() {
    }

    public BasicMultipleRecyclerAdapter(List<StringEntity> strings) {
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

    //如果指定泛型，将不能通过 multi type 返回不同的ViewHolder
    @Override public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == StringEntity.ItemType.CONTENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_simple_text_view, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_text_category, parent, false);
        }
        return new ContentViewHolder(view);
    }

    @Override public void onBindViewHolder(ContentViewHolder holder, int position) {
        final StringEntity entity = mStrings.get(position);
        if (holder.getItemViewType() == StringEntity.ItemType.CONTENT) {
            holder.content.setText(entity.text);
        } else {
            TextView textView = holder.categoryName;
            if (textView != null) {
                textView.setText(entity.text);
            }
        }
    }

    @Override public int getItemViewType(int position) {
        return mStrings.get(position).type;
    }

    @Override public int getItemCount() {
        return mStrings == null ? 0 : mStrings.size();
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        private final TextView content;
        private final TextView categoryName;

        private ContentViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.tv_item_input);
            categoryName = (TextView) itemView.findViewById(R.id.tv_category_name);
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
