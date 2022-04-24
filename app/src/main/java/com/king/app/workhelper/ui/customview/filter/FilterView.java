package com.king.app.workhelper.ui.customview.filter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FilterView extends LinearLayout {
    public static final String BRAND = "Brand";
    public static final String MODEL = "Model";

    private OnFilterChanged mOnFilterChanged;
    private HashMap<String, FilterGroupView> mChildren;

    public FilterView(Context context) {
        this(context, null);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChildren();
    }

    private void initChildren() {
        mChildren = new LinkedHashMap<>(2);
        mChildren.put(BRAND, new FilterGroupView(getContext()));
        mChildren.put(MODEL, new FilterGroupView(getContext()));
        //按 Map 顺序添加 View
        Collection<FilterGroupView> groupViews = mChildren.values();
        for (FilterGroupView view : groupViews) {
            addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            view.setOnFilterChanged(new FilterGroupView.OnFilterChanged() {
                @Override
                public void onChanged(FilterGroupView groupView, CheckedTextView checkedFilterView) {
                    if (mOnFilterChanged != null) {
                        mOnFilterChanged.onChanged(groupView.getData().getTitle(),
                                checkedFilterView.isChecked() ? checkedFilterView.getText().toString() : null);
                    }
                }
            });
        }
    }

    public void setFilterGroups(List<FilterGroupModel> filterGroupModels) {
        if (mChildren == null) {
            initChildren();
        }
        for (FilterGroupModel item : filterGroupModels) {
            FilterGroupView filterGroupView = mChildren.get(item.getTitle());
            if (filterGroupView == null) {
                continue;
            }
            filterGroupView.setData(item);
        }
    }

    public void setOnFilterChanged(OnFilterChanged listener) {
        mOnFilterChanged = listener;
    }

    public interface OnFilterChanged {
        void onChanged(String groupTitle, String checkText);
    }
}
