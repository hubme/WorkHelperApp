package com.king.applib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.Gravity;

import com.king.applib.R;

/**
 * 支持自定义字体。
 *
 * @author huoguangxu
 * @since 2018/7/21.
 */
public class TypefaceTextView extends android.support.v7.widget.AppCompatTextView {
    String typefaceText;
    String unit;
    private int unitTextSize;
    private int unitColor;

    public TypefaceTextView(Context context) {
        this(context, null);
    }

    public TypefaceTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypefaceTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setGravity(Gravity.CENTER);

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);
            typefaceText = typedArray.getString(R.styleable.TypefaceTextView_ttv_typeface);
            unit = typedArray.getString(R.styleable.TypefaceTextView_ttv_unit);
            unitTextSize = typedArray.getDimensionPixelSize(R.styleable.TypefaceTextView_ttv_unit_size, sp2px(14));
            unitColor = typedArray.getColor(R.styleable.TypefaceTextView_ttv_unit_color, Color.BLACK);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        try {

            Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), typefaceText);
            if (typeface != null) {
                setTypeface(typeface);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTypefaceText();

    }

    private void setTypefaceText() {
        String text = "";
        if (getText() != null) {
            text = text.concat(getText().toString());
        }
        if (TextUtils.isEmpty(unit)) {
            super.setText(text);
        } else {
            text = text.concat("\n").concat(unit);
            super.setText(buildTextStyle(text, text.length() - unit.length(), text.length(), unitColor, unitTextSize));
        }
    }
    
    
    @Override public void setText(CharSequence text, BufferType type) {
        setTypefaceText();
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
