package com.king.app.workhelper.ui.customview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.king.app.workhelper.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 饼图
 *
 * @author VanceKing
 * @since 2017/2/12.
 */

public class PieView extends View {
    private static final int START_ANGLE = -90;
    private static final int FULL_ANGLE = 360;
    private static final int DEFAULT_STROKE_WIDTH = 50;
    private static final int DEFAULT_RADIUS = 200;
    public static final int ASC = 0;
    public static final int DESC = 1;

    private boolean isPrint;

    @IntDef({ASC, DESC})
    public @interface SORT_TYPE {

    }

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    ;
    private List<PieItem> mPies = new ArrayList<>();

    private RectF mPieRectF;
    private int mRingWidth;
    private int mInnerCircleRadius;

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        //圆环宽度
        mRingWidth = dip2px(getContext(), 10);
        //内圆半径
        mInnerCircleRadius = dip2px(getContext(), 80);

        mPieRectF = new RectF();

        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.chocolate));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // FIXME: 2017/2/12
        setMeasuredDimension(resolveSizeAndState(Integer.MAX_VALUE, widthMeasureSpec, 0),
                resolveSizeAndState(500, heightMeasureSpec, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float totalValue = 0;
        for (PieItem pieItem : mPies) {
            if (pieItem != null) {
                totalValue += pieItem.value;
            }
        }
        if (totalValue == 0) {
            return;
        }

        final int width = getWidth();
        final int height = getHeight();
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        final int availableWidth = width - paddingLeft - paddingRight;
        final int availableHeight = height - paddingTop - paddingBottom;

        final int maxWidth = Math.min(availableWidth, availableHeight);
        final int radius = Math.min(DEFAULT_RADIUS, maxWidth / 2);

        final int centerX = paddingLeft + availableWidth / 2;
        final int centerY = paddingTop + availableHeight / 2;

        // TODO: 2017/2/22 没有处理paddingBottom和paddingRight 
        mPieRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);

        float startAngle = START_ANGLE;
        for (PieItem pieItem : mPies) {
            if (pieItem == null) {
                continue;
            }
            mPaint.setColor(ContextCompat.getColor(getContext(), pieItem.color));
            float pieAngle = (float) (pieItem.value / totalValue) * FULL_ANGLE;
            canvas.drawArc(mPieRectF, startAngle, pieAngle + 1, false, mPaint);//不加1后留下缝隙
            startAngle += pieAngle;
        }

        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.WHITE);
        float beforeValue = 0;
        for (PieItem item : mPies) {
            float percent = (float) (beforeValue + item.value / 2) / totalValue;
            canvas.drawLine(centerX, centerY, getPointX(centerX, radius + DEFAULT_STROKE_WIDTH / 2, percent),
                    getPointY(centerY, radius + DEFAULT_STROKE_WIDTH / 2, percent), mPaint);
            beforeValue += item.value;
        }

    }

    private float getPointX(int centerX, int radius, double percent) {
        if (percent >= 0 && percent <= 0.5) {//第一和第二象限
            return centerX + radius * (float) Math.sin(percent * 2 * Math.PI);
        } else if (percent > 0.5 && percent <= 1) {////第三和第四象限
            return centerX - radius * (float) Math.sin(percent * 2 * Math.PI);
        } else {
            return 0;
        }
    }

    private float getPointY(int centerY, int radius, double percent) {
        if ((percent >= 0 && percent <= 0.25) || (percent >= 0.75 && percent <= 1)) {//第一和第四象限
            return centerY - radius * (float) Math.cos(percent * 2 * Math.PI);
        } else if (percent > 0.25 && percent < 0.75) {//第二和第三象限
            return centerY + radius * (float) Math.cos(percent * 2 * Math.PI);
        } else {
            return 0;
        }
    }

    public static class PieItem {
        public double value;
        @ColorRes
        public int color;

        public PieItem(double value, int color) {
            this.value = value;
            this.color = color;
        }
    }

    public void drawPies(List<PieItem> pies, @SORT_TYPE int sortType) {
        if (pies != null && !pies.isEmpty()) {
            mPies.clear();
            mPies.addAll(pies);
            sortPies(sortType);
            postInvalidate();
        }
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

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
