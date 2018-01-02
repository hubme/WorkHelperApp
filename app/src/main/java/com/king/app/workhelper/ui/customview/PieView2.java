package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author VanceKing
 * @since 2018/1/2.
 */

public class PieView2 extends View {
    private static final int START_ANGLE = -90;
    private int mArcWidth = dip2px(35);
    private int mCenterCircleWidth = dip2px(40);

    public static final int ASC = 0;
    public static final int DESC = 1;

    @IntDef({ASC, DESC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SORT_TYPE {

    }

    private final Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mPieRectF = new RectF();

    private List<PieItem> mPies = new ArrayList<>();


    public PieView2(Context context) {
        this(context, null);
    }

    public PieView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mCirclePaint.setColor(Color.parseColor("#D2691E"));
        mCirclePaint.setStrokeWidth(2);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mTextPaint.setColor(Color.parseColor("#2c2c2c"));
    }
    
    
    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(dip2px(300), MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        final int centerX = width / 2;
        final int centerY = height / 2;

        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();

        final int actualWidth = width - paddingLeft - paddingRight;
        final int actualHeight = height - paddingTop - paddingBottom;
        
        canvas.drawLine(0, height/2, width, height/2, mTextPaint);
        canvas.drawLine(width/2, 0, width/2, height, mTextPaint);

        final int size = Math.min(actualWidth, actualHeight);

        int length = mArcWidth + mCenterCircleWidth;
        mPieRectF.set(centerX - length, centerY - length, centerX + length, centerY + length);
        canvas.drawArc(mPieRectF, START_ANGLE, 45, true, mCirclePaint);
    }

    public void drawPies(List<PieItem> pies, @SORT_TYPE int sortType) {
        if (pies == null || pies.isEmpty()) {
            return;
        }
        if (!mPies.isEmpty()) {
            mPies.clear();
        }
        mPies.addAll(pies);

        /*if (mCenterText != null && !mCenterText.trim().isEmpty()) {
            float width = mTextPaint.measureText(mCenterText);
            mTextLayout = new StaticLayout(mCenterText, mTextPaint, (int) width, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
        }*/

        sortPies(sortType);
        invalidate();
    }

    private void sortPies(@SORT_TYPE final int type) {
        Collections.sort(mPies, new Comparator<PieItem>() {
            @Override public int compare(PieItem o1, PieItem o2) {
                if (o1.value < o2.value) {
                    return type == ASC ? -1 : 1;
                } else if (o1.value > o2.value) {
                    return type == ASC ? 1 : -1;
                } else {
                    return 0;
                }
            }
        });
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static class PieItem {
        public double value;
        @ColorInt
        public int color;
        public int marginLeft = 50;//拐点左边距
        public int marginBottom = 50;//拐点底部边距
        private String primaryText;
        private String secondText;

        public PieItem(String primaryText, String secondText, double value, int color) {
            this.primaryText = primaryText;
            this.secondText = secondText;
            this.value = value;
            this.color = color;
        }

    }
}
