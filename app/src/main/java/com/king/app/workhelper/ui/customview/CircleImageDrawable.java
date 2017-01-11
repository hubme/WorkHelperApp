package com.king.app.workhelper.ui.customview;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * 圆形图片
 *
 * @author huoguangxu
 * @since 2017/1/11.
 */

public class CircleImageDrawable extends Drawable {
    private Paint mPaint;
    private int mWidth;
    private Bitmap mBitmap;

    public CircleImageDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        init();
    }

    private void init() {
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
        mWidth = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2, mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mWidth;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
