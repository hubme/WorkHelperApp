package com.king.app.workhelper.ui.customview.filter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.king.app.workhelper.R;
import com.king.applib.util.ExtendUtil;

import java.util.List;

public class FilterGroupView extends LinearLayout {
    private static final int VISIBLE_COUNT = 2;
    private static final int FILTER_TEXT_SIZE = 12;
    private static final int FILTER_ITEM_HEIGHT = 30;//dp

    private FilterGroupModel mFilter;
    private Context mContext;
    private CheckedTextView mCheckedView;
    private int mFilterItemHeight;
    private OnFilterChanged mOnFilterChanged;
    private CheckedTextView mTitleView;
    private FilterFlowLayout mConditionsContainer;

    public FilterGroupView(Context context) {
        this(context, null);
    }

    public FilterGroupView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        //为了实现设置最大行数，简单考虑，筛选项高度固定。
        mFilterItemHeight = ExtendUtil.dp2px(FILTER_ITEM_HEIGHT);
    }

    public void setData(@Nullable FilterGroupModel filter) {
        mFilter = filter;
        if (filter == null || filter.getConditions() == null || filter.getConditions().isEmpty()) {
            setVisibility(View.GONE);
            return;
        }

        setVisibility(View.VISIBLE);
        setTitleView();
        setFilterFlow();
    }

    public FilterGroupModel getData() {
        return mFilter;
    }

    private void setTitleView() {
        if (mTitleView == null) {
            mTitleView = new CheckedTextView(mContext);
            mTitleView.setEnabled(false);
            mTitleView.setLayoutParams(new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                    ExtendUtil.dp2px(41)));
            mTitleView.setGravity(Gravity.CENTER_VERTICAL);
            mTitleView.setTextSize(15);
            mTitleView.setTypeface(mTitleView.getTypeface(), Typeface.BOLD);
            mTitleView.setTextColor(Color.parseColor("#222222"));
            mTitleView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTitleView.toggle();
                    mTitleView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                            mTitleView.isChecked() ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down, 0);
                    mConditionsContainer.setVisibleCount(mTitleView.isChecked() ? -1 : VISIBLE_COUNT, mFilterItemHeight);
                    mFilter.setExpanded(mTitleView.isChecked());
                }
            });
            addView(mTitleView);
        }
        mTitleView.setText(mFilter.getTitle());
        mTitleView.setChecked(mFilter.isExpanded());
    }

    private void setFilterFlow() {
        if (mConditionsContainer == null) {
            mConditionsContainer = new FilterFlowLayout(mContext);
            mConditionsContainer.setVisibleCount(mFilter.isExpanded() ? -1 : VISIBLE_COUNT, mFilterItemHeight);
            int size = ExtendUtil.dp2px(10);
            mConditionsContainer.setHorizontalSpacing(size);
            mConditionsContainer.setVerticalSpacing(size);
            addView(mConditionsContainer);
        }
        if (mConditionsContainer.getChildCount() > 0) {
            mConditionsContainer.removeAllViews();
        }
        List<FilterGroupModel.FilterItem> conditions = mFilter.getConditions();
        if (conditions != null && !conditions.isEmpty()) {
            for (FilterGroupModel.FilterItem condition : conditions) {
                mConditionsContainer.addView(buildConditionView(mConditionsContainer, condition));
            }
        }
        mConditionsContainer.post(new Runnable() {
            @Override
            public void run() {
                boolean result = mConditionsContainer.getTotalCount() > VISIBLE_COUNT;
                mTitleView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                        result ? (mTitleView.isChecked() ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down) : 0, 0);
                mTitleView.setEnabled(result);
            }
        });
    }

    private CheckedTextView buildConditionView(FilterFlowLayout conditionsContainer, final FilterGroupModel.FilterItem filterItem) {
        CheckedTextView textView = conditionsContainer.getRecyclerChild();
        textView.setChecked(filterItem.isChecked());
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ExtendUtil.dp2px(FILTER_ITEM_HEIGHT)));
        textView.setTextSize(FILTER_TEXT_SIZE);
        textView.setTextColor(Color.parseColor("#222222"));
        textView.setText(filterItem.getText());
        textView.setBackground(buildBgDrawable(textView.isChecked()));
        int mItemPaddingLeftRight = ExtendUtil.dp2px(20);
        textView.setPadding(mItemPaddingLeftRight, 0, mItemPaddingLeftRight, 0);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CheckedTextView) v).toggle();
                updateCheckedView((CheckedTextView) v);
                if (mOnFilterChanged != null) {
                    mOnFilterChanged.onChanged(FilterGroupView.this, (CheckedTextView) v);
                }
            }
        });
        return textView;
    }

    private Drawable buildBgDrawable(boolean isChecked) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(ExtendUtil.dp2px(3));
        if (isChecked) {
            drawable.setColor(Color.parseColor("#f2f8ff"));
            drawable.setStroke(ExtendUtil.dp2px(1), Color.parseColor("#3A97FF"));
        } else {
            drawable.setColor(Color.parseColor("#f5f5f5"));
        }

        return drawable;
    }

    private void updateCheckedView(CheckedTextView textView) {
        textView.setBackground(buildBgDrawable(textView.isChecked()));
        if (mCheckedView != null && mCheckedView != textView) {
            mCheckedView.setChecked(false);
            mCheckedView.setBackground(buildBgDrawable(mCheckedView.isChecked()));
        }
        mCheckedView = textView;
    }

    public void setOnFilterChanged(OnFilterChanged listener) {
        mOnFilterChanged = listener;
    }

    public interface OnFilterChanged {
        void onChanged(FilterGroupView groupView, CheckedTextView checkedFilterView);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        if (child instanceof FilterFlowLayout) {
            ((FilterFlowLayout) child).removeAllViews();
        }
    }
}