package com.king.app.workhelper.fragment;

import android.widget.CheckedTextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.ui.customview.HorizontalProgressBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/7/9 0009.
 */

public class CustomViewFragment1 extends AppBaseFragment {
    @BindView(R.id.progress_bar)
    HorizontalProgressBar mProgressBar;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_custom_view1;
    }

    @Override
    protected void initData() {
        super.initData();
        mProgressBar.setDuration(10 * 1000);
        mProgressBar.setCorner(5);
        mProgressBar.setStartDelay(3000);
    }

    @OnClick(R.id.ctv_toggle)
    public void onStartProgress(CheckedTextView textView) {
        mProgressBar.start();
    }
}
