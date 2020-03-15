package com.king.applib.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import com.king.applib.R;

/**
 * 文字颜色渐变。
 *
 * @author VanceKing
 * @since 2018/2/26.
 */

public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView {
    private int mStartColor = Color.BLACK;
    private int mEndColor = Color.BLACK;
    //默认不渐变
    private int mGradientType = -1;

    public GradientTextView(Context context) {
        this(context, null);
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView);
            mStartColor = typedArray.getColor(R.styleable.GradientTextView_gtv_startColor, Color.BLACK);
            mEndColor = typedArray.getColor(R.styleable.GradientTextView_gtv_endColor, Color.BLACK);
            mGradientType = typedArray.getInt(R.styleable.GradientTextView_gtv_gradientType, -1);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
    }

    @SuppressLint("DrawAllocation") @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!changed || TextUtils.isEmpty(getText().toString().trim()) || mGradientType == -1) {
            return;
        }

        if (mGradientType == 0) {
            float textWidth = getPaint().measureText(getText().toString());
            int gravity = getGravity() & Gravity.HORIZONTAL_GRAVITY_MASK;
            if (gravity == Gravity.LEFT) {
                getPaint().setShader(new LinearGradient(0, 0, textWidth, 0, Color.RED, Color.GREEN, Shader.TileMode.CLAMP));
            } else if (gravity == Gravity.CENTER_HORIZONTAL) {
                getPaint().setShader(new LinearGradient((getMeasuredWidth() - textWidth) / 2, 0,
                        (getMeasuredWidth() + textWidth) / 2, 0, Color.RED, Color.GREEN, Shader.TileMode.CLAMP));
            } else if (gravity == Gravity.RIGHT) {
                getPaint().setShader(new LinearGradient(getMeasuredWidth() - textWidth, 0,
                        getMeasuredWidth(), 0, Color.RED, Color.GREEN, Shader.TileMode.CLAMP));
            }

        } else if (mGradientType == 1) {
            Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
            getPaint().setShader(new LinearGradient(0, -fontMetrics.ascent, 0, fontMetrics.descent, mEndColor,
                    mStartColor, Shader.TileMode.CLAMP));
        }
    }
}
