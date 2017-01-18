package com.king.app.workhelper.fragment;

import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 常用View的简单使用
 *
 * @author VanceKing
 * @since 2017/1/19 0019.
 */

public class ViewSampleFragment extends AppBaseFragment {
    @BindView(R.id.switcher_text)
    TextSwitcher mSwitcher;

    private int mCounter = 0;
    private ViewSwitcher.ViewFactory mViewFactory;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_view_sample;
    }

    @Override
    protected void initData() {
        super.initData();
        mViewFactory = new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView t = new TextView(mContext);
                t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                t.setTextAppearance(mContext, android.R.style.TextAppearance_Large);
                return t;
            }
        };

        mSwitcher.setFactory(mViewFactory);
        Animation in = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);
    }

    @OnClick(R.id.btn_switch)
    public void onSwitcherClick() {
        mCounter++;
        mSwitcher.setText(String.valueOf(mCounter));
    }
}
