package com.king.app.workhelper.ui.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author huoguangxu
 * @since 2017/6/28.
 */

public class MyItemDivider extends RecyclerView.ItemDecoration {
    public static final String TAG = "MyItemDivider";
    public static final int HORIZONTAL = 0;//水平方向
    public static final int VERTICAL = 1;//垂直方向

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DividerOrientation {
    }

    private int orientation;//方向
    private final int itemGap;//边距大小 px

    private final Rect mRect;
    private final Paint mPaint;

    private final int DecorationColor = Color.parseColor("#D2691E");

    public MyItemDivider(int itemGap){
        this(VERTICAL, itemGap);

    }

    public MyItemDivider(@DividerOrientation int orientation, int itemGap) {
        this.orientation = orientation;
        this.itemGap = itemGap;

        mRect = new Rect();
        mRect.set(0, 0, 100, 100);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /*
    在 onDraw 为 divider 设置绘制范围，并绘制到 canvas 上，而这个绘制范围可以超出在 getItemOffsets 中设置的范围，
    但由于 decoration 是绘制在 child view 的底下，所以并不可见，但是会存在 overdraw。
     */
    @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawVertical(c, parent);
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        Log.i(TAG, "childCount: " + childCount);
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + 20;//这个值可以超过getItemOffsets设置的值，但是
            mRect.set(left, top, right, bottom);

            mPaint.setColor(DecorationColor);
            c.drawRect(mRect, mPaint);
        }
    }

    /*
    decoration 的 onDraw，child view 的 onDraw，decoration 的 onDrawOver，这三者是依次发生的。
    而由于 onDrawOver 是绘制在最上层的，所以它的绘制位置并不受限制（当然，decoration 的 onDraw 绘制范围也不受限制，只不过不可见）
     */
    @Override public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        mPaint.setColor(Color.BLUE);
        c.drawRect(0, 0, parent.getRight(), 40, mPaint);


        View view = parent.getChildAt(3);
        c.drawRect(0, view.getBottom(), parent.getRight(), view.getBottom() + 40, mPaint);
        
    }

    //为 outRect 设置的4个方向的值，将被计算进所有 decoration 的尺寸中，而这个尺寸，被计入了 RecyclerView 每个 item view 的 padding 中
    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int lastPosition = state.getItemCount() - 1;//要通过RecyclerView.State#getItemCount获取全部item数量,是固定不变的
        int current = parent.getChildAdapterPosition(view);
        Log.i(TAG, "lastPosition: " + lastPosition + ";current: " + current);

        //holder出现异常时，可能为-1
        if (current == -1) {
            return;
        }
        
        //每个item的上下左右多加了20px的padding
        outRect.set(20, 20, 20, 20);


        /*if (layoutManager instanceof LinearLayoutManager && !(layoutManager instanceof GridLayoutManager)) {
            if (orientation == LinearLayoutManager.VERTICAL) {//竖直排列
                if (current == lastPosition) {
                    outRect.set(0, 0, 0, 0);
                } else {
                    outRect.set(5, 5, 10, itemGap);
                } 
            } else {//水平排列
                if (current == lastPosition) {//判断是否为最后一个item
                    outRect.set(0, 0, 0, 0);
                } else {
                    outRect.set(5, 5, itemGap,  10);
                }
            }
        }*/
    }
}
