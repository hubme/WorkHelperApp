package com.king.app.workhelper.fragment;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
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
    private View mEmptyView;
    private View mErrorView;

    @Override protected int getContentLayout() {
        return R.layout.fragment_multi_status_view;
    }

    @Override protected void initContentView(View rootView) {
        super.initContentView(rootView);
       
    }

    @Override
    protected void initData() {
        super.initData();
        mRefreshLayout.setOnRefreshListener(() -> {
            mRefreshLayout.setRefreshing(false);
            mMultiStatusView.showContentView();
        });
    }

    @OnClick(R.id.tv_status_view_error)
    public void onStatusViewErrorClick() {
        if (mErrorView == null) {
            mErrorView = inflateView(mContext, R.layout.layout_statsu_view_error);
        }
        mMultiStatusView.showStatusView(mErrorView);
    }

    @OnClick(R.id.tv_status_view_empty)
    public void onStatusViewEmptyClick() {
        if (mEmptyView == null) {
            mEmptyView = inflateView(mContext, R.layout.layout_statsu_view_empty);
        }
        mMultiStatusView.showStatusView(mEmptyView);
    }
    
    @OnClick(R.id.tv_content_view)
    public void onContentViewClick() {
        mMultiStatusView.showContentView();
    }

    private View inflateView(Context context, @LayoutRes int layoutRes) {
        return LayoutInflater.from(context).inflate(layoutRes, null, false);
    }
}
