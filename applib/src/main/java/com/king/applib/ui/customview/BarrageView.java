package com.king.applib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.annotation.ColorInt;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.king.applib.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 1.继承ViewGroup还是View。继承ViewGroup可以通过动画方便控制每个弹幕显示的速度、延迟显示等。但是要addView()和removeView(),同时还要创建Animation
 * 继承View通过绘制的方式则不太好控制弹幕显示和速度控制。优点是可以不用创建Animation对象。
 *
 * @author VanceKing
 * @since 2018/2/26.
 */

public class BarrageView extends FrameLayout {

    private static final String TAG = "aaa";

    private static final int MIN_INTERVAL = 2500;//mils
    private static final int MAX_INTERVAL = 3500;//mils
    private static final int MIN_DURATION = 3000;//mils
    private static final int MAX_DURATION = 4500;//mils

    //最多显示多少个弹幕
    private int mMaxBarrageCount = 3;
    //最多几条弹道
    private int mMaxRows = 1;

    //两个弹幕之间显示的间隔
    private int mMinInterval = MIN_INTERVAL;
    private int mMaxInterval = MAX_INTERVAL;
    //弹幕显示的总时长
    private int mMinDuration = MIN_DURATION;
    private int mMaxDuration = MAX_DURATION;
    //控制View动画的最终位置
    private int mMaxBarrageWidth;
    private int mStartOffset;
    private int mEndOffset;

    private Random mRandom;
    private Random mOffsetRandom;
    private final List<Barrage> mBarrages = new ArrayList<>();
    //缓存TextView，避免重复创建
//    Pools.Pool<TextView> mCahceBarrageViews = new Pools.SimplePool<>(mMaxBarrageCount);
    private List<Drawable> mBgDrawables;
    private int mScreenWidth;
    private int mHeight;
    private int mCurrentBarrageIndex;
    private DelayRunnable mDelayRunnable = new DelayRunnable();

    public BarrageView(Context context) {
        this(context, null);
    }

    public BarrageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.BarrageView);
            mMinInterval = typedArray.getInteger(R.styleable.BarrageView_bv_MinInterval, MIN_INTERVAL);
            mMaxInterval = typedArray.getInteger(R.styleable.BarrageView_bv_MaxInterval, MAX_INTERVAL);
            mMinDuration = typedArray.getInteger(R.styleable.BarrageView_bv_MinDuration, MIN_DURATION);
            mMaxDuration = typedArray.getInteger(R.styleable.BarrageView_bv_MaxDuration, MAX_DURATION);
            mMaxBarrageWidth = typedArray.getDimensionPixelSize(R.styleable.BarrageView_bv_MaxBarrageWidth, dp2px(200));
            mStartOffset = typedArray.getDimensionPixelSize(R.styleable.BarrageView_bv_StartOffset, dp2px(10));
            mEndOffset = typedArray.getDimensionPixelSize(R.styleable.BarrageView_bv_EndOffset, dp2px(100));
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        init();
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG, "停止");
        removeCallbacks(mDelayRunnable);
    }

    private void init() {
        mScreenWidth = getScreenWidth();
        mRandom = new Random();
        mOffsetRandom = new Random();
    }

    public void setBarrages(List<Barrage> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        mBarrages.addAll(list);
        showBarrage();
    }

    public void addBarrage(Barrage barrage) {
        mBarrages.add(barrage);
    }

    public void showBarrage() {
        /*post(new Runnable() {
            @Override public void run() {
                Log.i(TAG, "getMeasuredWidth(): " + getMeasuredWidth() + " ;getMeasuredHeight(): " + getMeasuredHeight());
                mScreenWidth = getMeasuredWidth();
                mHeight = getMeasuredHeight();
                postDelayed(mDelayRunnable, 500);
            }
        });*/
        postDelayed(mDelayRunnable, 500);
    }

    private void addBarrageView(Barrage barrage) {
        final TextView view = buildBarrageView(barrage);
        addView(view);
        TranslateAnimation animation = buildAnimation(mScreenWidth, -mMaxBarrageWidth);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {
            }

            @Override public void onAnimationEnd(Animation animation) {
                removeView(view);
            }

            @Override public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public void setBackgroundDrawables(List<Drawable> bgDrawables) {
        mBgDrawables = bgDrawables;
    }

    private Drawable getRandomBgDrawable() {
        if (mBgDrawables == null || mBgDrawables.isEmpty()) {
            return null;
        }
        return mBgDrawables.get(mRandom.nextInt(mBgDrawables.size()));
    }

    public TextView buildBarrageView(Barrage barrage) {
        TextView barrageView = new TextView(getContext());
        if (barrage == null || TextUtils.isEmpty(barrage.getContent())) {
            return barrageView;
        }
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int randomOffset = getRandomOffset();

        params.setMargins(0, randomOffset, 0, 0);
        barrageView.setLayoutParams(params);
        barrageView.setText(barrage.getContent());
        barrageView.setTextColor(barrage.getTextColor());
        barrageView.setTextSize(barrage.getTextSize());
        barrageView.setMaxWidth(mMaxBarrageWidth);
        barrageView.setSingleLine();
        barrageView.setMaxLines(1);
        barrageView.setEllipsize(TextUtils.TruncateAt.END);
        Drawable drawable = getRandomBgDrawable();
        if (drawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                barrageView.setBackground(drawable);
            } else {
                barrageView.setBackgroundDrawable(drawable);
            }
            barrageView.setPadding(dp2px(10), dp2px(6), dp2px(10), dp2px(6));

            if (drawable instanceof GradientDrawable) {
                ((GradientDrawable) drawable).setCornerRadius(dp2px(20));
            }
        }
        return barrageView;
    }

    private TranslateAnimation buildAnimation(int fromX, int toX) {
        TranslateAnimation animation = new TranslateAnimation(fromX, toX, 0, 0);
        int randomDuration = getRandomDuration();
        animation.setDuration(randomDuration);
        animation.setInterpolator(new LinearInterpolator());
        return animation;
    }

    private int getRandomDuration() {
        return mRandom.nextInt((mMaxDuration - mMinDuration) + 1) + mMinDuration;
    }

    private int getRandomDelay() {
        return mRandom.nextInt((mMaxInterval - mMinInterval) + 1) + mMinInterval;
    }

    private int getRandomOffset() {
        return mOffsetRandom.nextInt((mEndOffset - mStartOffset) + 1) + mStartOffset;
    }

    /** 指定弹幕显示的范围.in dp */
    public void setShowRange(int startOffset, int endOffset) {
        if (startOffset <= 0 || endOffset <= 0 || endOffset < startOffset) {
            throw new IllegalArgumentException("endOffset and end endOffset be greater than 0, endOffset must be greater than startOffset");
        }
        mStartOffset = dp2px(startOffset);
        mEndOffset = dp2px(endOffset);
    }

    private class DelayRunnable implements Runnable {
        @Override public void run() {
            addBarrageView(mBarrages.get(mCurrentBarrageIndex % mBarrages.size()));
            int randomDelay = getRandomDelay();
            postDelayed(mDelayRunnable, randomDelay);
            mCurrentBarrageIndex++;
            mCurrentBarrageIndex = mCurrentBarrageIndex % mBarrages.size();
        }
    }

    public static class Barrage {
        private String content;
        @ColorInt private int textColor = Color.BLACK;
        private int textSize = 14;//in sp

        public Barrage() {

        }

        public Barrage(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(@ColorInt int textColor) {
            this.textColor = textColor;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }
}
