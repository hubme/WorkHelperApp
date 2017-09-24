package com.king.applib.ui.recyclerview;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * 支持 Header、 Footer、RecyclerView 多状态的Adapter.
 *
 * @author VanceKing
 * @since 2017/9/6.
 */

public abstract class AdvanceRecyclerAdapter<E> extends RecyclerView.Adapter<RecyclerHolder> {
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

    private final List<E> mAdapterList = new ArrayList<>();

    private View mEmptyDataView;
    private LinearLayout mHeaderPanel;//添加 Header View 的容器
    private LinearLayout mFooterPanel;//添加 Footer View 的容器
    protected Context mContext;

    public AdvanceRecyclerAdapter() {

    }

    public AdvanceRecyclerAdapter(Context context) {
        mContext = context;
    }

    public AdvanceRecyclerAdapter(List<E> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        if (!mAdapterList.isEmpty()) {
            mAdapterList.clear();
        }
        mAdapterList.addAll(dataList);
    }

    public AdvanceRecyclerAdapter(Context context, List<E> dataList) {
        mContext = context;
        addDataLis(dataList, false);
    }

    public List<E> getAdapterData() {
        return mAdapterList;
    }

    public void setAdapterData(List<E> adapterData) {
        setAdapterData(adapterData, false);
    }

    public void setAdapterData(List<E> adapterData, boolean append) {
        addDataLis(adapterData, append);
        notifyDataSetChanged();
    }

    private void addDataLis(List<E> adapterData, boolean append) {
        if (isListEmpty(adapterData)) {
            return;
        }
        if (append) {
            mAdapterList.addAll(adapterData);
        } else {
            if (!isListEmpty(mAdapterList)) {
                mAdapterList.clear();
            }
            mAdapterList.addAll(adapterData);
        }
    }

    private boolean isListEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }

    public void clearAdapterData() {
        mAdapterList.clear();
        notifyDataSetChanged();
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
                    View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutRes(), parent, false);
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
                if (listPosition >= 0 && listPosition < mAdapterList.size()) {
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
                return mAdapterList.size() + getHeaderViewCount() + getFooterViewCount();
        }
    }

    @Override public int getItemViewType(int position) {
        switch (mViewState) {
            case STATE_EMPTY:
                return STATE_EMPTY;
            case STATE_ERROR:
                return STATE_ERROR;
            case STATE_LOADING:
                return STATE_LOADING;
            case STATE_NORMAL:
            default:
                if (position < getHeaderViewCount()) {
                    return VIEW_TYPE_HEADER;
                } else if (position >= mAdapterList.size() + getHeaderViewCount()) {
                    return VIEW_TYPE_FOOTER;
                } else {
                    return STATE_NORMAL;
                }
        }
    }

    @LayoutRes public abstract int getItemLayoutRes();

    @ViewState public int getViewState() {
        return mViewState;
    }

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

    public void onBindEmptyViewHolder(RecyclerHolder holder, int position) {
    }

    public abstract void convert(RecyclerHolder holder, E item, int position);

    public E getItem(int position) {
        return mAdapterList.get(position);
    }

    public void addData(int position, E entity) {
        if (position >= 0 && position <= mAdapterList.size()) {
            mAdapterList.add(position, entity);
            notifyItemInserted(position);
        }
    }

    public void deleteData(int position) {
        if (checkPosition(position)) {
            mAdapterList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void modifyData(int position, E e) {
        if (checkPosition(position)) {
            mAdapterList.remove(position);
            mAdapterList.add(position, e);
            notifyItemChanged(position);
        }
    }

    private boolean checkPosition(int position) {
        return position >= 0 && position < mAdapterList.size();
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
