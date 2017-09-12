package com.king.applib.ui.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author VanceKing
 * @since 2017/9/12.
 */

public class SimpleRecyclerView extends RecyclerView {
    private static final String TAG = "aaa";


    private boolean mLoadMoreEnable = true;
    private boolean mIsLoadingMore = false;
    private View mLoadingMoreView;


    private OnLoadMoreListener mLoadMoreListener;
    private InternalAdapter mInternalAdapter;


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

    @Override public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof AdvanceRecyclerAdapter) {
            mInternalAdapter = new InternalAdapter(adapter);
            super.setAdapter(mInternalAdapter);
        } else {
            super.setAdapter(adapter);
        }
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
                case RecyclerView.SCROLL_STATE_IDLE://表示当前并处于静止状态
                    if (!mLoadMoreEnable || mIsLoadingMore) {
                        break;
                    }
                    int visibleCount = layoutManager.getChildCount();
                    int totalCount = layoutManager.getItemCount();
                    if (visibleCount > 0
                            && layoutManager.findLastVisibleItemPosition() >= totalCount - 1
                            && totalCount > visibleCount//不满一屏时
                            && mLoadMoreListener != null) {
                        Log.i(TAG, "滑动到最后了");
                        mIsLoadingMore = true;
                        currentPage++;
                        mLoadMoreListener.loadMore(currentPage);
                    }
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING://标识当前RecyclerView处于滑动状态（手指在屏幕上）
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING://表示当前RecyclerView处于从滑动状态到静止状态（手已经离开屏幕）
                    break;
                default:
                    break;
            }
        }

        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }

    public void setLoadMoreEnabled(boolean enable) {
        mLoadMoreEnable = enable;
    }

    public void setLoadingMore() {
        mIsLoadingMore = true;
        mLoadingMoreView.setVisibility(View.VISIBLE);
    }

    public void setLoadMoreComplete() {
        mIsLoadingMore = false;
        mLoadingMoreView.setVisibility(View.GONE);
    }

    public void setLoadMoreError() {
        mIsLoadingMore = false;
        mLoadingMoreView.setVisibility(View.GONE);
    }

    public void setNoMoreData() {
        mLoadingMoreView.setVisibility(View.GONE);
    }

    // TODO: 2017/9/12 没有设置View的情况下，设置默认的More View

    /**
     * 设置加载更多的View.注意在setAdapter()之前调用.
     */
    public void setLoadMoreView(View view) {
        Adapter adapter = getAdapter();
        if (!(adapter instanceof AdvanceRecyclerAdapter)) {
            throw new IllegalStateException("must be AdvanceRecyclerAdapter");
        }
        AdvanceRecyclerAdapter recyclerAdapter = (AdvanceRecyclerAdapter) adapter;
        recyclerAdapter.addFooterView(view);
        mLoadingMoreView = view;
    }

    private TextView buildLoadingMoreView() {
        TextView textView = new TextView(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setText("加载中...");
        return textView;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public interface OnLoadMoreListener {
        void loadMore(int currentPage);
    }

    private class InternalAdapter extends AdvanceRecyclerAdapter {
        RecyclerView.Adapter mAdapter;

        public InternalAdapter(RecyclerView.Adapter adapter) {
            mAdapter = adapter;
        }

        @Override public int getItemLayoutRes() {
            return 0;
        }

        @Override public void convert(RecyclerHolder holder, Object item, int position) {

        }

        @Override public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override public int getItemCount() {
            if (mLoadMoreEnable) {
                return mAdapter != null ? mAdapter.getItemCount() + getFooterViewCount() : getFooterViewCount();
            } else {
                return mAdapter != null ? mAdapter.getItemCount() : 0;
            }
        }

        private int isHeaderView(int position) {
            return position = 0;
        }

        private int isFooterView(int position) {
            return getItemCount()
        }
    }
}
