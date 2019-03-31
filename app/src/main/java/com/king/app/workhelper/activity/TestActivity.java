package com.king.app.workhelper.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private MyDialog dialogFragment;

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
                    imageView.setImageResource((Boolean) imageView.getTag() ? R.drawable.like_checked : R.drawable.like_unchecked);
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
        if (dialogFragment == null) {
            dialogFragment = new MyDialog();
        }
        if (!dialogFragment.isVisible()) {
            dialogFragment.show(getSupportFragmentManager(), "aaa");
        }
    }

    @DebugLog
    private void printLog() {
        SystemClock.sleep(3000);
        Log.i(TAG, "哈哈哈");

    }

    public static class MyDialog extends DialogFragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.layout_simple_dialog_fragment, container, false);
            return view;
        }
    }
}
