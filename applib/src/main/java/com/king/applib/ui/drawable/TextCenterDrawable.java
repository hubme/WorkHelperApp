package com.king.applib.ui.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

/**
 * 文字居中显示的 Drawable。
 *
 * @author VanceKing
 * @since 2021/8/9
 */
public class TextCenterDrawable extends GradientDrawable {

    private final TextPaint mTextPaint;
    private final String mText;
    private final float mTextWidth;

    public TextCenterDrawable(@NonNull String text, @ColorInt int textColor, int textSizePx) {
        mText = text;

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSizePx);
        mTextWidth = mTextPaint.measureText(text);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (mText != null) {
            canvas.drawText(mText, (getIntrinsicWidth() - mTextWidth) / 2f,
                    (getIntrinsicHeight() + Math.abs(mTextPaint.ascent()) - mTextPaint.descent()) / 2f,
                    mTextPaint);
        }
    }
}
