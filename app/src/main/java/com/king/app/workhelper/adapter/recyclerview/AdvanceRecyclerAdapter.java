package com.king.app.workhelper.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.king.app.workhelper.R;
import com.king.applib.ui.recyclerview.RecyclerHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VanceKing
 * @since 2017/9/6.
 */

public abstract class AdvanceRecyclerAdapter<E> extends RecyclerView.Adapter<RecyclerHolder> {
    public static final int VIEW_TYPE_HEADER = 0x0010;
    public static final int VIEW_TYPE_FOOTER = 0x0011;

    @IntDef({VIEW_TYPE_HEADER, VIEW_TYPE_FOOTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewType {

    }

    private final List<E> mAdapterList = new ArrayList<>();

    private LinearLayout mHeaderPanel;//添加 Header View 的容器
    private LinearLayout mFooterPanel;//添加 Footer View 的容器
    private Context mContext;
    
    public AdvanceRecyclerAdapter(Context context) {
        mContext = context;
    }
    
    public AdvanceRecyclerAdapter(Context context, List<E> dataList) {
        mContext = context;
        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        if (!mAdapterList.isEmpty()) {
            mAdapterList.clear();
        }
        mAdapterList.addAll(dataList);
    }

    @Override public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new RecyclerHolder(mHeaderPanel);
            case VIEW_TYPE_FOOTER:
                return new RecyclerHolder(mFooterPanel);
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_simple_text_view, parent, false);
                return new RecyclerHolder(view);
        }
    }

    @Override public void onBindViewHolder(RecyclerHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:

                break;
            case VIEW_TYPE_FOOTER:

                break;
            default:
                int listPosition = position - getHeaderViewCount();
                if (listPosition >= 0 && listPosition < mAdapterList.size()) {
                    E item = getItem(listPosition);
                    if (item != null) {
                        convert(holder, item, listPosition);
                    }
                }
                break;
        }
    }

    @Override public int getItemCount() {
        return mAdapterList.size() + getHeaderViewCount() + getFooterViewCount();
    }

    @Override public int getItemViewType(int position) {
        if (position < getHeaderViewCount()) {
            return VIEW_TYPE_HEADER;
        } else if (position >= mAdapterList.size() + getHeaderViewCount()) {
            return VIEW_TYPE_FOOTER;
        } else {
            return 0;
        }
    }

    @LayoutRes public abstract int getItemLayoutRes();

    public abstract void convert(RecyclerHolder holder, E item, int position);

    public E getItem(int position) {
        return mAdapterList.get(position);
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

    public int getHeaderViewCount() {
        return null == mHeaderPanel ? 0 : 1;
    }

    public int getFooterViewCount() {
        return null == mFooterPanel ? 0 : 1;
    }
}
