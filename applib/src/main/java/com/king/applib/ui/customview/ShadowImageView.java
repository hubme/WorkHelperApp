package com.king.applib.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import android.util.AttributeSet;

/**
 * @author VanceKing
 * @since 2017/9/29.
 */

public class ShadowImageView extends androidx.appcompat.widget.AppCompatImageView {
    private static final String TEXT = "好";
    private Paint mPaint;
    private final int SHADOW_COLOR = Color.parseColor("#D2691E");
    private Paint mPaint2;

    public ShadowImageView(Context context) {
        this(context, null);
    }

    public ShadowImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(18);

        mPaint2 = new Paint();
        mPaint2.setColor(SHADOW_COLOR);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

//        mPaint.setShadowLayer(10, 10, 10, SHADOW_COLOR);
//        canvas.drawText(TEXT, 0, TEXT.length(), width / 2, height / 2, mPaint);

        Drawable drawable = getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            int color = getBitmapProminentColor((BitmapDrawable) drawable);
            if (color == -1) {
                return;
            }
            mPaint.setColor(color);
            canvas.drawText(TEXT, 0, TEXT.length(), width / 2, height / 2, mPaint);

            mPaint2.setShadowLayer(5, 0, 7, SHADOW_COLOR);
            mPaint2.setColor(SHADOW_COLOR);
            canvas.drawBitmap(((BitmapDrawable) drawable).getBitmap(), 20, 20, mPaint2);
        }


    }

    @ColorInt
    private int getBitmapProminentColor(BitmapDrawable drawable) {
        Palette.Swatch mSwatch = Palette.from(drawable.getBitmap()).generate().getDominantSwatch();
        /*Palette.from(drawable.getBitmap()).generate(new Palette.PaletteAsyncListener() {
            @Override public void onGenerated(Palette palette) {
                
            }
        });*/
        if (mSwatch != null) {
            return getDarkerColor(mSwatch.getRgb());
        }
        return -1;
    }

    private int getDarkerColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[1] = hsv[1] + 0.1f;
        hsv[2] = hsv[2] - 0.1f;
        return Color.HSVToColor(hsv);
    }
}
