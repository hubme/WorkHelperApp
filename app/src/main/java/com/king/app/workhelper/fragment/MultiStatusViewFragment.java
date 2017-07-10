package com.king.app.workhelper.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.ui.customview.MultiStatusView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/7/10.
 */

public class MultiStatusViewFragment extends AppBaseFragment {
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.multi_status_view) MultiStatusView mMultiStatusView;

    @Override protected int getContentLayout() {
        return R.layout.fragment_multi_status_view;
    }

    @Override protected void initContentView(View rootView) {
        super.initContentView(rootView);
        mMultiStatusView.showContentView(R.layout.layout_statsu_view_content);
    }

    @Override
    protected void initData() {
        super.initData();
        mRefreshLayout.setOnRefreshListener(() -> {
            mRefreshLayout.setRefreshing(false);
            mMultiStatusView.showContentView(R.layout.layout_statsu_view_content);
        });
    }

    @OnClick(R.id.tv_status_view_error)
    public void onStatusViewErrorClick() {
        mMultiStatusView.showContentView(R.layout.layout_statsu_view_error);
    }

    @OnClick(R.id.tv_status_view_empty)
    public void onStatusViewEmptyClick() {
        mMultiStatusView.showContentView(R.layout.layout_statsu_view_empty);
    }
}
