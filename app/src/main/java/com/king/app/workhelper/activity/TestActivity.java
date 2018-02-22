package com.king.app.workhelper.activity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.util.ViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.tv_transition_drawable_sample1) TextView mDrawableTv1;
    @BindView(R.id.tv_transition_drawable_sample2) TextView mDrawableTv2;
    private TransitionDrawable transitionDrawable1;
    private TransitionDrawable transitionDrawable2;

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();

        Drawable[] drawables = {ContextCompat.getDrawable(this, R.drawable.icon_3),
                ContextCompat.getDrawable(this, R.drawable.icon_4)};
        transitionDrawable1 = new TransitionDrawable(drawables);
        transitionDrawable1.setCrossFadeEnabled(true);
        ViewUtil.setViewBackground(mDrawableTv1, transitionDrawable1);

        transitionDrawable2 = (TransitionDrawable) mDrawableTv2.getBackground();
        transitionDrawable2.setCrossFadeEnabled(false);

    }

    @OnClick(R.id.tv_open_qq)
    public void openQQ() {
        transitionDrawable1.startTransition(2000);
        transitionDrawable2.startTransition(2000);
    }

    @OnClick(R.id.tv_open_email)
    public void openEmail(TextView textView) {

        textView.animate().alpha(0.2f).setDuration(800).start();
    }
}
