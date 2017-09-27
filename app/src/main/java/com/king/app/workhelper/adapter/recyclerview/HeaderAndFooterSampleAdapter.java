package com.king.app.workhelper.adapter.recyclerview;

import android.content.Context;

import com.king.app.workhelper.R;
import com.king.app.workhelper.model.entity.StringEntity;
import com.king.applib.ui.recyclerview.RecyclerHolder;
import com.king.applib.ui.recyclerview.SimpleRecyclerAdapter;
import com.king.applib.ui.recyclerview.SwipeMenuLayout;
import com.king.applib.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VanceKing
 * @since 2017/9/7.
 */

public class HeaderAndFooterSampleAdapter extends SimpleRecyclerAdapter<StringEntity> {

    public HeaderAndFooterSampleAdapter(Context context) {
        super(context);
    }

    public HeaderAndFooterSampleAdapter(Context context, List<StringEntity> dataList) {
        super(context, dataList);
    }

    @Override public int getItemLayoutRes() {
        return R.layout.layout_simple_text_view;
    }

    @Override public void convert(RecyclerHolder holder, StringEntity item, int position) {
        holder.setText(R.id.tv_item_input, item.text);
        holder.setOnClickListener(R.id.tv_item_input, v -> ToastUtil.showShort(item.text));
        SwipeMenuLayout swipeMenuLayout = holder.getView(R.id.swipe_layout);

        holder.setOnClickListener(R.id.btnTop, v -> ToastUtil.showShort("置顶"));
        holder.setOnClickListener(R.id.btnUnRead, v -> ToastUtil.showShort("未读"));
        holder.setOnClickListener(R.id.btnDelete, v -> {
            ToastUtil.showShort("删除");
            swipeMenuLayout.smoothClose();
        });
    }

    public static List<StringEntity> fakeData() {
        List<StringEntity> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new StringEntity("item " + i, StringEntity.ItemType.CONTENT));
        }
        return data;
    }
}
