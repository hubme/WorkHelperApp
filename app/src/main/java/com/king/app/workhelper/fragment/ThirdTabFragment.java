package com.king.app.workhelper.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.recyclerview.MyRecyclerAdapter;
import com.king.app.workhelper.common.AppBasePageFragment;
import com.king.applib.log.Logger;
import com.king.applib.ui.recyclerview.BaseRecyclerViewAdapter;
import com.king.applib.ui.recyclerview.RecyclerHolder;
import com.king.applib.util.ExtendUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ThirdTabFragment extends AppBasePageFragment {
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.top_recycler) RecyclerView mTopRecyclerView;

    private final List<ItemData> itemList = new ArrayList<>();
    private int overallXScroll = 0;
    int itemWidth;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_thirt_tab;
    }

    @Override protected void initInitialData() {
        super.initInitialData();
        itemWidth = getResources().getDimensionPixelSize(R.dimen.list_item_width);
        for (int i = 0; i < 50; i++) {
            itemList.add(new ItemData("7/" + i));
        }
    }

    @Override protected void initContentView(View rootView) {
        super.initContentView(rootView);

        mTopRecyclerView.setHasFixedSize(true);
        mTopRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        int padding = (ExtendUtil.getScreenWidth() - itemWidth) / 2;
        mTopRecyclerView.setPadding(padding, 0, padding, 0);
        MyAdapter myAdapter = new MyAdapter(itemList);
        mTopRecyclerView.setAdapter(myAdapter);
        mTopRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int offset = (overallXScroll) % itemWidth;

                    int asbOffset = Math.abs(offset);
                    if (asbOffset <= itemWidth / 2) {
                        mTopRecyclerView.smoothScrollBy(-offset, 0);
                    } else if (asbOffset > itemWidth / 2 && asbOffset < itemWidth) {//reverseLayout 为 true 和 false 时情况不同
                        mTopRecyclerView.smoothScrollBy(offset > 0 ? itemWidth - asbOffset : asbOffset - itemWidth, 0);
                    }

                    //通过smoothScrollBy()滚动到指定位置，不会再滚动了。
                    if (offset == 0) {
                        int index = overallXScroll / itemWidth;
                        Log.i("aaa", "index: " + Math.abs(index));
                    }

                }
            }

            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                overallXScroll += dx;
            }
        });
        
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerView.setAdapter(new MyRecyclerAdapter(MyRecyclerAdapter.fakeData()));
    }

    @Override 
    protected void lazyData() {
        Logger.i("ThirdTabFragment fetchData");
    }

    private static class MyAdapter extends BaseRecyclerViewAdapter<ItemData> {
        public MyAdapter() {
            super();
        }

        public MyAdapter(List<ItemData> adapterData) {
            super(adapterData);
        }

        @Override public void convert(RecyclerHolder holder, ItemData item, int position) {
            holder.setText(R.id.tv_date_time, item.getLabel());
        }

        @Override public int getItemLayoutRes(int viewType) {
            return R.layout.layout_item;
        }
    }

    private static class ItemData {
        private String label;

        public ItemData(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
