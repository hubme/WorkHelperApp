package com.king.app.workhelper.dialog;

import android.view.View;

import com.king.app.workhelper.R;
import com.king.applib.base.dialog.BaseCenterDialog;

/**
 * @author huoguangxu
 * @since 2017/7/12.
 */

public class SimpleDialog extends BaseCenterDialog {
    @Override public int getLayoutRes() {
        return R.layout.common_empty_view;
    }

    @Override public void bindView(View v) {

    }

    @Override public boolean getCancelOutside() {
        return true;
    }

    @Override public float getDimAmount() {
        return 0.8f;
    }
}
