package com.king.app.workhelper.adapter.recyclerview;

import com.king.app.workhelper.R;
import com.king.applib.ui.recyclerview.BaseRecyclerViewAdapter;
import com.king.applib.ui.recyclerview.RecyclerHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VanceKing
 * @since 2018/8/7.
 */
public class SimpleRecyclerAdapter extends BaseRecyclerViewAdapter<String> {
    public SimpleRecyclerAdapter() {
    }

    public SimpleRecyclerAdapter(List<String> adapterData) {
        super(adapterData);
    }

    @Override public void convert(RecyclerHolder holder, String item, int position) {
        holder.setText(R.id.tv_text, item);
    }

    @Override public int getItemLayoutRes(int viewType) {
        return R.layout.layout_single_text_view;
    }

    public static List<String> fakeData(int size) {
        if (size <= 0) {
            return null;
        }
        final List<String> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add(String.valueOf(i));

        }
        return data;
    }
}
