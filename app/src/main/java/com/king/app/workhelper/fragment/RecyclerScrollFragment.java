package com.king.app.workhelper.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.common.TextWatcherAdapter;
import com.king.applib.ui.recyclerview.BaseRecyclerViewAdapter;
import com.king.applib.ui.recyclerview.RecyclerHolder;
import com.king.applib.ui.recyclerview.listener.RecyclerItemTouchListener;
import com.king.applib.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/9/12.
 */

public class RecyclerScrollFragment extends AppBaseFragment {
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.et_position) EditText mPositionEt;
    private int overallXScrol = 0;
    private boolean mIsScroll = true;

    private List<ItemData> itemData = new ArrayList<>();
    private int mPosition;
    private LinearLayoutManager layoutManager;

    @Override protected void initInitialData() {
        super.initInitialData();
        for (int i = 0; i < 20; i++) {
            itemData.add(new ItemData("7/" + i));
        }
    }

    @Override protected void initData() {
    }

    @Override protected int getContentLayout() {
        return R.layout.fragment_recycler_scroll;
    }

    @Override protected void initContentView(View rootView) {
        super.initContentView(rootView);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);

        mPositionEt.addTextChangedListener(new TextWatcherAdapter() {
            @Override public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                mPosition = NumberUtil.getInt(s.toString(), 0);
            }
        });


        int itemWidth = getResources().getDimensionPixelSize(R.dimen.list_item_width);
        int padding = Math.round((float) (getResources().getDisplayMetrics().widthPixels - itemWidth) / 2);
        mRecyclerView.setPadding(padding, 0, padding, 0);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(mRecyclerView,
                new RecyclerItemTouchListener.OnRecyclerItemListenerAdapter() {
                    @Override public void onItemClick(View view, int position) {
                        mRecyclerView.smoothScrollToPosition(position);
                    }
                }));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int offset = (overallXScrol) % itemWidth;
                    Log.i("aaa", "X总偏移量：" + overallXScrol + "。offset: " + offset);

                    int asbOffset = Math.abs(offset);
                    if (asbOffset <= itemWidth / 2) {
                        mRecyclerView.smoothScrollBy(-offset, 0);
                    } else if (asbOffset > itemWidth / 2 && asbOffset < itemWidth) {//reverseLayout 为 true 和 false 时情况不同
                        mRecyclerView.smoothScrollBy(offset > 0 ? itemWidth - asbOffset : asbOffset - itemWidth, 0);
                    }

                    //通过smoothScrollBy()滚动到指定位置，不会再滚动了。
                    if (offset == 0) {
                        int index = overallXScrol / itemWidth;
                        Log.i("aaa", "index: " + Math.abs(index));
                    }

                }
            }

            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Log.i("aaa", "dx = " + dx);
                overallXScrol += dx;
            }
        });
        MyAdapter adapter = new MyAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.setAdapterData(itemData);

    }

    @Override public void onDestroyView() {
        //滚动中销毁页面，onScrollStateChanged() 中报 NPE.
        mRecyclerView.clearOnScrollListeners();
        super.onDestroyView();
    }

    @OnClick(R.id.btn_go)
    public void onButtonClick(View view) {
        //无过度。滚动到指定位置并偏移指定大小。item可见时也可滚动。
//        layoutManager.scrollToPositionWithOffset(mPosition, 50);

        //无过度。滚动到指定位置。item可见时不会滚动。
//        mRecyclerView.scrollToPosition(mPosition);

        //平滑滚动。滚动到指定位置。item可见时不会滚动。
//        mRecyclerView.smoothScrollToPosition(mPosition);

        //不支持
//        mRecyclerView.scrollTo(400, 0);

        //无过度。滚动指定的偏移量。
//        mRecyclerView.scrollBy(100, 0);

        mRecyclerView.smoothScrollBy(300, 0);

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
