package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

/**
 * 动态设置背景色、圆角等
 * Created by HuoGuangxu on 2016/12/22.
 */

public class SimpleDrawable extends GradientDrawable {

    private SimpleDrawable() {

    }

    public static class Builder {

        private final Context mContext;
        @ColorRes private int mBackgroundColor;
        @ColorInt private int mBackgroundColorInt;
        private float mCornerRadius;
        private int mShapeType = GradientDrawable.RECTANGLE;
        private int mGradientType = GradientDrawable.LINEAR_GRADIENT;

        public Builder(@NonNull Context context) {
            mContext = context;
        }

        /**
         * 设置背景形状
         *
         * @param type GradientDrawable.Shape
         */
        public Builder setShape(int type) {
            mShapeType = type;
            return this;
        }

        /**
         * 设置渐变类型
         *
         * @param gradientType GradientDrawable.GradientType
         */
        public Builder setGradientType(int gradientType) {
            mGradientType = gradientType;
            return this;
        }

        public Builder setBackgroundColorInt(@ColorInt int colorInt) {
            mBackgroundColorInt = colorInt;
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

        public GradientDrawable build() {
            SimpleDrawable drawable = new SimpleDrawable();
            apply(drawable);
            return drawable;
        }

        private void apply(SimpleDrawable drawable) {
            drawable.setColor(mBackgroundColorInt);
            if (mBackgroundColor > 0) {
                drawable.setColor(ContextCompat.getColor(mContext, mBackgroundColor));
            }
            if (mCornerRadius > 0) {
                drawable.setCornerRadius(mCornerRadius);
            }

            drawable.setGradientType(mGradientType);
            drawable.setShape(mShapeType);
        }
    }
}
