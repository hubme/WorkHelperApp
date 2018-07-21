package com.king.app.workhelper.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.common.TextWatcherAdapter;
import com.king.applib.ui.recyclerview.BaseRecyclerViewAdapter;
import com.king.applib.ui.recyclerview.RecyclerHolder;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.et_position) EditText mPositionEt;
    
    private List<ItemData> itemData = new ArrayList<>();
    private int mPosition;
    private LinearLayoutManager manager;

    @Override protected void initInitialData() {
        super.initInitialData();
        for (int i = 0; i < 50; i++) {
            itemData.add(new ItemData("7/" + i));
        }
    }

    @Override protected void initData() {
        Log.i("aaa", "屏幕一半：" + ExtendUtil.getScreenWidth() / 2);
    }

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();
        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mPositionEt.addTextChangedListener(new TextWatcherAdapter() {
            @Override public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                mPosition = NumberUtil.getInt(s.toString(), 0);
            }
        });
        
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.i("aaa", "静止");
                }
            }

            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("aaa", "dx = " + dx);
            }
        });
        MyAdapter adapter = new MyAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.setAdapterData(itemData);
        
    }

    private void moveToPosition(int p, int fir, int end) {
        if (p <= fir) {
            mRecyclerView.scrollToPosition(p);
        } else if (p <= end) {
            int top = mRecyclerView.getChildAt(p - fir).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(p);    //先让当前view滚动到列表内
        }
    }

    @OnClick(R.id.btn_go)
    public void onButtonClick(View view) {
        moveToPosition(mPosition, manager.findFirstVisibleItemPosition(), manager.findLastVisibleItemPosition());
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
