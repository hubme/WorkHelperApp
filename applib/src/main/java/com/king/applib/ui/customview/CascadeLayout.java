package com.king.applib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.king.applib.R;

/**
 * @author VanceKing
 * @since 2018/1/14
 */
public class CascadeLayout extends ViewGroup {

    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    public CascadeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CascadeLayout);
        try {
            mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.CascadeLayout_horizontal_spacing, dp2px(10));
            mVerticalSpacing = a.getDimensionPixelSize(R.styleable.CascadeLayout_vertical_spacing, dp2px(10));
        } finally {
            a.recycle();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();

        //Group的宽度
        int width = paddingLeft;
        //Group的高度
        int height = paddingTop;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {

            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            width = paddingLeft + mHorizontalSpacing * i;
            height = paddingTop + mVerticalSpacing * i + lp.verticalSpacing;

            lp.x = width;
            lp.y = height;
        }

        View lastChild = getChildAt(getChildCount() - 1);
        width += lastChild.getMeasuredWidth() + getPaddingRight();
        height += lastChild.getMeasuredHeight() + getPaddingBottom();

        setMeasuredDimension(resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y
                    + child.getMeasuredHeight());
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        int x;
        int y;
        public int verticalSpacing;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CascadeLayoutParams);
            try {
                verticalSpacing = a.getDimensionPixelSize(R.styleable.CascadeLayoutParams_layout_vertical_spacing, 0);
            } finally {
                a.recycle();
            }
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
