package com.king.app.workhelper.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.os.Build;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author VanceKing
 * @since 19-3-17.
 */
public class BrowViewController {
    private View anchorView;
    private FrameLayout decorView;
    private int[] imageResIds;
    private AnimatorSet animatorSet;
    private long duration = 1000;
    private long delay;
    private final Random random = new Random();
    private int[] anchorPoint;
    private ImageView[] imageViews;

    public BrowViewController(View anchorView, FrameLayout decorView, int[] imageResIds) {
        this.anchorView = anchorView;
        this.decorView = decorView;
        this.imageResIds = imageResIds;
    }

    public BrowViewController setDuration(long duration) {
        if (duration > 0) {
            this.duration = duration;
        }
        return this;
    }

    public BrowViewController setStartDelay(long delay) {
        if (delay > 0) {
            this.delay = delay;
        }
        return this;
    }

    public void start() {
        if (animatorSet.isRunning()) {
            return;
        }
        animatorSet.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BrowViewController build() {
        if (imageResIds == null || imageResIds.length <= 0) {
            throw new IllegalArgumentException("imageResIds must not be null or empty.");
        }
        anchorPoint = getAnchorViewPoint();
        imageViews = buildImageViews(anchorPoint);
        animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(delay);
        animatorSet.setDuration(duration);
        PathInterpolator pathInterpolator = new PathInterpolator(0.33f, 0f, 0.12f, 1f);
        animatorSet.setInterpolator(pathInterpolator);

        final List<Animator> animators = new ArrayList<>(imageResIds.length + 1);

        ValueAnimator alphaAnim = ValueAnimator.ofInt(0, 255, 0);
        alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer alphaValue = (Integer) animation.getAnimatedValue();
                for (ImageView view : imageViews) {
                    view.setAlpha(alphaValue);
                }
            }
        });
        animators.add(alphaAnim);

        for (int i = 0; i < imageResIds.length; i++) {
            Path path = buildAnimatorPath();
            ObjectAnimator viewPathAnimator = ObjectAnimator.ofFloat(imageViews[i], "x", "y", path);
            animators.add(viewPathAnimator);
        }

        animatorSet.playTogether(animators);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (imageViews != null) {
                    for (View view : imageViews) {
                        decorView.removeView(view);
                    }
                }
            }
        });
        return this;
    }

    private ImageView[] buildImageViews(int[] point) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(point[0], point[1], 0, 0);
        final ImageView[] imageViews = new ImageView[imageResIds.length];
        for (int i = 0; i < imageResIds.length; i++) {
            ImageView imageView = new ImageView(anchorView.getContext());
            imageView.setImageResource(imageResIds[i]);
            imageView.setAlpha(0);
            imageViews[i] = imageView;
            decorView.addView(imageView, params);
        }
        return imageViews;
    }

    private Path buildAnimatorPath() {
        final Path path = new Path();
        resetPathPoint(path);
        return path;
    }

    private void resetPathPoint(Path path) {
        path.reset();
        path.moveTo(anchorPoint[0], anchorPoint[1]);
        int screenWidth = anchorView.getContext().getResources().getDisplayMetrics().widthPixels;
        int x = random(random, 100, screenWidth - 100);
        int y = random(random, 100, anchorPoint[1] - 100);
//        path.lineTo(x, y);
        if (x < anchorPoint[0]) {
            path.quadTo(x + 50, y + 50, x, y);
        } else {
            path.quadTo(x - 50, y + 50, x, y);
        }
    }

    private int random(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private int[] getAnchorViewPoint() {
        int[] anchorPoint = new int[2];
        anchorView.getLocationInWindow(anchorPoint);
        return anchorPoint;
    }


}
