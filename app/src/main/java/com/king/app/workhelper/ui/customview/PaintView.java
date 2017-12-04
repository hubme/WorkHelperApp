package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author VanceKing
 * @since 2017/12/4.
 */

public class PaintView extends View {

    private Paint mPaint;
    private final String TEXT = "哈哈哈";

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(100);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        final int centerWidth = width / 2;
        final int centerHeight = height / 2;

        drawCenterCircle(canvas, centerWidth, centerHeight, 200);
        drawAnchorLine(canvas, width, height);

        mPaint.setColor(Color.parseColor("#D2691E"));
        float textWidth = mPaint.measureText(TEXT);

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //注意：fontMetrics.descent > 0; fontMetrics.ascent <0
        float textHeight = fontMetrics.descent - fontMetrics.ascent;//文字的高度
        //textHeight / 2 - fontMetrics.descent = BaseLine的偏移量
        float centerBaseLine = centerHeight + textHeight / 2 - fontMetrics.descent;
        canvas.drawText(TEXT, centerWidth - textWidth / 2, centerBaseLine, mPaint);

    }

    private void drawAnchorLine(Canvas canvas, int width, int height) {
        mPaint.setColor(Color.WHITE);
        canvas.drawLine(0, height / 2, width, height / 2, mPaint);
        canvas.drawLine(width / 2, 0, width / 2, height, mPaint);
    }

    private void drawCenterCircle(Canvas canvas, int cx, int cy, int radius) {
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(cx, cy, radius, mPaint);
    }
}
