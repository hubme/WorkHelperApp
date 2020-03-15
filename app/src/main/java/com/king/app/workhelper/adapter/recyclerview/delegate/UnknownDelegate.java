package com.king.app.workhelper.adapter.recyclerview.delegate;

import android.graphics.Color;
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

public class UnknownDelegate extends AdapterDelegate<List<StringEntity>> {
    @Override protected boolean isForViewType(@NonNull List<StringEntity> items, int position) {
        return items.get(position).type == StringEntity.ItemType.UNKNOWN;
    }

    @NonNull @Override protected RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new UnknownViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_text_category, parent, false));
    }

    @Override protected void onBindViewHolder(@NonNull List<StringEntity> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        UnknownViewHolder unknownHolder = ((UnknownViewHolder) holder);
        unknownHolder.unknown.setText("nothing");
        unknownHolder.unknown.setBackgroundColor(Color.GRAY);
    }

    public static class UnknownViewHolder extends RecyclerView.ViewHolder {
        private final TextView unknown;

        public UnknownViewHolder(View itemView) {
            super(itemView);
            unknown = (TextView) itemView.findViewById(R.id.tv_category_name);
        }
    }
}
