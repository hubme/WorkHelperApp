package com.king.app.workhelper.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.SystemClock;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.ui.BrowViewController;

import butterknife.BindView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.image_view)
    ImageView imageView;
    int[] resIds = {R.drawable.brow_001, R.drawable.brow_002,
            R.drawable.brow_003, R.drawable.brow_004, R.drawable.brow_007};
    private BrowViewController controller;
    private AnimatorSet animatorSet;

    @Override
    protected void initInitialData() {
        super.initInitialData();
    }

    @Override
    protected void initData() {
        animatorSet = new AnimatorSet();

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 0.1f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 1.0f, 0.2f, 1.0f);
        scaleYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i(TAG, "onAnimationUpdate :" + animation.getAnimatedValue() + animation.getAnimatedFraction());
                if ((float) animation.getAnimatedValue() < 0.3f) {
                    imageView.setImageResource((Boolean)imageView.getTag() ? R.drawable.like_checked : R.drawable.like_unchecked);
                }
            }
        });
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
//        animatorSet.setStartDelay(50);
        animatorSet.setDuration(600);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_test;
    }


    @OnClick(R.id.image_view)
    public void onViewClick() {
        Object tag = imageView.getTag();
        if (tag == null || !(Boolean) tag) {
            imageView.setTag(true);
            controller = new BrowViewController(imageView, (FrameLayout) getWindow().getDecorView(), resIds)
                    .setDuration(600)
                    .build();
            controller.start();
        } else {
            imageView.setTag(false);
        }
        if (!animatorSet.isRunning()) {
            animatorSet.start();
        }
    }

    @DebugLog
    private void printLog() {
        SystemClock.sleep(3000);
        Log.i(TAG, "哈哈哈");

    }
}
