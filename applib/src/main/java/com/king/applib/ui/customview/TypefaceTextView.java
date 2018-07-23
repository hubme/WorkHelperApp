package com.king.applib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        

        try {

            Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), typefaceText);
            if (typeface != null) {
                mTextPaint.setTypeface(typeface);
            }
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

        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
        float textWidth = mTextPaint.measureText(text);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        float centerX = (width - paddingLeft - paddingRight) / 2;
        float centerY = height / 4;//paddingTop + textHeight;
        Log.i("aaa", "centerX: "+centerX+" centerY： "+centerY);
        drawCenterText(canvas, centerX, centerY, text, mTextPaint);
    }

    public TypefaceTextView setText(String text) {
        this.text = text;
        return this;
    }

    public TypefaceTextView setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        return this;
    }
    
    
    public void build() {
        
        invalidate();
    }

    private void drawCenterText(Canvas canvas, float centerX, float centerY, String text, Paint paint) {
        final Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float baseLine = (fontMetrics.descent + fontMetrics.ascent) / 2;
        float textWidth = paint.measureText(text);
        float startX = centerX - textWidth / 2;
        float endY = centerY + baseLine;
        canvas.drawText(text, startX, endY, paint);
    }
    
    private SpannableStringBuilder buildTextStyle(String text, int start, int end, int color, int size) {
        if (TextUtils.isEmpty(text) || start < 0 || end > text.length()) {
            return null;
        }
        SpannableStringBuilder style = SpannableStringBuilder.valueOf(text);
        style.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new StyleSpan(Typeface.NORMAL), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return style;
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
