package com.king.app.workhelper.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.ui.recyclerview.BaseRecyclerViewAdapter;
import com.king.applib.ui.recyclerview.RecyclerDivider;
import com.king.applib.ui.recyclerview.RecyclerHolder;
import com.king.applib.ui.recyclerview.listener.RecyclerItemTouchListener;
import com.king.applib.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author VanceKing
 * @since 2017/7/18.
 */

public class RecyclerSwipeMenuFragment extends AppBaseFragment {
    @BindView(R.id.tv_recycler_view) RecyclerView mRecyclerView;

    @Override protected int getContentLayout() {
        return R.layout.fragment_swipe_menu;
    }

    @Override protected void initData() {
        super.initData();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        RecyclerDivider divider = new RecyclerDivider(RecyclerDivider.VERTICAL, ContextCompat.getColor(mContext, R.color.chocolate));
        mRecyclerView.addItemDecoration(divider);

        SwipeMenuAdapter adapter = new SwipeMenuAdapter(fakeData());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(mRecyclerView,
                new RecyclerItemTouchListener.OnRecyclerItemListenerAdapter() {
                    @Override public void onItemClick(View view, int position) {
                        super.onItemClick(view, position);
                        showToast(adapter.getItem(position));
                    }
                }));
    }

    private List<String> fakeData() {
        List<String> data = new ArrayList<>(25);
        for (int i = 0; i < 25; i++) {
            data.add("item " + i);
        }
        return data;
    }

    private static class SwipeMenuAdapter extends BaseRecyclerViewAdapter<String> {
        public SwipeMenuAdapter(List<String> adapterData) {
            super(adapterData);
        }

        @Override public void convert(RecyclerHolder holder, String item, int position) {
            holder.setText(R.id.tv_item_content, item);
            holder.setOnClickListener(R.id.tv_item_content, v -> ToastUtil.showShort(item));

            holder.setOnClickListener(R.id.tv_menu_share, v -> ToastUtil.showShort("分享"));
            holder.setOnClickListener(R.id.tv_menu_delete, v -> ToastUtil.showShort("删除"));
            holder.setOnClickListener(R.id.tv_menu_collection, v -> ToastUtil.showShort("收藏"));
        }

        @Override public int getItemLayoutRes(int viewType) {
            return R.layout.list_swipe_menu_item;
        }
    }
}
