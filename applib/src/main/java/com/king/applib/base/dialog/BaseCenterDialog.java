package com.king.applib.base.dialog;

import android.view.Gravity;

/**
 * @author VanceKing
 * @since 2017/7/12.
 */

public abstract class BaseCenterDialog extends BaseDialogFragment {
    @Override public int getWidth() {
        return (int) (getDisplayMetrics().widthPixels * 0.8);
    }

    @Override protected int getGravity() {
        return Gravity.CENTER;
    }
}
