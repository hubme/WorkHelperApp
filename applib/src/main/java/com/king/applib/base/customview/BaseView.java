package com.king.applib.base.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.king.applib.R;
import com.king.applib.log.Logger;

/**
 * View 基类，抽取公共操作。
 *
 * @author VanceKing
 * @since 2018/2/3.
 */

public abstract class BaseView extends View {
    private int mWidth;
    private int mHeight;

    protected final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected final TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    public BaseView(Context context) {
        this(context, null);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        initPaint(mPaint);
        initTextPaint(mTextPaint);
    }

    protected void initPaint(Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
    }

    protected void initTextPaint(TextPaint textPaint) {
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.ts_normal));
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.chocolate));
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Logger.i("mWidth: " + w + "; mHeight: " + h);
        mWidth = w;
        mHeight = h;
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (onDrawCoordinate()) {
            drawCoordinate(canvas);
        }
    }

    protected boolean onDrawCoordinate() {
        return true;
    }

    protected void drawCoordinate(Canvas canvas) {
        canvas.drawLine(0, mHeight / 2, mWidth, mHeight / 2, mPaint);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight, mPaint);
    }

    protected int getViewWidth() {
        return mWidth;
    }

    protected int getViewHeight() {
        return mHeight;
    }

    protected int getViewHalfWidth() {
        return mWidth / 2;
    }

    protected int getViewHalfHeight() {
        return mHeight / 2;
    }

    protected float getTextWidth(String text) {
        return text == null ? 0 : mTextPaint.measureText(text, 0, text.length());
    }

    /**
     * 把文字画在某个点的正中央。
     *
     * @param canvas 画布
     * @param text   文本
     * @param x      点的 x 坐标
     * @param y      点的 y 坐标
     * @param paint  画笔
     */
    protected void drawCenterText(Canvas canvas, String text, int x, int y, Paint paint) {
        float textWidth = getTextWidth(text);
        final Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        float baseLine = y + textHeight / 2 - fontMetrics.descent;
        canvas.drawText(text, x - textWidth / 2, baseLine, paint);
    }
}
