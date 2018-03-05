package com.king.applib.builder;

import android.annotation.TargetApi;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.king.applib.util.ContextUtil;

/**
 * GradientDrawable 构造器。
 *
 * @author VanceKing
 * @since 2018/3/5.
 */

public class GradientDrawableBuilder {

    private final GradientDrawable mGradientDrawable;

    private GradientDrawableBuilder(GradientDrawable.Orientation orientation, int... colors) {
        mGradientDrawable = new GradientDrawable(orientation, colors);
    }

    private GradientDrawableBuilder() {
        mGradientDrawable = new GradientDrawable();
        setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
    }

    public static GradientDrawableBuilder create() {
        return new GradientDrawableBuilder();
    }

    public static GradientDrawableBuilder create(GradientDrawable.Orientation orientation, int... colors) {
        return new GradientDrawableBuilder(orientation, colors);
    }

    public GradientDrawableBuilder setCornerRadius(float radius) {
        mGradientDrawable.setCornerRadius(radius);
        return this;
    }

    public GradientDrawableBuilder setCornerRadii(float[] radii) {
        mGradientDrawable.setCornerRadii(radii);
        return this;
    }

    public GradientDrawableBuilder setSize(int width, int height) {
        mGradientDrawable.setSize(width, height);
        return this;
    }

    public GradientDrawableBuilder setGradientType(int gradientType) {
        mGradientDrawable.setGradientType(gradientType);
        return this;
    }

    public GradientDrawableBuilder setColor(@ColorInt int argb) {
        mGradientDrawable.setColor(argb);
        return this;
    }

    public GradientDrawableBuilder setColorResource(@ColorRes int color) {
        mGradientDrawable.setColor(ContextCompat.getColor(ContextUtil.getAppContext(), color));
        return this;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GradientDrawableBuilder setColors(@ColorInt int... colors) {
        mGradientDrawable.setColors(colors);
        return this;
    }

    public GradientDrawableBuilder setColorResources(@ColorRes int... colors) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (colors != null && colors.length > 0) {
                int[] colorInt = new int[colors.length];
                for (int i = 0; i < colors.length; i++) {
                    colorInt[i] = ContextCompat.getColor(ContextUtil.getAppContext(), colors[i]);
                }
                mGradientDrawable.setColors(colorInt);
            }
        }

        return this;
    }

    public GradientDrawableBuilder setOrientation(GradientDrawable.Orientation orientation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mGradientDrawable.setOrientation(orientation);
        }
        return this;
    }

    public GradientDrawableBuilder setShape(int shape) {
        mGradientDrawable.setShape(shape);
        return this;
    }

    public GradientDrawable build() {
        return mGradientDrawable;
    }
}
