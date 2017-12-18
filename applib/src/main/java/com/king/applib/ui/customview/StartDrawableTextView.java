package com.king.applib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.king.applib.R;

/**
 * 类似列表项。图片始终和第一行的第一个字符对齐。
 *
 * @author huoguangxu
 * @since 2017/12/18.
 */

public class StartDrawableTextView extends android.support.v7.widget.AppCompatTextView {
    private Canvas bitmapCanvas;
    private Bitmap bitmap;
    private Drawable drawable;
    private int drawableWidth;
    private int drawableHeight;
    private int drawableMarginLeft;
    private int drawableMarginRight;
    private int drawableMarginTop;
    private float textHeight;

    public StartDrawableTextView(Context context) {
        this(context, null);
    }

    public StartDrawableTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StartDrawableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = null;

        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.StartDrawableTextView);
            drawable = typedArray.getDrawable(R.styleable.StartDrawableTextView_imageDrawable);
            drawableWidth = typedArray.getDimensionPixelSize(R.styleable.StartDrawableTextView_imageDrawableWidth, 0);
            drawableHeight = typedArray.getDimensionPixelSize(R.styleable.StartDrawableTextView_imageDrawableHeight, 0);
            drawableMarginLeft = typedArray.getDimensionPixelSize(R.styleable.StartDrawableTextView_imageMarginLeft, 0);
            drawableMarginRight = typedArray.getDimensionPixelSize(R.styleable.StartDrawableTextView_imageMarginRight, 0);
            drawableMarginTop = typedArray.getDimensionPixelSize(R.styleable.StartDrawableTextView_imageMarginTop, 0);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        init();
    }

    private void init() {
        setStartImage(drawable, drawableWidth, drawableHeight);

        TextPaint textPaint = getPaint();
        final String text = getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            textHeight = fontMetrics.descent - fontMetrics.ascent;
        }
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, getPaddingLeft() + drawableMarginLeft - drawableWidth - drawableMarginRight, getPaddingTop() + drawableMarginTop + (textHeight - drawableHeight) / 2, null);
        }
    }

    public void setStartImage(Drawable imageDrawable, int width, int height) {
        bitmap = drawable2Bitmap(imageDrawable, width, height);
        drawableWidth = width;
        drawableHeight = height;
        if (bitmap != null) {
            setPadding(getPaddingLeft() + drawableMarginLeft + width + drawableMarginRight, getPaddingTop(), 0, 0);
        }


    }

    private Bitmap drawable2Bitmap(Drawable drawable, int width, int height) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int resultWidth = width;
        int resultHeight = height;

        //ShapeDrawable
        if (resultWidth <= 0 && drawable.getIntrinsicWidth() > 0) {
            resultWidth = drawable.getIntrinsicWidth();
        }
        if (resultHeight <= 0 && drawable.getIntrinsicHeight() > 0) {
            resultHeight = drawable.getIntrinsicHeight();
        }

        Bitmap bitmap = Bitmap.createBitmap(resultWidth, resultHeight, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            return null;
        }
        if (bitmapCanvas == null) {
            bitmapCanvas = new Canvas();
        }
        bitmapCanvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, bitmapCanvas.getWidth(), bitmapCanvas.getHeight());
        drawable.draw(bitmapCanvas);
        return bitmap;
    }
}
