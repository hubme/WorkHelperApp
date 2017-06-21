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
import com.king.app.workhelper.common.ripplecompat.RippleCompat;
import com.king.app.workhelper.common.ripplecompat.RippleCompatDrawable;
import com.king.app.workhelper.common.ripplecompat.RippleConfig;

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

    @BindView(R.id.tv_ripple)
    TextView mRippleTv;

    private int mCounter = 0;
    private ViewSwitcher.ViewFactory mViewFactory;
    private RippleConfig mRippleConfig;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_view_sample;
    }

    @Override
    protected void initData() {
        super.initData();
        mRippleConfig = initRippleConfig();

        //View初始化的时候调用，在View的Click事件中调用，第一次点击无效，第二次点击有效，但最后报错。
        RippleCompat.apply(mRippleTv, mRippleConfig);

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

    @OnClick(R.id.tv_ripple)
    public void onRippleClick() {
        
    }

    private RippleConfig initRippleConfig() {
        RippleConfig config = new RippleConfig();
        config.setIsEnablePalette(true);
        config.setIsFull(false);
        config.setIsSpin(true);
        config.setType(RippleCompatDrawable.Type.CIRCLE);
        return config;
    }
}
