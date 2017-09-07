package com.king.app.workhelper.adapter.recyclerview;

import com.king.app.workhelper.R;
import com.king.app.workhelper.model.entity.StringEntity;
import com.king.applib.ui.recyclerview.RecyclerHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VanceKing
 * @since 2017/9/7.
 */

public class HeaderAndFooterAdapter extends AdvanceRecyclerAdapter<StringEntity> {
    public HeaderAndFooterAdapter(List<StringEntity> dataList) {
        super(dataList);
    }

    @Override public int getItemLayoutRes() {
        return R.layout.layout_simple_text_view;
    }

    @Override public void convert(RecyclerHolder holder, StringEntity item, int position) {
        holder.setText(R.id.tv_item_input, item.text);
    }

    public static List<StringEntity> fakeData() {
        List<StringEntity> data = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            data.add(new StringEntity("item " + i, StringEntity.ItemType.CONTENT));
        }
        return data;
    }
}
