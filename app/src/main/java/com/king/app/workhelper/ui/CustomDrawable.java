package com.king.app.workhelper.ui;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 自定义 Drawable。
 *
 * @author VanceKing
 * @since 2020/7/28
 */
public class CustomDrawable extends Drawable {
    private final Paint mPaint;
    private final int mWidth;
    private final int mHeight;
    private final Path mPath;
    private final int mPadding;
    private int mUpColor;
    private int mDownColor;

    public CustomDrawable(int widthPx, int heightPx, @ColorInt int upColor, @ColorInt int downColor,
                          int dividerHeight) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mUpColor = upColor;
        mDownColor = downColor;

        mWidth = widthPx;
        mHeight = heightPx;
        mPadding = Math.max(0, dividerHeight);

        mPath = new Path();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        mPaint.setColor(mUpColor);
        mPath.reset();
        mPath.moveTo(mWidth / 2f, 0);
        mPath.lineTo(0, (mHeight - mPadding) / 2f);
        mPath.lineTo(mWidth, (mHeight - mPadding) / 2f);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        mPaint.setColor(mDownColor);
        mPath.reset();
        mPath.moveTo(0, (mHeight + mPadding) / 2f);
        mPath.lineTo(mWidth / 2f, mHeight);
        mPath.lineTo(mWidth, (mHeight + mPadding) / 2f);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }
}
