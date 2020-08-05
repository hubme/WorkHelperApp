package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;

/**
 * 可动态换行的布局容器。只支持 ChildView 高度固定。
 *
 * @author VanceKing
 * @since 2017/1/11.
 */
public class FlowLayout extends ViewGroup {
    private int mVisibleCount;
    private int mItemHeight;
    //子 View 一共占几行
    private int mTotalLineCount;

    private int mVerticalSpacing; //每个item纵向间距
    private int mHorizontalSpacing; //每个item横向间距

    private int mBreakChildIndex = -1;
    private boolean mIsBreak;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setHorizontalSpacing(int pixelSize) {
        mHorizontalSpacing = pixelSize;
    }

    public void setVerticalSpacing(int pixelSize) {
        mVerticalSpacing = pixelSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int selfWidth = resolveSize(0, widthMeasureSpec);

        boolean isLtr = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR;

        int paddingStart = getPaddingStart();
        int paddingTop = getPaddingTop();
        int paddingEnd = getPaddingEnd();
        int paddingBottom = getPaddingBottom();

        int offsetStart = isLtr ? paddingStart : selfWidth - paddingStart;
        int offsetTop = paddingTop;
        int availableMaxWidth = selfWidth - paddingEnd;

        mIsBreak = false;
        mBreakChildIndex = -1;
        mTotalLineCount = 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            LayoutParams params = (LayoutParams) childView.getLayoutParams();
            childView.measure(
                    getChildMeasureSpec(widthMeasureSpec, paddingStart + paddingEnd, params.width),
                    getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom,
                            params.height));
            int childWidth = childView.getMeasuredWidth();

            if (isLtr) {
                if (offsetStart + childWidth > availableMaxWidth) {//超过父 View 宽度，换行，行数 +1
                    offsetStart = paddingStart;
                    offsetTop += (mItemHeight + mVerticalSpacing);
                    mTotalLineCount++;
                }

                params.x = offsetStart;
                params.y = offsetTop;

                offsetStart += (childWidth + mHorizontalSpacing);
            } else {
                if (offsetStart - childWidth < 0) {//超过父 View 宽度，换行，行数 +1
                    offsetStart = selfWidth - paddingStart;
                    offsetTop += (mItemHeight + mVerticalSpacing);
                    mTotalLineCount++;
                }
                params.x = offsetStart - childWidth;
                params.y = offsetTop;
                offsetStart -= (childWidth + mHorizontalSpacing);

            }

            if (mVisibleCount > 0 && mTotalLineCount == mVisibleCount) {
                mBreakChildIndex = i - 1;
                mIsBreak = true;
                break;
            }
        }
        //处理占据一行但是不满一行的情况
        if (isLtr) {
            if (offsetStart > paddingStart && offsetStart <= availableMaxWidth) {
                mTotalLineCount += 1;
            }
        } else {
            if (offsetStart + mHorizontalSpacing >= 0 && offsetStart + mHorizontalSpacing < selfWidth - paddingStart) {
                mTotalLineCount += 1;
            }
        }
        Log.i("aaa", mTotalLineCount + "行");

        if (mVisibleCount > 0 && mVisibleCount < mTotalLineCount) {
            //不包含最后一行下面的 mVerticalSpacing
            int wantedHeight = paddingTop + paddingBottom + (mItemHeight + mVerticalSpacing) * mVisibleCount - mVerticalSpacing;
            setMeasuredDimension(selfWidth, resolveSize(wantedHeight, heightMeasureSpec));
        } else {
            int wantedHeight = paddingTop + paddingBottom + (mItemHeight + mVerticalSpacing) * mTotalLineCount - mVerticalSpacing;
            setMeasuredDimension(selfWidth, resolveSize(wantedHeight, heightMeasureSpec));
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof FlowLayout.LayoutParams;
    }

    @Override
    protected FlowLayout.LayoutParams generateDefaultLayoutParams() {
        return new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                FlowLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public FlowLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FlowLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected FlowLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new FlowLayout.LayoutParams(p.width, p.height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int index = mIsBreak ? mBreakChildIndex : getChildCount() - 1;
        for (int i = 0; i <= index; i++) {
            View child = getChildAt(i);
            FlowLayout.LayoutParams lp = (FlowLayout.LayoutParams) child.getLayoutParams();
            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(),
                    lp.y + child.getMeasuredHeight());
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        int x;
        int y;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    public void setVisibleCount(int count, int childHeight) {
        if (mVisibleCount != count) {
            mVisibleCount = count;
            mItemHeight = childHeight;
            requestLayout();
        }
    }

    /**
     * 所有的子 View 一共占据多少行。
     */
    public int getTotalCount() {
        return mTotalLineCount;
    }
}
