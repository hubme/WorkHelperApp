package com.king.app.workhelper.adapter.recyclerview;

import com.king.app.workhelper.R;
import com.king.app.workhelper.model.entity.StringEntity;
import com.king.applib.ui.recyclerview.BaseRecyclerViewAdapter;
import com.king.applib.ui.recyclerview.RecyclerHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VanceKing
 * @since 2017/6/29.
 */

public class MyRecyclerAdapter extends BaseRecyclerViewAdapter<StringEntity> {
    public MyRecyclerAdapter(List<StringEntity> adapterData) {
        super(adapterData);
    }

    @Override public void convert(RecyclerHolder holder, StringEntity item, int position) {
        if (item.type == StringEntity.ItemType.CONTENT) {
            holder.setText(R.id.tv_item_input, item.text);
        }
    }

    @Override public int getItemLayoutRes(int viewType) {
        return R.layout.layout_simple_text_view;
    }

    @Override public int getItemViewType(int position) {
        return getAdapterData().get(position).type;
    }

    public static List<StringEntity> fakeData() {
        List<StringEntity> data = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            data.add(new StringEntity("item " + i, StringEntity.ItemType.CONTENT));

        }
        return data;
    }
}
