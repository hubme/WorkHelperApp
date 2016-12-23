package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

/**
 * 动态设置背景色、圆角等
 * Created by HuoGuangxu on 2016/12/22.
 */

public class SimpleDrawable extends GradientDrawable {
    public static final int LINEAR = 0;

    public static final int RADIAL = 1;

    public static final int SWEEP = 2;

    @IntDef({LINEAR, RADIAL, SWEEP})
    public @interface ShapeType {
    }

    public static class Builder {

        private final Context mContext;
        @ColorRes
        private int mBackgroundColor;
        private float mCornerRadius;
        @ShapeType
        private int mShapeType;

        public Builder(@NonNull Context context) {
            mContext = context;
        }

        public Builder setShape(@ShapeType int type) {
            mShapeType = type;
            return this;
        }

        public Builder setBackgroundColor(@ColorRes int colorRes) {
            mBackgroundColor = colorRes;
            return this;
        }

        public Builder setCornerRadius(float radius) {
            mCornerRadius = radius;
            return this;
        }

        public Drawable build() {
            SimpleDrawable drawable = new SimpleDrawable();
            apply(drawable);
            return drawable;
        }

        private void apply(SimpleDrawable drawable) {
            if (mBackgroundColor > 0) {
                drawable.setColor(mContext.getResources().getColor(mBackgroundColor));
            }
            if (mCornerRadius > 0) {
                drawable.setCornerRadius(mCornerRadius);
            }
            if (mShapeType == RADIAL) {
                drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
            }
        }
    }
}
