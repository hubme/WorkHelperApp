package com.king.applib.base.dialog;

import android.view.Gravity;

import com.king.applib.R;

/**
 * @author VanceKing
 * @since 2017/7/12.
 */

public abstract class BaseBottomDialog extends BaseDialogFragment {
    @Override protected int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override protected int getAnimationRes() {
        return R.style.SimpleDialog_AnimationStyle;
    }
}
