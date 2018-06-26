package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author VanceKing
 * @since 2017/12/4.
 */

public class PaintView extends View {

    private Paint mPaint;
    private final String TEXT = "aaaaabbbbbcccccddddd";
    private int width;
    private int height;
    private int centerWidth;
    private int centerHeight;
    private Path mTextPath;
    private Paint mSimplePaint;

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
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);
        mPaint.setColor(Color.BLACK);

        mSimplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPath = new Path();

    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        centerWidth = width / 2;
        centerHeight = height / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        drawPath(canvas);
    }

    private void drawPath(Canvas canvas) {
        mTextPath.addArc(200, 200, 400, 400, -225, 225);
        mTextPath.arcTo(400, 200, 600, 400, -180, 225, false);
        mTextPath.lineTo(400, 542);
        canvas.drawPath(mTextPath, mSimplePaint);
    }

    private void drawText2(Canvas canvas) {
        mTextPath.reset();
        final int width = 600;
        final int height = 200;

        final int startX = 100;
        final int startY = 100;

        mTextPath.moveTo(startX, startY);
        mTextPath.lineTo(startX, startY + height);
        mTextPath.lineTo(startX + width, startY + height);
        mTextPath.lineTo(startX + width, startY-80);
        mTextPath.close();
        canvas.drawTextOnPath(TEXT, mTextPath, 0, 0, mPaint);

//        mTextPath.addRoundRect(50, 50, 250, 250, 20, 20, Path.Direction.CCW);
        mTextPath.addRoundRect(600, 50, 1200, 250, new float[]{0, 0, 50, 50, 0, 0, 100, 100}, Path.Direction.CCW);
        canvas.drawPath(mTextPath, mPaint);
    }

    private void drawText1(Canvas canvas) {
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
