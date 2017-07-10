package com.king.app.workhelper.fragment;

import android.view.View;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.ui.customview.MultiStatusView;

import butterknife.BindView;

/**
 * @author huoguangxu
 * @since 2017/7/10.
 */

public class MultiStatusViewFragment extends AppBaseFragment {
    @BindView(R.id.multi_status_view) MultiStatusView mMultiStatusView;

    @Override protected int getContentLayout() {
        return R.layout.fragment_multi_status_view;
    }

    @Override protected void initContentView(View rootView) {
        super.initContentView(rootView);
        mMultiStatusView.showView(R.layout.layout_statsu_view1);
        mMultiStatusView.showView(R.layout.layout_statsu_view2);
    }
}
