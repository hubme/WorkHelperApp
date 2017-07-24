package com.king.applib.ui.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 分割线<br/>
 * https://github.com/kymjs/RecyclerViewDemo/blob/master/RecyclerViewDemo/recycler/src/main/java/com/kymjs/recycler/Divider.java <br/>
 * also see {@link android.support.v7.widget.DividerItemDecoration v7.DividerItemDecoration}
 * 
 * @author huoguangxu
 * @since 2017/3/31.
 */
public class RecyclerDivider extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }

    @Orientation private int mOrientation;

    private static final int DEFAULT_COLOR = Color.parseColor("#e8e8e8");

    // TODO: 2017/3/31 支持Drawable 
    private ColorDrawable mDivider;

    private int leftMargin, rightMargin, topMargin, bottomMargin;

    private int width, height;
    
    public RecyclerDivider() {
        this(VERTICAL);
    }

    public RecyclerDivider(@Orientation int orientation) {
        this(orientation, DEFAULT_COLOR);
    }

    public RecyclerDivider(@Orientation int orientation, @ColorInt int color) {
        mDivider = new ColorDrawable(color);
        width = mDivider.getIntrinsicWidth();
        height = mDivider.getIntrinsicHeight();
        setOrientation(orientation);
        if (orientation == VERTICAL) {
            setHeight(1);
        } else {
            setWidth(1);
        }
    }

    public void setOrientation(@Orientation int orientation) {
        mOrientation = orientation;
    }

    public void setMargin(int left, int top, int right, int bottom) {
        this.leftMargin = left;
        this.topMargin = top;
        this.rightMargin = right;
        this.bottomMargin = bottom;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        if (mOrientation == HORIZONTAL) {
            drawHorizontal(canvas, parent);
        } else {
            drawVertical(canvas, parent);
        }
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop() + topMargin;
        final int bottom = parent.getHeight() - parent.getPaddingBottom() - bottomMargin;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin + leftMargin;
            final int right = left + width;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + leftMargin;
        final int right = parent.getWidth() - parent.getPaddingRight() - rightMargin;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin + topMargin;
            final int bottom = top + height;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == HORIZONTAL) {
            outRect.set(0, 0, leftMargin + width + rightMargin, 0);
        } else {
            outRect.set(0, 0, 0, topMargin + height + bottomMargin);
        }
    }
}