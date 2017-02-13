package com.king.app.workhelper.ui.customview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.king.app.workhelper.R;

import java.util.List;

/**
 * 饼图
 *
 * @author VanceKing
 * @since 2017/2/12.
 */

public class PieView extends View {

    private Paint mPaint;
    private List<Pie> mPies;
    private RectF mPieViewRectF;
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

        mPieViewRectF = new RectF(0, 0, 300, 300);
        mPieViewRectF.offset(100, 100);

        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
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
        setMeasuredDimension(resolveSizeAndState(500000, widthMeasureSpec, 0),
                resolveSizeAndState(500, heightMeasureSpec, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPies(canvas);
    }

    private void drawPies(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(50);

        float startAngle = -75;
        for (Pie pie : mPies) {
            mPaint.setColor(ContextCompat.getColor(getContext(), pie.color));
            canvas.drawArc(mPieViewRectF, startAngle, pie.percent * 360 + 1, false, mPaint);//不加1后留下缝隙
            startAngle += pie.percent * 360;
        }

    }

    public static class Pie {
        public float percent;
        @ColorRes
        public int color;

        public Pie(float percent, int color) {
            this.percent = percent;
            this.color = color;
        }
    }

    public void drawPies(List<Pie> pies) {
        if (pies != null && !pies.isEmpty()) {
            mPies = pies;
            postInvalidate();
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
