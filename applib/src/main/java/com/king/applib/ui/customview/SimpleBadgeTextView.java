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
    private static final int DEFAULT_RADIUS = 16;
    private Context mContext;
    private String mBadgeText;
    private boolean mIsShowBadge = true;
    private TextPaint mTextPaint;
    @ColorInt
    private int mBadgeBgColor = Color.RED;
    @ColorInt
    private int mBadgeTextColor = Color.WHITE;
    private float mBadgeTextSize = 26;

    private float mMarginLeft = 0;
    private float mMarginRight = 0;
    private RectF mRectF;

    private static final int DEFAULT_CORNER = 14;
    private int mCorner = DEFAULT_CORNER;

    private float mRadiusExtra;
    private float mLeftExtra;
    private float mTopExtra;
    private float mRightExtra;
    private float mBottomExtra;

    public SimpleBadgeTextView(Context context) {
        this(context, null);
    }

    public SimpleBadgeTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleBadgeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
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

        mTextPaint.setTextSize(getTextSize());
        float textWidth = mTextPaint.measureText(getText().toString());
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        float badgeX = centerX + textWidth / 2 + DEFAULT_RADIUS * (float) Math.sin(Math.toRadians(45)) + mMarginLeft;
        float badgeY = centerY - textHeight / 2 - DEFAULT_RADIUS * (float) Math.sin(Math.toRadians(45)) + mMarginRight;

        drawBadgeBg(canvas, badgeX, badgeY);

        drawBadgeText(canvas, badgeX, badgeY);
    }

    private void drawBadgeBg(Canvas canvas, float badgeX, float badgeY) {
        mTextPaint.setColor(mBadgeBgColor);
        mTextPaint.setTextSize(mBadgeTextSize);
        float width = mTextPaint.measureText(mBadgeText);
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float height = metrics.descent - metrics.ascent;
        float radius = Math.max(width, height) / 2;

        int defaultExtra = dp2px(3);
        if (mBadgeText.length() == 1) {
            canvas.drawCircle(badgeX, badgeY, radius + defaultExtra + mRadiusExtra, mTextPaint);
        } else {
            mRectF.set(badgeX - width / 2 - defaultExtra - mLeftExtra, badgeY - height / 2 - mTopExtra,
                    badgeX + width / 2 + defaultExtra + mRightExtra, badgeY + height / 2 + mBottomExtra);
            canvas.drawRoundRect(mRectF, mCorner, mCorner, mTextPaint);
        }
    }

    private void drawBadgeText(Canvas canvas, float centerX, float centerY) {
        mTextPaint.setColor(mBadgeTextColor);
        mTextPaint.setTextSize(mBadgeTextSize);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float baseLine = centerY - (fontMetrics.bottom + fontMetrics.top) / 2;
        float startX = centerX - mTextPaint.measureText(mBadgeText) / 2;
        canvas.drawText(mBadgeText, startX, baseLine, mTextPaint);
    }

    public void setMarginLeft(float marginLeft) {
        mMarginLeft = dp2px(marginLeft);
    }

    public void setMarginRight(float marginRight) {
        mMarginRight = dp2px(marginRight);
    }

    public void setCorner(int corner) {
        mCorner = corner;
    }

    //正圆.badge text length == 1有效
    public void setRadiusExtra(float radiusExtra) {
        mRadiusExtra = dp2px(radiusExtra);
    }

    //椭圆.badge text length > 1有效
    public void setBadgePadding(float left, float top, float right, float bottom) {
        mLeftExtra = dp2px(left);
        mTopExtra = dp2px(top);
        mRightExtra = dp2px(right);
        mBottomExtra = dp2px(bottom);
    }

    public void setBadgeTextSize(float textSize) {
        if (textSize > 0) {
            mBadgeTextSize = textSize;
        }
    }

    public void show(String text) {
        if (text == null || text.length() <= 0) {
            return;
        }
        mBadgeText = text;
        invalidate();
    }

    public int dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
