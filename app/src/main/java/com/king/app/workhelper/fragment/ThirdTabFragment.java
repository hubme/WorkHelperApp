package com.king.app.workhelper.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.recyclerview.MyRecyclerAdapter;
import com.king.app.workhelper.common.AppBasePageFragment;
import com.king.applib.log.Logger;

import butterknife.BindView;

public class ThirdTabFragment extends AppBasePageFragment {
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    protected int getContentLayout() {
        return R.layout.common_refresh_load_more_list;
    }

    @Override protected void initContentView(View rootView) {
        super.initContentView(rootView);
        mRefreshLayout.setEnabled(false);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new MyRecyclerAdapter(MyRecyclerAdapter.fakeData()));
    }

    @Override 
    protected void lazyData() {
        Logger.i("ThirdTabFragment fetchData");
    }

}
