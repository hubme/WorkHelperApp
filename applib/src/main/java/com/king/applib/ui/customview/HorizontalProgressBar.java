package com.king.applib.ui.customview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.king.applib.log.Logger;

/**
 * @author VanceKing
 * @since 2017/7/9.
 */

public class HorizontalProgressBar extends View {
    private long mDuration;
    private long mStartDelay;
    private Paint mPaint;
    private ValueAnimator mProgressAnimator;

    private OnProgressListener mOnProgressListener;
    private float mCurrentFraction;
    private float mCorner;
    private RectF mRectF;

    public HorizontalProgressBar(Context context) {
        this(context, null);
    }

    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#D2691E"));

        mRectF = new RectF();

        mProgressAnimator = ValueAnimator.ofFloat(0, 1);
        mProgressAnimator.setInterpolator(new LinearInterpolator());
        mProgressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentFraction = animation.getAnimatedFraction();
                Log.i("aaa", "Current Fraction : " + mCurrentFraction);
                invalidate();

                if (mOnProgressListener != null) {
                    mOnProgressListener.onCurrentFraction(mCurrentFraction);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mProgressAnimator != null && mProgressAnimator.isRunning()) {
            mProgressAnimator.cancel();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(Integer.MAX_VALUE, widthMeasureSpec),
                getDefaultSize((int) sp2px(getContext(), 20), heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();
        Logger.i("width: " + width + ";height: " + height + ";paddingLeft: " + paddingLeft + ";paddingRight: " + paddingRight);

        //        canvas.drawRect(paddingLeft, paddingTop, (width - paddingRight) * mCurrentFraction, height - paddingBottom, mPaint);

        mRectF.set(paddingLeft, paddingTop, (width - paddingRight) * mCurrentFraction, height - paddingBottom);
        canvas.drawRoundRect(mRectF, mCorner, mCorner, mPaint);
    }


    public void start() {
        if (mProgressAnimator.isRunning()) {
            mProgressAnimator.cancel();
        }
        mProgressAnimator.setDuration(mDuration);
        mProgressAnimator.setStartDelay(mStartDelay);
        mProgressAnimator.start();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void stop() {
        if (mProgressAnimator.isRunning()) {
            mProgressAnimator.pause();
        }
    }

    public void cancel() {
        if (mProgressAnimator.isRunning()) {
            mProgressAnimator.cancel();
        }
    }

    /**
     * 设置时长.
     *
     * @param duration 时长(ms)
     */
    public void setDuration(@IntRange(from = 0) long duration) {
        mDuration = duration;
    }

    /**
     * 设置延时时长.
     *
     * @param startDelay 延时时长(ms)
     */
    public void setStartDelay(@IntRange(from = 0) long startDelay) {
        mStartDelay = startDelay;
    }

    public void setCorner(@FloatRange(from = 0) float corner) {
        mCorner = sp2px(getContext(), corner);
    }

    public float sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }

    public void setOnProgressListener(OnProgressListener listener) {
        mOnProgressListener = listener;
    }

    public interface OnProgressListener {
        void onCurrentFraction(float fraction);
    }
}
