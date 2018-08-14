package com.king.app.workhelper.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.recyclerview.SimpleRecyclerAdapter;
import com.king.app.workhelper.common.AppBaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * @author VanceKing
 * @since 2018/8/7.
 */
public class SmartRefreshSampleFragment extends AppBaseFragment {
    @BindView(R.id.refreshLayout) SmartRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Override protected int getContentLayout() {
        return R.layout.fragment_smart_refresh_sample;
    }

    @Override protected void initContentView(View rootView) {
        super.initContentView(rootView);

        refreshLayout.setOnRefreshListener(refreshLayout -> {
            Log.i("aaa", "onRefresh()");
            refreshLayout.finishRefresh(2000);
        });

        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            Log.i("aaa", "onLoadMore");
            refreshLayout.finishLoadMore(2000);
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SimpleRecyclerAdapter(SimpleRecyclerAdapter.fakeData(30)));
    }
}
