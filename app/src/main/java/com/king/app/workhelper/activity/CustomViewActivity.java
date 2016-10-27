package com.king.app.workhelper.activity;

import android.widget.CheckedTextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.ui.customview.LoadingView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 自定义View
 * Created by HuoGuangxu on 2016/10/17.
 */

public class CustomViewActivity extends AppBaseActivity {

    @BindView(R.id.btn_change_anim)
    CheckedTextView mChangeAnimTv;

    @BindView(R.id.loading_view)
    LoadingView mLoadingView;

    @Override public int getContentLayout() {

        return R.layout.activity_custom;
    }

    @Override protected void initData() {
        super.initData();

    }

    @OnClick(R.id.btn_change_anim)
    public void changeAnim() {
        mChangeAnimTv.toggle();
        if (mChangeAnimTv.isChecked()) {
            mChangeAnimTv.setText("暂停动画");
            mLoadingView.start();
        } else {
            mChangeAnimTv.setText("开始动画");
            mLoadingView.pause();
        }
    }
}
