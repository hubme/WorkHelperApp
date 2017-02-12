package com.king.app.workhelper.ui.customview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

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
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
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
                resolveSizeAndState(900, heightMeasureSpec, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPies(canvas);
    }

    private void drawPies(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(0, 0, 500, 500);
        rectF.offset(20, 10);

        float startAngle = -90;
        for (Pie pie : mPies) {
            mPaint.setColor(ContextCompat.getColor(getContext(), pie.color));
            canvas.drawArc(rectF, startAngle, pie.percent * 360, true, mPaint);
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
}
