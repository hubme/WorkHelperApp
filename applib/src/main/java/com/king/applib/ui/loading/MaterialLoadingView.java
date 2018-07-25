package com.king.applib.ui.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.king.applib.R;

public class MaterialLoadingView extends View {
    private Animatable mDrawable;
    private int mColor;

    public MaterialLoadingView(Context context) {
        this(context, null);
    }

    public MaterialLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialLoadingView);
            mColor = typedArray.getColor(R.styleable.MaterialLoadingView_mlvColor, Color.GRAY);
        }finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
        init(context);
    }

    private void init(Context context) {
        MaterialProgressDrawable background = new MaterialProgressDrawable(context, this);
        background.setColorSchemeColors(mColor);
        background.setAlpha(255);
        setBackgroundDrawable(background);
    }

    @Override
    public void setVisibility(int visibility) {
        if (getVisibility() == visibility) {
            return;
        }
        if (mDrawable != null) {
            if (visibility == View.VISIBLE) {
                mDrawable.start();
            } else {
                mDrawable.stop();
            }
        }
        super.setVisibility(visibility);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mDrawable != null && getVisibility() == View.VISIBLE) {
            mDrawable.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDrawable != null) {
            mDrawable.stop();
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        if (mDrawable == background) {
            return;
        }
        if (background != null) {
            if (mDrawable != null) {
                mDrawable.stop();
            }

            if (background instanceof Animatable) {
                mDrawable = (Animatable) background;
            } else {
                mDrawable = null;
            }
        }
        super.setBackgroundDrawable(background);
    }
}
