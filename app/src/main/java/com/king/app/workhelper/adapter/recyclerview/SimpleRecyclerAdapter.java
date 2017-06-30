package com.king.app.workhelper.adapter.recyclerview;

import android.support.annotation.LayoutRes;

import com.king.app.workhelper.R;
import com.king.app.workhelper.model.entity.StringEntity;
import com.king.applib.ui.recyclerview.BaseRecyclerViewAdapter;
import com.king.applib.ui.recyclerview.RecyclerHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huoguangxu
 * @since 2017/6/29.
 */

public class SimpleRecyclerAdapter extends BaseRecyclerViewAdapter<StringEntity>{
    public SimpleRecyclerAdapter(@LayoutRes int layoutRes) {
        super(layoutRes);
    }

    public SimpleRecyclerAdapter(@LayoutRes int layoutRes, List<StringEntity> adapterData) {
        super(layoutRes, adapterData);
    }

    @Override public void convert(RecyclerHolder holder, StringEntity item, int position) {
        if (item.type == StringEntity.ItemType.CATEGORY) {
            holder.setText(R.id.tv_item_input, item.text);
        }
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
