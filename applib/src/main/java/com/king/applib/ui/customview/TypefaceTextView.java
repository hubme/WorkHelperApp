package com.king.applib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.king.applib.R;

/**
 * 支持自定义字体。
 *
 * @author VanceKing
 * @since 2018/7/21.
 */
public class TypefaceTextView extends View {
    private static final String TAG = "aaa";
    String typefaceText;

    String text;
    private int textSize;
    private int textColor;
    String unit;
    private int unitTextSize;
    private int unitColor;
    private int textPadding;

    private TextPaint mTextPaint;
    private int width;
    private int height;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private Typeface typeface;
    private float totalTextHeight;
    private float textHeight;
    private float unitTextHeight;


    public TypefaceTextView(Context context) {
        this(context, null);
    }

    public TypefaceTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypefaceTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);
            typefaceText = typedArray.getString(R.styleable.TypefaceTextView_ttv_typeface);
            text = typedArray.getString(R.styleable.TypefaceTextView_ttv_text);
            textSize = typedArray.getDimensionPixelSize(R.styleable.TypefaceTextView_ttv_text_size, sp2px(16));
            textColor = typedArray.getColor(R.styleable.TypefaceTextView_ttv_text_color, Color.BLACK);
            unit = typedArray.getString(R.styleable.TypefaceTextView_ttv_unit);
            unitTextSize = typedArray.getDimensionPixelSize(R.styleable.TypefaceTextView_ttv_unit_size, sp2px(14));
            unitColor = typedArray.getColor(R.styleable.TypefaceTextView_ttv_unit_color, Color.GRAY);
            textPadding = typedArray.getDimensionPixelOffset(R.styleable.TypefaceTextView_ttv_text_padding, 0);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
        init();
    }

    private void init() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);


        try {
            typeface = Typeface.createFromAsset(getResources().getAssets(), typefaceText);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                Log.i(TAG, "AT_MOST。widthSize = " + widthSize);
                break;
            case MeasureSpec.EXACTLY:
                Log.i(TAG, "EXACTLY。widthSize = " + widthSize);

                break;
            case MeasureSpec.UNSPECIFIED:
                Log.i(TAG, "UNSPECIFIED。widthSize = " + widthSize);

                break;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0, height / 2, width, height / 2, mTextPaint);
        canvas.drawLine(width / 2, 0, width/2, height, mTextPaint);

        mTextPaint.setTextSize(textSize);
        textHeight = getTextHeight(mTextPaint);
        mTextPaint.setTextSize(unitTextSize);
        unitTextHeight = getTextHeight(mTextPaint);

        totalTextHeight = textHeight + unitTextHeight + textPadding;

        drawText(canvas);
        drawUnitText(canvas);
    }

    private void drawText(Canvas canvas) {
        if (typeface != null) {
            mTextPaint.setTypeface(typeface);
        }
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
        float textWidth = mTextPaint.measureText(text);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        canvas.drawText(text, (width - paddingLeft - paddingRight) / 2 - textWidth / 2,
                -fontMetrics.ascent + (height - totalTextHeight) / 2, mTextPaint);
    }

    private void drawUnitText(Canvas canvas) {
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setTextSize(unitTextSize);
        mTextPaint.setColor(unitColor);
        canvas.drawText(unit, (width - paddingLeft - paddingRight) / 2 - mTextPaint.measureText(unit) / 2,
                textHeight + textPadding - mTextPaint.getFontMetrics().ascent + (height - totalTextHeight) / 2, mTextPaint);

    }

    public TypefaceTextView setText(String text) {
        this.text = text;
        return this;
    }

    public TypefaceTextView setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        return this;
    }


    public void update() {
        invalidate();
    }

    private float getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent;
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
