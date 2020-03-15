package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author VanceKing
 * @since 2017/9/17.
 */

public class DrawableVerticalTextView extends androidx.appcompat.widget.AppCompatTextView {
    public DrawableVerticalTextView(Context context) {
        super(context);
    }

    public DrawableVerticalTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableVerticalTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void onDraw(Canvas canvas) {
        canvas = getTopCanvas(canvas);
        super.onDraw(canvas);
    }

    private Canvas getTopCanvas(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawable = drawables[1];// 上面的drawable  
        if (drawable == null) {
            drawable = drawables[3];// 下面的drawable  
        }

        float textSize = getPaint().getTextSize();
        int drawHeight = drawable.getIntrinsicHeight();
        int drawPadding = getCompoundDrawablePadding();
        float contentHeight = textSize + drawHeight + drawPadding;
        int topPadding = (int) (getHeight() - contentHeight);
        setPadding(0, topPadding, 0, 0);
        float dy = (contentHeight - getHeight()) / 2;
        canvas.translate(0, dy);
        Log.i("aaa", "setPadding(0," + topPadding + ",0,0");
        Log.i("aaa", "translate(0," + dy + ")");
        return canvas;
    }
}
