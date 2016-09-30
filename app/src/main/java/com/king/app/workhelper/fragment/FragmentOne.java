package com.king.app.workhelper.fragment;


import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.common.BusProvider;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by VanceKing at 2016/9/29
 */

public class FragmentOne extends AppBaseFragment {
    @BindView(R.id.tv_content)
    TextView mContentTv;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_one;
    }

    @OnClick(R.id.tv_content)
    public void printText() {
        BusProvider.getEventBus().post("哈哈哈");
    }
}
