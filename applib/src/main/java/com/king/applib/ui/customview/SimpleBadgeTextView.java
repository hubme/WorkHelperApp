package com.king.applib.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * @author huoguangxu
 * @since 2017/4/24.
 */

public class SimpleBadgeTextView extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "aaa";
    private static final String Text = "1";
    private String mBadgeText = Text;
    private boolean mIsShowBadge = true;
    private TextPaint mTextPaint;
    @ColorInt private int mBadgeBgColor = Color.RED;
    @ColorInt private int mBadgeTextColor = Color.WHITE;
    private int mBadgeTextSize = 24;
    private int mRadius = 16;

    private float mMarginLeft = 0;
    private float mMarginRight = 0;
    private RectF mRectF;

    private static final int DEFAULT_CORNER = 14;
    private int mCorner = DEFAULT_CORNER;
    
    public SimpleBadgeTextView(Context context) {
        this(context, null);
    }

    public SimpleBadgeTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleBadgeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);

        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBadgeText == null || mBadgeText.length() <= 0) {
            return;
        }
        
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(28);
        float fontWidth = mTextPaint.measureText(mBadgeText);
        canvas.drawLine(0, centerY, getWidth(), centerY, mTextPaint);
        canvas.drawLine(centerX, 0, centerX, getHeight(), mTextPaint);

        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float baseLine = centerY - (metrics.bottom + metrics.top) / 2;
        canvas.drawText(mBadgeText, centerX - fontWidth / 2, baseLine, mTextPaint);

        mTextPaint.setTextSize(getTextSize());
        float textWidth = mTextPaint.measureText(getText().toString());
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        float badgeX = centerX + textWidth / 2 + mRadius * (float) Math.sin(Math.toRadians(45)) + mMarginLeft;
        float badgeY = centerY - textHeight / 2 - mRadius * (float) Math.sin(Math.toRadians(45)) + mMarginRight;

        drawBadgeBg(badgeX, badgeY, canvas);
        
//        canvas.drawCircle(badgeX, badgeY, mRadius, mTextPaint);
        
        drawBadgeText(canvas, badgeX, badgeY, mBadgeText);
    }

    private void drawBadgeBg(float badgeX, float badgeY, Canvas canvas) {
        mTextPaint.setColor(mBadgeBgColor);
        mTextPaint.setTextSize(mBadgeTextSize);
        float width = mTextPaint.measureText(mBadgeText);
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float height = metrics.bottom - metrics.top;
        float radius = Math.max(width, height) / 2;
        
        if (mBadgeText.length() == 1) {
            canvas.drawCircle(badgeX, badgeY, radius, mTextPaint);//增加圆的半径
        } else {
            mRectF.set(badgeX - width / 2 - 6, badgeY - height / 2, badgeX + width / 2 + 6, badgeY + height / 2);
            canvas.drawRoundRect(mRectF, mCorner, mCorner, mTextPaint);
        }
    }

    private void drawBadgeText(Canvas canvas, float centerX, float centerY, String text) {
        mTextPaint.setColor(mBadgeTextColor);
        mTextPaint.setTextSize(mBadgeTextSize);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float baseLine = centerY - (fontMetrics.bottom + fontMetrics.top) / 2;
        float startX = centerX - mTextPaint.measureText(text) / 2;
        canvas.drawText(text, startX, baseLine, mTextPaint);
    }

    private void drawBadgeText() {
        mTextPaint.setColor(mBadgeTextColor);
        mTextPaint.setTextSize(mBadgeTextSize);
        
    }

    public void setMarginLeft(float marginLeft) {
        mMarginLeft = marginLeft;
    }

    public void setMarginRight(float marginRight) {
        mMarginRight = marginRight;
    }

    public void setCorner(int corner) {
        mCorner = corner;
    }
}
