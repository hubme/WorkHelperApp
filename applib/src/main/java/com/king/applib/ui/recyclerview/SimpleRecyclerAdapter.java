package com.king.applib.ui.recyclerview;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * @author VanceKing
 * @since 2017/9/27.
 */

public abstract class SimpleRecyclerAdapter<E> extends BaseRecyclerViewAdapter<E> {
    public static final int VIEW_TYPE_CONTENT = 0;
    public static final int VIEW_TYPE_HEADER = 1000;
    public static final int VIEW_TYPE_FOOTER = 1001;

    @IntDef({VIEW_TYPE_CONTENT, VIEW_TYPE_HEADER, VIEW_TYPE_FOOTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewType {

    }

    public static final int STATE_NORMAL = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_EMPTY = 2;
    public static final int STATE_ERROR = 3;

    @IntDef({STATE_NORMAL, STATE_LOADING, STATE_EMPTY, STATE_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewState {
    }

    @ViewState private int mViewState = STATE_NORMAL;

    private View mEmptyDataView;
    private LinearLayout mHeaderPanel;//添加 Header View 的容器
    private LinearLayout mFooterPanel;//添加 Footer View 的容器

    private Context mContext;

    public SimpleRecyclerAdapter(Context context) {
        mContext = context;
    }

    public SimpleRecyclerAdapter(List<E> adapterData) {
        super(adapterData);
    }

    public SimpleRecyclerAdapter(Context context, List<E> adapterData) {
        super(adapterData);
        mContext = context;
        registerAdapterDataObserver(mDataObserver);
    }

    @Override public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case STATE_EMPTY:
            case STATE_ERROR:
            case STATE_LOADING:
                return new RecyclerHolder(mEmptyDataView);
            case STATE_NORMAL:
            default:
                if (viewType == VIEW_TYPE_HEADER) {
                    return new RecyclerHolder(mHeaderPanel);
                } else if (viewType == VIEW_TYPE_FOOTER) {
                    return new RecyclerHolder(mFooterPanel);
                } else {
                    View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutRes(viewType), parent, false);
                    return new RecyclerHolder(view);
                }
        }
    }

    @Override public void onBindViewHolder(RecyclerHolder holder, int position) {
        switch (getItemViewType(position)) {
            case STATE_EMPTY:
                onBindEmptyViewHolder(holder, position);
                break;
            case STATE_NORMAL:
                int listPosition = position - getHeaderViewCount();
                if (listPosition >= 0 && listPosition < super.getItemCount()) {
                    E item = getItem(listPosition);
                    if (item != null) {
                        convert(holder, item, listPosition);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override public int getItemCount() {
        switch (mViewState) {
            case STATE_EMPTY:
            case STATE_ERROR:
            case STATE_LOADING:
                return 1;
            case STATE_NORMAL:
            default:
                return super.getItemCount() + getHeaderViewCount() + getFooterViewCount();
        }
    }

    @Override public int getItemViewType(int position) {
        switch (mViewState) {
            case STATE_LOADING:
                return STATE_LOADING;
            case STATE_EMPTY:
                return STATE_EMPTY;
            case STATE_ERROR:
                return STATE_ERROR;
            case STATE_NORMAL:
            default:
                if (position < getHeaderViewCount()) {
                    return VIEW_TYPE_HEADER;
                } else if (position >= super.getItemCount() + getHeaderViewCount()) {
                    return VIEW_TYPE_FOOTER;
                } else {
                    return STATE_NORMAL;
                }
        }
    }

    @ViewState public int getViewState() {
        return mViewState;
    }

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
//            notifyItemRangeChanged(positionStart + getHeaderViewCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
//            notifyItemRangeInserted(positionStart + getHeaderViewCount(), itemCount);
            Log.i("aaa", "positionStart: " + positionStart + ";itemCount: " + itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
//            notifyItemRangeRemoved(positionStart + getHeaderViewCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            int headerViewsCountCount = getHeaderViewCount();
//            notifyItemRangeChanged(fromPosition + headerViewsCountCount, toPosition + headerViewsCountCount + itemCount);
        }
    };

    public void setViewState(@ViewState int viewState, View view) {
        if (mViewState == viewState) {
            return;
        }
        mViewState = viewState;
        switch (mViewState) {
            case STATE_NORMAL:

                break;
            case STATE_EMPTY:
                mEmptyDataView = view;
                break;
            case STATE_ERROR:

                break;
            case STATE_LOADING:

                break;
            default:

                break;
        }
        notifyDataSetChanged();
    }

    public void setContentState(List<E> adapterData) {
        setContentState(adapterData, false);
    }

    public void setContentState(List<E> adapterData, boolean append) {
        mViewState = STATE_NORMAL;
        addDataList(adapterData, append);
        notifyDataSetChanged();
    }

    public void onBindEmptyViewHolder(RecyclerHolder holder, int position) {
    }

    private void checkHeaderPanel() {
        if (mHeaderPanel == null) {
            mHeaderPanel = new LinearLayout(mContext);
            mHeaderPanel.setOrientation(LinearLayout.VERTICAL);
            mHeaderPanel.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    public void addHeaderView(View header) {
        addHeaderView(header, 0);
    }

    public void addHeaderView(View header, int index) {
        checkHeaderPanel();
        if (index >= 0 && index <= mHeaderPanel.getChildCount()) {
            mHeaderPanel.addView(header, index);
            notifyDataSetChanged();
        }
    }

    public void addHeaderViews(List<View> headers) {
        checkHeaderPanel();
        for (View header : headers) {
            if (header == null) {
                continue;
            }
            addHeaderView(header);
        }
        notifyDataSetChanged();
    }

    public void removeHeaderView(View header) {
        if (mHeaderPanel != null && header != null) {
            mHeaderPanel.removeView(header);
            notifyDataSetChanged();
        }
    }

    public void removeHeaderView(int index) {
        if (mHeaderPanel == null || index < 0 || index >= mHeaderPanel.getChildCount()) {
            return;
        }
        mHeaderPanel.removeViewAt(index);
        notifyDataSetChanged();
    }

    private void checkFooterPanel() {
        if (mFooterPanel == null) {
            mFooterPanel = new LinearLayout(mContext);
            mFooterPanel.setOrientation(LinearLayout.VERTICAL);
            mFooterPanel.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    public void addFooterView(View header) {
        addFooterView(header, 0);
    }

    public void addFooterView(View header, int index) {
        checkFooterPanel();
        if (index >= 0 && index <= mFooterPanel.getChildCount()) {
            mFooterPanel.addView(header, index);
            notifyDataSetChanged();
        }
    }

    public void addFooterViews(List<View> footers) {
        checkFooterPanel();
        for (View footer : footers) {
            if (footer == null) {
                continue;
            }
            addFooterView(footer);
        }
    }

    public void removeFooterView(View footer) {
        if (mFooterPanel != null && footer != null) {
            mFooterPanel.removeView(footer);
            notifyDataSetChanged();
        }
    }

    public void removeFooterView(int index) {
        if (mFooterPanel == null || index < 0 || index >= mFooterPanel.getChildCount()) {
            return;
        }
        mFooterPanel.removeViewAt(index);
        notifyDataSetChanged();
    }

    public int getHeaderViewCount() {
        return null == mHeaderPanel ? 0 : 1;
    }

    public int getFooterViewCount() {
        return null == mFooterPanel ? 0 : 1;
    }
}
