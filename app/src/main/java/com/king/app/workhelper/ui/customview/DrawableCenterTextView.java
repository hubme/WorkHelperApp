package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;

import com.king.app.workhelper.R;

/**
 * 文字和Left、Top、Right、Bottom Drawable都居中的TextView.
 *
 * @author VanceKing
 * @since 2017/9/20.
 */
public class DrawableCenterTextView extends android.support.v7.widget.AppCompatTextView {
    private Drawable drawableLeft = null;
    private Drawable drawableTop = null;
    private Drawable drawableRight = null;
    private Drawable drawableBottom = null;

    public DrawableCenterTextView(Context context) {
        this(context, null);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableCenterTextView);
            drawableLeft = typedArray.getDrawable(R.styleable.DrawableCenterTextView_imageLeft);
            drawableTop = typedArray.getDrawable(R.styleable.DrawableCenterTextView_imageTop);
            drawableRight = typedArray.getDrawable(R.styleable.DrawableCenterTextView_imageRight);
            drawableBottom = typedArray.getDrawable(R.styleable.DrawableCenterTextView_imageBottom);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        setIncludeFontPadding(false);
        setGravity(Gravity.START | Gravity.TOP);//避免canvas.translate()不正常
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int drawablePadding = getCompoundDrawablePadding();
        final int width = getWidth();
        final int height = getHeight();

        final TextPaint textPaint = getPaint();
        final float textWidth = textPaint.measureText(getText().toString());

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        int extraDy = 0;
        if (drawableTop != null && drawableBottom != null) {
            canvas.drawBitmap(((BitmapDrawable) drawableTop).getBitmap(), (width - drawableTop.getIntrinsicWidth()) / 2, height / 2 - textHeight / 2 - drawableTop.getIntrinsicHeight() - drawablePadding, null);
            canvas.drawBitmap(((BitmapDrawable) drawableBottom).getBitmap(), (width - drawableBottom.getIntrinsicWidth()) / 2, height / 2 + textHeight / 2 + drawablePadding, null);
        } else if (drawableTop != null && drawableTop instanceof BitmapDrawable) {//只有顶部Drawable
            canvas.drawBitmap(((BitmapDrawable) drawableTop).getBitmap(), (width - drawableTop.getIntrinsicWidth()) / 2, height / 2 - drawableTop.getIntrinsicHeight() - drawablePadding, null);
            extraDy = drawablePadding + (int) textHeight / 2;
        } else if (drawableBottom != null && drawableBottom instanceof BitmapDrawable) {//只有底部Drawable
            canvas.drawBitmap(((BitmapDrawable) drawableBottom).getBitmap(), (width - drawableBottom.getIntrinsicWidth()) / 2, height / 2 + textHeight / 2 + drawablePadding, null);
            extraDy = drawablePadding - (int) textHeight / 2;
        }

        int extraDx = 0;
        if (drawableLeft != null && drawableRight != null) {
            canvas.drawBitmap(((BitmapDrawable) drawableLeft).getBitmap(), width / 2 - textWidth / 2 - drawableLeft.getIntrinsicWidth() - drawablePadding, (height - drawableLeft.getIntrinsicHeight()) / 2, null);
            canvas.drawBitmap(((BitmapDrawable) drawableRight).getBitmap(), width / 2 + textWidth / 2 + drawablePadding, (height - drawableRight.getIntrinsicHeight()) / 2, null);
        } else if (drawableLeft != null) {
            canvas.drawBitmap(((BitmapDrawable) drawableLeft).getBitmap(), width / 2 - textWidth / 2 - drawableLeft.getIntrinsicWidth() - drawablePadding, (height - drawableLeft.getIntrinsicHeight()) / 2, null);
        } else if (drawableRight != null) {
            canvas.drawBitmap(((BitmapDrawable) drawableRight).getBitmap(), width / 2 + textWidth / 2 + drawablePadding, (height - drawableRight.getIntrinsicHeight()) / 2, null);
        }

        canvas.save();
        canvas.translate((width - textWidth) / 2 + extraDx, (height - textHeight) / 2 + extraDy);
        super.onDraw(canvas);
        canvas.restore();
    }
}
