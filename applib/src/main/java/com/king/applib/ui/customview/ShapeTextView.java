package com.king.applib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;

import com.king.applib.R;

/**
 * 可以自由设置渐变背景和圆角的 TextView。
 *
 * @author VanceKing
 * @since 2018/6/7.
 */
public class ShapeTextView extends android.support.v7.widget.AppCompatTextView {
    private GradientDrawable mBgDrawable;

    private float mRadius;
    private int mBgColor;

    private int mGradientOrientation = -1;
    private int mGradientStartColor = Color.TRANSPARENT;
    private int mGradientMiddleColor = Color.TRANSPARENT;
    private int mGradientEndColor = Color.TRANSPARENT;

    private float mTLRadius;
    private float mTRRadius;
    private float mBLRadius;
    private float mBRRadius;

    public ShapeTextView(Context context) {
        super(context);
        init(context, null);
    }

    public ShapeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShapeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);

        if (mGradientOrientation >= 0) {
            mBgDrawable = new GradientDrawable(getGradientOrientation(), new int[]{mGradientStartColor, mGradientMiddleColor, mGradientEndColor});
        } else {
            mBgDrawable = new GradientDrawable();
            mBgDrawable.setColor(mBgColor);
        }
        if (mRadius > 0) {
            mBgDrawable.setCornerRadius(mRadius);
        } else {
            mBgDrawable.setCornerRadii(new float[]{mTLRadius, mTLRadius, mTRRadius, mTRRadius, mBRRadius, mBRRadius, mBLRadius, mBLRadius});
        }

        setBgDrawable();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeTextView);
            mRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeTextView_stv_radius, 0);
            mBgColor = typedArray.getColor(R.styleable.ShapeTextView_stv_bgColor, Color.TRANSPARENT);
            mGradientOrientation = typedArray.getInt(R.styleable.ShapeTextView_stv_gradientOrientation, -1);
            mGradientStartColor = typedArray.getColor(R.styleable.ShapeTextView_stv_gradientStartColor, Color.TRANSPARENT);
            mGradientMiddleColor = typedArray.getColor(R.styleable.ShapeTextView_stv_gradientMiddleColor, Color.TRANSPARENT);
            mGradientEndColor = typedArray.getColor(R.styleable.ShapeTextView_stv_gradientEndColor, Color.TRANSPARENT);

            mTLRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeTextView_stv_tlRadius, 0);
            mTRRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeTextView_stv_trRadius, 0);
            mBLRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeTextView_stv_blRadius, 0);
            mBRRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeTextView_stv_brRadius, 0);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
    }

    private GradientDrawable.Orientation getGradientOrientation() {
        switch (mGradientOrientation) {
            case 1:
                return GradientDrawable.Orientation.TR_BL;
            case 2:
                return GradientDrawable.Orientation.RIGHT_LEFT;
            case 3:
                return GradientDrawable.Orientation.BR_TL;
            case 4:
                return GradientDrawable.Orientation.BOTTOM_TOP;
            case 5:
                return GradientDrawable.Orientation.BL_TR;
            case 6:
                return GradientDrawable.Orientation.LEFT_RIGHT;
            case 7:
                return GradientDrawable.Orientation.TL_BR;
            case 0:
            case -1:
            default:
                return GradientDrawable.Orientation.TOP_BOTTOM;
        }
    }

    private void setBgDrawable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(mBgDrawable);
        } else {
            setBackgroundDrawable(mBgDrawable);
        }
    }
}
