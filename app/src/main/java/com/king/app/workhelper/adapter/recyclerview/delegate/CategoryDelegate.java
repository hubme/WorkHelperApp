package com.king.app.workhelper.adapter.recyclerview.delegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.model.entity.StringEntity;
import com.king.applib.adapterdelegate.AdapterDelegate;

import java.util.List;

/**
 * @author huoguangxu
 * @since 2017/6/29.
 */

public class CategoryDelegate implements AdapterDelegate<List<StringEntity>> {
    @Override public int getItemViewType() {
        return StringEntity.ItemType.CATEGORY;
    }

    @Override public boolean isForViewType(@NonNull List<StringEntity> items, int position) {
        return items.get(position).type == StringEntity.ItemType.CATEGORY;
    }

    @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_text_category, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull List<StringEntity> items, int position, @NonNull RecyclerView.ViewHolder holder) {
        ((CategoryViewHolder)holder).categoryName.setText(items.get(position).text);
    }
    
    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        private final TextView categoryName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.tv_category_name);
        }
    }
}
