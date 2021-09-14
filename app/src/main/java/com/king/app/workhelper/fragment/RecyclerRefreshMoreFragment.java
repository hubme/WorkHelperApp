package com.king.app.workhelper.fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.recyclerview.HeaderAndFooterSampleAdapter;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.model.entity.StringEntity;
import com.king.applib.ui.recyclerview.SimpleRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author VanceKing
 * @since 2017/9/12.
 */

public class RecyclerRefreshMoreFragment extends AppBaseFragment {
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_mine)
    SimpleRecyclerView mMineRecycler;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private HeaderAndFooterSampleAdapter mAdapter;
    private List<StringEntity> mDataList;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_recycler_refresh_more;
    }

    @Override
    protected void initContentView(View rootView) {
        super.initContentView(rootView);
        mRefreshLayout.setColorSchemeResources(R.color.chocolate, R.color.tomato, R.color.gold);
        mRefreshLayout.setOnRefreshListener(this::delayRefreshData);

        mDataList = HeaderAndFooterSampleAdapter.fakeData();
        mAdapter = new HeaderAndFooterSampleAdapter(mContext, mDataList);
        mMineRecycler.setAdapter(mAdapter);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mMineRecycler.setHasFixedSize(true);
        mMineRecycler.setLayoutManager(mLinearLayoutManager);

        mMineRecycler.setOnLoadMoreListener(currentPage -> delayNoMoreData());
        mMineRecycler.setDefaultLoadMoreView();
        mMineRecycler.setAutoLoadMore(true);
    }

    private void delayRefreshData() {
        mRefreshLayout.setRefreshing(true);
        mHandler.postDelayed(() -> {
            mAdapter.insertItem(0, new StringEntity("刷新得到的数据", StringEntity.ItemType.CONTENT));
            mRefreshLayout.setRefreshing(false);
        }, 2000);
    }

    private void delayLoadMoreData(int page) {
        mMineRecycler.setLoadingMore();
        mHandler.postDelayed(() -> {
            mAdapter.setAdapterData(fakeLoadMoreData(page, 2), true);
            mMineRecycler.setLoadMoreComplete();
        }, 2000);
    }

    private void delayLoadError() {
        mMineRecycler.setLoadingMore();
        mHandler.postDelayed(() -> mMineRecycler.setLoadMoreError(-1, "加载失败"), 2000);
    }

    private void delayNoMoreData() {
        mMineRecycler.setLoadingMore();
        mHandler.postDelayed(() -> mMineRecycler.setNoMoreData("- 没有更多内容 -"), 2000);
    }

    private List<StringEntity> fakeLoadMoreData(int page, int size) {
        List<StringEntity> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add(new StringEntity("第" + page + "页的第" + i + "条数据", StringEntity.ItemType.CONTENT));
        }
        return data;
    }
}
