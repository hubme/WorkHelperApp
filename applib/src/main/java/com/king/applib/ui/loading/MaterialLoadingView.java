package com.king.applib.ui.loading;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class MaterialLoadingView extends View {
    private Animatable mDrawable;

    public MaterialLoadingView(Context context) {
        this(context, null);
    }

    public MaterialLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundDrawable(new MaterialLoadingDrawable(context, this));
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
        if (mDrawable != null) {
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
