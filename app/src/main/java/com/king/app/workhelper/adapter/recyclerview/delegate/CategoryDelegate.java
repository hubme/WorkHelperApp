package com.king.app.workhelper.adapter.recyclerview.delegate;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.model.entity.StringEntity;
import com.king.applib.ui.recyclerview.multiitemdelegate.AdapterDelegate;

import java.util.List;

/**
 * @author VanceKing
 * @since 2017/6/29.
 */

public class CategoryDelegate extends AdapterDelegate<List<StringEntity>> {
    @Override protected boolean isForViewType(@NonNull List<StringEntity> items, int position) {
        return items.get(position).type == StringEntity.ItemType.CATEGORY;
    }

    @NonNull @Override protected RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_text_category, parent, false));
    }

    @Override protected void onBindViewHolder(@NonNull List<StringEntity> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
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
