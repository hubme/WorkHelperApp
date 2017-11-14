package com.king.applib.ui.loading;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * loading View
 */
public class LoadingProgress extends View {
    private Animatable mDrawable;

    public LoadingProgress(Context context) {
        this(context, null);
    }

    public LoadingProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public void setVisibility(int visibility) {
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
        if (getVisibility() == View.VISIBLE) {
            if (mDrawable != null) {
                mDrawable.start();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDrawable != null) {
            mDrawable.stop();
        }
    }

    private void init(Context context) {
        MaterialProgressDrawable background = new MaterialProgressDrawable(context, this);
        background.setColorSchemeColors(Color.BLACK);
        background.setAlpha(255);
        setBackgroundDrawable(background);
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
                mDrawable.start();
            } else {
                mDrawable = null;
            }
        }
        super.setBackgroundDrawable(background);
    }
}
