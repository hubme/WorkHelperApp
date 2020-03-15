package com.king.applib.ui.recyclerview;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.king.applib.ui.recyclerview.listener.OnLoadMoreHandler;

/**
 * 必须结合AdvanceRecyclerAdapter使用
 *
 * @author VanceKing
 * @since 2017/9/12.
 */

public class SimpleRecyclerView extends RecyclerView {

    private boolean mLoadMoreEnable = true;
    private boolean mIsLoadingMore = false;
    private boolean mIsAutoLoadMore = true;

    private View mRefreshHeaderView;
    private View mLoadingMoreView;

    private OnLoadMoreListener mLoadMoreListener;

    public SimpleRecyclerView(Context context) {
        this(context, null);
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        mLoadingMoreView = buildLoadingMoreView();
        addOnScrollListener(new SimpleRecyclerScrollListener());
    }

    private class SimpleRecyclerScrollListener extends RecyclerView.OnScrollListener {
        private int currentPage = 1;

        @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (!(manager instanceof LinearLayoutManager)) {
                throw new RuntimeException("only support LinearLayoutManager");// TODO: 2017/9/12 支持GridLayoutManager和StaggeredGridLayoutManager 
            }
            LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE://当前并处于静止状态
                    if (!mLoadMoreEnable || mIsLoadingMore || !mIsAutoLoadMore) {
                        break;
                    }
                    SimpleRecyclerAdapter adapter = (SimpleRecyclerAdapter) getAdapter();
                    int visibleCount = layoutManager.getChildCount();
                    int totalCount = layoutManager.getItemCount() - adapter.getFooterViewCount();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    if (visibleCount > 0
                            && lastVisibleItemPosition >= totalCount - adapter.getFooterViewCount()
                            && totalCount > visibleCount//不满一屏时
                            && mLoadMoreListener != null) {
                        mIsLoadingMore = true;
                        currentPage++;
                        mLoadMoreListener.loadMore(currentPage);
                    }
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING://当前RecyclerView处于滑动状态（手指在屏幕上）
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING://当前RecyclerView处于从滑动状态到静止状态（手已经离开屏幕）
                    break;
                default:
                    break;
            }
        }

        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }

    @Override public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        checkAdapter();
    }

    private void checkAdapter() {
        Adapter adapter = getAdapter();
        if (!(adapter instanceof SimpleRecyclerAdapter)) {
            throw new IllegalStateException("adapter must be instance of SimpleRecyclerAdapter");
        }
    }

    public void setAutoLoadMore(boolean autoLoadMore) {
        mIsAutoLoadMore = autoLoadMore;
    }

    public void setLoadMoreEnabled(boolean enable) {
        mLoadMoreEnable = enable;
    }

    public void setLoadingMore() {
        mIsLoadingMore = true;
        if (mLoadingMoreView instanceof OnLoadMoreHandler) {
            ((OnLoadMoreHandler) mLoadingMoreView).onLoading();
        } else {
            mLoadingMoreView.setVisibility(View.VISIBLE);
        }
    }

    public void setLoadMoreComplete() {
        mIsLoadingMore = false;
        if (mLoadingMoreView instanceof OnLoadMoreHandler) {
            ((OnLoadMoreHandler) mLoadingMoreView).onLoadComplete();
        } else {
            mLoadingMoreView.setVisibility(View.INVISIBLE);
        }
    }

    public void setLoadMoreError(int code, String desc) {
        mIsLoadingMore = false;
        if (mLoadingMoreView instanceof OnLoadMoreHandler) {
            ((OnLoadMoreHandler) mLoadingMoreView).onLoadError(code, desc);
        } else {
            mLoadingMoreView.setVisibility(View.INVISIBLE);
        }
    }

    public void setNoMoreData(String desc) {
        mIsLoadingMore = false;
        if (mLoadingMoreView instanceof OnLoadMoreHandler) {
            ((OnLoadMoreHandler) mLoadingMoreView).onNoMoreData(desc);
        } else {
            mLoadingMoreView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置加载更多的View.注意在setAdapter()之后调用.
     */
    public void setDefaultLoadMoreView() {
        setLoadMoreView(mLoadingMoreView);
    }

    /**
     * 设置加载更多的View.注意在setAdapter()之后调用.
     */
    public void setLoadMoreView(View view) {
        SimpleRecyclerAdapter recyclerAdapter = (SimpleRecyclerAdapter) getAdapter();
        recyclerAdapter.addFooterView(view);
        mLoadingMoreView = view;
    }

    private View buildLoadingMoreView() {
        return new LoadMoreView(getContext());
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public interface OnLoadMoreListener {
        void loadMore(int currentPage);
    }
}
