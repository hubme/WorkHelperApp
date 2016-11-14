package com.king.app.workhelper.activity;

import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.ui.customview.FormInputView;
import com.king.app.workhelper.ui.customview.LoadingDrawable;
import com.king.app.workhelper.ui.customview.DrawableEditText;
import com.king.applib.log.Logger;

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
    public FormInputView mFormInputView;

    @BindView(R.id.xedittext)
    public DrawableEditText mDrawableEditText;

    @Override public int getContentLayout() {

        return R.layout.activity_custom;
    }

    @Override protected void initData() {
        super.initData();

        mLoadingDrawable = new LoadingDrawable(this);
        mLoadingView.setImageDrawable(mLoadingDrawable);

        mFormInputView.setTitle("哈航啊");
        mFormInputView.setTitleTextColor(R.color.black);
        mFormInputView.setTitleTextSize(R.dimen.ts_normal);

//        mFormTextViewItem.setBackgroundResource(R.drawable.bg_selfhelp_selector);
//        mFormTextViewItem.setRightDrawable(R.mipmap.arrow_right);

//        mDrawableEditText.setDrawableExtraSpace(R.dimen.dp_50);
        mDrawableEditText.setDrawableRightListener(new DrawableEditText.DrawableRightListener() {
            @Override public void onDrawableRightClick(View view) {
                Logger.i("哈哈哈");
            }
        });
        mDrawableEditText.setDrawableLeftListener(new DrawableEditText.DrawableLeftListener() {
            @Override public void onDrawableLeftClick(View view) {
                Logger.i("呵呵呵");
            }
        });
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
