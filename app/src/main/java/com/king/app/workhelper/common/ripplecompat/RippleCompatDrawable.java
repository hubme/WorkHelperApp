package com.king.app.workhelper.common.ripplecompat;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Jiayi Yao on 2015/10/28.
 */
public class RippleCompatDrawable extends Drawable implements View.OnTouchListener {
    private enum Speed {PRESSED, NORMAL}

    public enum Type {CIRCLE, HEART, TRIANGLE}

    public interface OnFinishListener {
        void onFinish();
    }

    /* ClipBound for widget inset padding.*/
    private Rect mClipBound;
    /* Drawable bound for background image. */
    private Rect mDrawableBound;

    private ArrayList<OnFinishListener> mOnFinishListeners;
    private Paint mRipplePaint;
    private Speed mSpeed;
    private Path mRipplePath;
    private Interpolator mInterpolator;
    private ValueAnimator mFadeAnimator;
    private Drawable mBackgroundDrawable;
    private RippleUtil.PaletteMode mPaletteMode;
    private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;
    private int mWidth = 0;
    private int mHeight = 0;
    private int mRippleColor;
    private int mBackgroundColor;
    private int mBackgroundColorAlpha = 0;
    private int mRippleDuration;
    private int mMaxRippleRadius;
    private int mFadeDuration;
    private int mAlpha;
    private int mAlphaDelta = 0;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private int mPaddingTop = 0;
    private int mPaddingBottom = 0;
    private float mDegree;

    private long mStartTime;
    private int x;
    private int y;
    private float mScale = 0f;
    private int lastX;
    private int lastY;
    private float lastScale = 0f;

    private boolean isFull = false;
    private boolean isSpin = false;
    private boolean isWaving = false;
    private boolean isPressed = false;
    private boolean isFading = false;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRippleRunnable = new Runnable() {
        @Override
        public void run() {
            updateRipple(mSpeed);
            if (isWaving || isPressed) {
                mHandler.postDelayed(this, RippleUtil.FRAME_INTERVAL);
            } else if (!isFading) {
                startFadeAnimation();
            }
        }
    };

    public RippleCompatDrawable(RippleConfig config) {
        this(config.getRippleColor(), config.getMaxRippleRadius(),
                config.getRippleDuration(), config.getInterpolator(), config.getFadeDuration(),
                config.isFull(), config.getPath(), config.isSpin(), config.getPaletteMode());
    }

    private RippleCompatDrawable(int rippleColor, int maxRippleRadius,
                                 int rippleDuration, Interpolator interpolator, int fadeDuration,
                                 boolean isFull, Path path, boolean isSpin, RippleUtil.PaletteMode paletteMode) {
        setRippleColor(rippleColor);
        mMaxRippleRadius = maxRippleRadius;
        mRippleDuration = rippleDuration;
        mInterpolator = interpolator;
        mFadeDuration = fadeDuration;
        mPaletteMode = paletteMode;

        this.isFull = isFull;
        this.isSpin = isSpin;

        mRipplePath = path;

        mRipplePaint = new Paint();
        mRipplePaint.setAntiAlias(true);
        mAlpha = 0;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        if (mBackgroundDrawable == null) {
            canvas.clipRect(mClipBound);
        } else if (mBackgroundDrawable instanceof ColorDrawable) {
            canvas.clipRect(mClipBound);
            mBackgroundDrawable.setBounds(mClipBound);
            mBackgroundDrawable.draw(canvas);
        } else {
            if (mDrawableBound == null) {
                mDrawableBound = RippleUtil.getBound(mScaleType, new Rect(0, 0, mWidth, mHeight),
                        mBackgroundDrawable.getIntrinsicWidth(), mBackgroundDrawable.getIntrinsicHeight());
            }
            canvas.clipRect(mDrawableBound);
            mBackgroundDrawable.setBounds(mDrawableBound);
            mBackgroundDrawable.draw(canvas);
        }

        canvas.drawColor(RippleUtil.alphaColor(mBackgroundColor, mBackgroundColorAlpha));
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(mScale, mScale);
        if (isSpin) canvas.rotate(mDegree);
        mRipplePaint.setAlpha(mAlpha);
        canvas.drawPath(mRipplePath, mRipplePaint);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        mRipplePaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    private long elapsedOffset = 0;
    private static final String TAG = "RippleCompatDrawable";

    private void updateRipple(Speed speed) {
        float progress = 0f;
        if (isWaving) {
            long elapsed = SystemClock.uptimeMillis() - mStartTime;
            if (speed == Speed.PRESSED) {
                elapsed = elapsed / 5;
                elapsedOffset = elapsed * 4;
            } else {
                elapsed = elapsed - elapsedOffset;
            }
            progress = Math.min(1f, (float) elapsed / mRippleDuration);
            isWaving = progress <= 0.99f;
            mScale = (mMaxRippleRadius - RippleUtil.MIN_RIPPLE_RADIUS) / RippleUtil.MIN_RIPPLE_RADIUS * mInterpolator.getInterpolation(progress) + 1f;
            mBackgroundColorAlpha = (int) (Color.alpha(mBackgroundColor) * (progress <= 0.125f ? progress * 8 : 1f));
        } else {
            mScale = (mMaxRippleRadius - RippleUtil.MIN_RIPPLE_RADIUS) / RippleUtil.MIN_RIPPLE_RADIUS + 1f;
            mBackgroundColorAlpha = Color.alpha(mBackgroundColor);
        }
        if (lastX == x && lastY == y && lastScale == mScale) return;

        lastX = x;
        lastY = y;
        lastScale = mScale;

        mRipplePaint.setColor(mRippleColor);
        mRipplePaint.setStyle(Paint.Style.FILL);

        mDegree = progress * 480f;
        invalidateSelf();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();

                mSpeed = Speed.PRESSED;

                stopFading();
                mHandler.removeCallbacks(mRippleRunnable);
                mStartTime = SystemClock.uptimeMillis();
                mHandler.postDelayed(mRippleRunnable, RippleUtil.FRAME_INTERVAL);
                isWaving = true;
                isPressed = true;
                elapsedOffset = 0;
                lastX = x;
                lastY = y;
                mDegree = 0;
                mAlpha = Color.alpha(mRippleColor);

                break;
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX();
                y = (int) event.getY();
                mSpeed = Speed.PRESSED;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mSpeed = Speed.NORMAL;
                isPressed = false;
                startFadeAnimation();
                break;
        }
        return true;
    }

