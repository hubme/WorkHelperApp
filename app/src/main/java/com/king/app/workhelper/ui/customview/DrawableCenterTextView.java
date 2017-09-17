/**
 * Copyright (C) 2006-2015 Tuniu All rights reserved
 */
package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

public class DrawableCenterTextView extends android.support.v7.widget.AppCompatTextView {

    public DrawableCenterTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawableLeft = drawables[0];
        Drawable drawableTop = drawables[1];
        Drawable drawableRight = drawables[2];
        Drawable drawableBottom = drawables[3];

        int drawablePadding = getCompoundDrawablePadding();

        int drawableWidth = (drawableLeft == null ? 0 : drawableLeft.getIntrinsicWidth()) + (drawableRight == null ? 0 : drawableRight.getIntrinsicWidth());
        int drawableHeight = (drawableTop == null ? 0 : drawableTop.getIntrinsicWidth()) + (drawableBottom == null ? 0 : drawableBottom.getIntrinsicWidth());

        final TextPaint textPaint = getPaint();
        float textWidth = textPaint.measureText(getText().toString());

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        float contentHeight = textHeight + drawableHeight + drawablePadding;
        
        float dy = (contentHeight - getHeight())/2;
        Log.i("aaa", "contentHeight: " + contentHeight + ";getHeight(): " + getHeight() + ";dy: " + dy);
//        canvas.translate(0, -dy);
        if (drawableTop != null) {
            canvas.drawBitmap(((BitmapDrawable)drawableTop).getBitmap(), 20, 20, null);
        }
        super.onDraw(canvas);
        
    }

    private Canvas getTopCanvas(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawable = drawables[1];// 上面的drawable  
        if(drawable == null){
            drawable = drawables[3];// 下面的drawable  
        }

        float textSize = getPaint().getTextSize();
        int drawHeight = drawable.getIntrinsicHeight();
        int drawPadding = getCompoundDrawablePadding();
        float contentHeight = textSize + drawHeight + drawPadding;
        int topPadding = (int) (getHeight() - contentHeight);
        setPadding(0, topPadding , 0, 0);
        float dy = (contentHeight - getHeight())/2;
        canvas.translate(0, dy);
        Log.i("DrawableTopButton", "setPadding(0,"+topPadding+",0,0");
        Log.i("DrawableTopButton", "translate(0,"+dy+")");
        return canvas;
    }
}
