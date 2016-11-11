package com.king.app.workhelper.activity;

import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.ui.customview.FormTextViewItem;
import com.king.app.workhelper.ui.customview.LoadingDrawable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 自定义View
 * Created by HuoGuangxu on 2016/10/17.
 */

public class CustomViewActivity extends AppBaseActivity {

    @BindView(R.id.btn_change_anim)
    CheckedTextView mChangeAnimTv;

    @BindView(R.id.iv_loading)
    ImageView mLoadingView;
    private LoadingDrawable mLoadingDrawable;

    @BindView(R.id.form_item)
    public FormTextViewItem mFormTextViewItem;

    @Override public int getContentLayout() {

        return R.layout.activity_custom;
    }

    @Override protected void initData() {
        super.initData();

        mLoadingDrawable = new LoadingDrawable(this);
        mLoadingView.setImageDrawable(mLoadingDrawable);

        mFormTextViewItem.setTitle("哈航啊");
        mFormTextViewItem.setTitleTextColor(R.color.black);
        mFormTextViewItem.setTitleTextSize(R.dimen.ts_normal);

//        mFormTextViewItem.setBackgroundResource(R.drawable.bg_selfhelp_selector);
//        mFormTextViewItem.setRightDrawable(R.mipmap.arrow_right);
    }

    @OnClick(R.id.btn_change_anim)
    public void changeAnim() {
        if (mLoadingDrawable.isRunning()) {
            mLoadingDrawable.stop();
            mChangeAnimTv.setText("start");
        } else {
            mLoadingDrawable.start();
            mChangeAnimTv.setText("stop");
        }
    }
}