    public void triggerListener() {
        if (mOnFinishListeners != null && mOnFinishListeners.size() != 0) {
            for (OnFinishListener listener : mOnFinishListeners) {
                listener.onFinish();
            }
        }
    }

    public void finishRipple() {
        mHandler.removeCallbacks(mRippleRunnable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && mFadeAnimator != null) {
            mFadeAnimator.removeAllUpdateListeners();
            mFadeAnimator.removeAllListeners();
            mFadeAnimator = null;
        } else {
            mHandler.removeCallbacks(mFadeRunnable4Froyo);
        }
    }

    private void startFadeAnimation() {
        isFading = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            startFadeAnimation4HoneyComb();
        } else {
            startFadeAnimation4Froyo();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startFadeAnimation4HoneyComb() {
        if (mFadeAnimator == null) {
            mFadeAnimator = ValueAnimator.ofInt(Color.alpha(mRippleColor), 0);
            mFadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mAlpha = (int) animation.getAnimatedValue();
                    if (mAlpha <= mBackgroundColorAlpha) mBackgroundColorAlpha = mAlpha;
                    invalidateSelf();
                }
            });

            mFadeAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isFading = false;
                    triggerListener();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isFading = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mFadeAnimator.setDuration(mFadeDuration);
        } else {
            mFadeAnimator.cancel();
        }
        mFadeAnimator.start();
    }

    private void startFadeAnimation4Froyo() {
        mAlphaDelta = getAlphaDelta();
        mHandler.removeCallbacks(mFadeRunnable4Froyo);
        mHandler.post(mFadeRunnable4Froyo);
    }

    private void stopFading() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            if (mFadeAnimator != null) mFadeAnimator.cancel();
        } else {
            mHandler.removeCallbacks(mFadeRunnable4Froyo);
        }
        isFading = false;
    }

    private Runnable mFadeRunnable4Froyo = new Runnable() {
        @Override
        public void run() {
            if (mAlpha != 0) {
                int alpha = mAlpha - mAlphaDelta;
                if (alpha <= 0) {
                    alpha = 0;
                    isFading = false;
                    triggerListener();
                }
                mAlpha = alpha;
                if (mAlpha <= mBackgroundColorAlpha) mBackgroundColorAlpha = mAlpha;
                invalidateSelf();
                mHandler.postDelayed(this, RippleUtil.FRAME_INTERVAL);
            }
        }
    };

    protected void setPadding(float l, float t, float r, float b) {
        mPaddingLeft = RippleUtil.dip2px(l);
        mPaddingRight = RippleUtil.dip2px(r);
        mPaddingTop = RippleUtil.dip2px(t);
        mPaddingBottom = RippleUtil.dip2px(b);
    }

    protected void setMeasure(int width, int height) {
        mWidth = width;
        mHeight = height;
        setClipBound();
    }

    protected void setMaxRippleRadius(int maxRippleRadius) {
        mMaxRippleRadius = maxRippleRadius;
    }

    public boolean isFull() {
        return isFull;
    }

    protected void setBackgroundDrawable(Drawable backgroundDrawable) {
        mBackgroundDrawable = backgroundDrawable;
        RippleUtil.palette(this, backgroundDrawable, mPaletteMode);
        mDrawableBound = null;
    }

    protected void setPaletteMode(RippleUtil.PaletteMode paletteMode) {
        mPaletteMode = paletteMode;
        RippleUtil.palette(this, mBackgroundDrawable, mPaletteMode);
    }

    protected void setScaleType(ImageView.ScaleType scaleType) {
        mScaleType = scaleType;
        mDrawableBound = null;
    }

    public void addOnFinishListener(OnFinishListener onFinishListener) {
        if (mOnFinishListeners == null) {
            mOnFinishListeners = new ArrayList<>();
        }
        mOnFinishListeners.add(onFinishListener);
    }

    public void setRippleColor(int rippleColor) {
        mRippleColor = rippleColor;
        mBackgroundColor = RippleUtil.produceBackgroundColor(rippleColor);
    }

    private void setClipBound() {
        if (mClipBound == null) {
            mClipBound = new Rect(mPaddingLeft, mPaddingTop, mWidth - mPaddingRight, mHeight - mPaddingBottom);
        } else {
            mClipBound.set(mPaddingLeft, mPaddingTop, mWidth - mPaddingRight, mHeight - mPaddingBottom);
        }
    }

    protected Rect getDrawableBound() {
        return mDrawableBound;
    }

    protected Rect getClipBound() {
        return mClipBound;
    }

    protected Drawable getBackgroundDrawable() {
        return mBackgroundDrawable;
    }

    private int getAlphaDelta() {
        int times = mFadeDuration / RippleUtil.FRAME_INTERVAL + 1;
        return Color.alpha(mRippleColor) / times;
    }
}
