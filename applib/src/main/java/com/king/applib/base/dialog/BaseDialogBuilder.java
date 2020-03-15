package com.king.applib.base.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.FloatRange;
import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;
import android.view.Gravity;

/**
 * @author VanceKing
 * @since 2017/7/24.
 */

public class BaseDialogBuilder {
    public static final String KEY_DIALOG_WIDTH= "KEY_DIALOG_WIDTH";
    public static final String KEY_DIALOG_HEIGHT= "KEY_DIALOG_HEIGHT";
    public static final String KEY_DIALOG_GRAVITY= "KEY_DIALOG_GRAVITY";
    public static final String KEY_CANCELABLE_OUTSIDE = "KEY_CANCELABLE_OUTSIDE";
    public static final String KEY_CANCELABLE= "KEY_CANCELABLE";
    public static final String KEY_DIM_AMOUNT= "KEY_DIM_AMOUNT";
    public static final String KEY_ANIMATION_RES= "KEY_ANIMATION_RES";
    
    private int mWidth = -1;
    private int mHeight = -1;
    private int mGravity = Gravity.CENTER;
    private boolean mCancelableOutside = true;
    private boolean mCancelable = true;
    private float mDimAmount = 0.4f;
    @StyleRes private int mAnimRes = -1;

    protected final Class<? extends BaseDialogFragment> mClass;
    protected final Context mContext;


    public BaseDialogBuilder(Context context, Class<? extends BaseDialogFragment> clazz) {
        mContext = context;
        mClass = clazz;
    }

    public BaseDialogBuilder setDialogWidth(int width) {
        if (width > 0) {
            mWidth = width;
        }
        return this;
    }

    public BaseDialogBuilder setDialogHeight(int height) {
        if (mHeight > 0) {
            mHeight = height;
        }
        return this;
    }

    public BaseDialogBuilder setGravity(int gravity) {
        mGravity = gravity;
        return this;
    }

    public BaseDialogBuilder setCancelableOutside(boolean cancelableOutside) {
        mCancelableOutside = cancelableOutside;
        return this;
    }

    public BaseDialogBuilder setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return this;
    }

    public BaseDialogBuilder setDimAmount(@FloatRange(from = 0, to = 1) float dimAmount) {
        mDimAmount = dimAmount;
        return this;
    }

    public BaseDialogBuilder setAnimationRes(@StyleRes int animRes) {
        mAnimRes = animRes;
        return this;
    }

    private Bundle buildArguments() {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_DIALOG_WIDTH, mWidth);
        bundle.putInt(KEY_DIALOG_HEIGHT, mHeight);
        bundle.putInt(KEY_DIALOG_GRAVITY, mGravity);
        bundle.putBoolean(KEY_CANCELABLE_OUTSIDE, mCancelableOutside);
        bundle.putBoolean(KEY_CANCELABLE, mCancelable);
        bundle.putFloat(KEY_DIM_AMOUNT, mDimAmount);
        bundle.putInt(KEY_ANIMATION_RES, mAnimRes);
        return bundle;
    }

    public BaseDialogFragment build() {
        final Bundle bundle = buildArguments();
        BaseDialogFragment fragment = (BaseDialogFragment) Fragment.instantiate(mContext, mClass.getName(), bundle);
        return fragment;
    }
}
