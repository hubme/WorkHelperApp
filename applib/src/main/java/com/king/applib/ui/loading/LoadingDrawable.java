/*
 *     Copyright 2017 GuDong
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *
 *
 */

package com.king.applib.ui.loading;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.king.applib.R;

import java.lang.ref.WeakReference;


/**
 * gudong (gudong.name@gmail.com)
 * https://github.com/maoruibin/FlowerLoading
 * 中心旋转Drawable
 *
 * @author VanceKing
 * @since 2017/12/8.
 */
public class LoadingDrawable extends Drawable implements Animatable {
    private static final int DEFAULT_STEP_COUNT = 12;
    private static final int DEFAULT_DURATION = 1200;

    private int mAverageCount = DEFAULT_STEP_COUNT;
    private int mDuration = DEFAULT_DURATION;

    private Bitmap mSource;
    private int mCurrentDegree = 0;
    private float mScaleY = 1;
    private float mScaleX = 1;
    private int mOffset = -1;
    private int mAlpha;
    private int mCenterXY;
    private boolean isAnimRunning;
    private final Matrix mMatrix = new Matrix();

    private Context mContext;
    private Handler mHandler;
    private LoadingTask mLoadingTask;

    public LoadingDrawable(Context context) {
        this(context, R.drawable.applib_flower_loading);
    }

    public LoadingDrawable(Context context, int resDrawable) {
        mSource = BitmapFactory.decodeResource(context.getResources(), resDrawable);
        if (mSource.getWidth() != mSource.getHeight()) {
            throw new IllegalStateException("drawable must have same width and height.");
        }
        mContext = context;
        mHandler = new Handler();
        mLoadingTask = new LoadingTask(this);
    }

    private static class LoadingTask implements Runnable {
        WeakReference<LoadingDrawable> mWeakDrawable;

        LoadingTask(LoadingDrawable drawable) {
            this.mWeakDrawable = new WeakReference<>(drawable);
        }

        @Override
        public void run() {
            LoadingDrawable mDrawable = mWeakDrawable.get();
            if (mDrawable == null) {
                return;
            }
            mDrawable.mCurrentDegree += mDrawable.getRotateStep();
            if (mDrawable.mCurrentDegree >= 360) {
                mDrawable.mCurrentDegree = 0;
            }
            mDrawable.rotateLoading(mDrawable.mCurrentDegree);
            if (mDrawable.isRunning()) {
                mDrawable.mHandler.postDelayed(this, mDrawable.getRotateStepTime());
            }
        }
    }

    private void rotateLoading(int degree) {
        mMatrix.reset();
        mMatrix.postScale(mScaleX, mScaleY);
        if (mOffset >= 0) {
            mMatrix.postTranslate(mOffset, mOffset);
        }
        mMatrix.postRotate(degree, mCenterXY, mCenterXY);
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(getBounds().left, getBounds().top);
        canvas.drawBitmap(mSource, mMatrix, null);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        this.mAlpha = alpha;
    }

    @Override
    public int getAlpha() {
        return mAlpha;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public void start() {
        if (!isRunning()) {
            mHandler.post(mLoadingTask);
            isAnimRunning = true;
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            mHandler.removeCallbacks(mLoadingTask);
            isAnimRunning = false;
        }
    }

    @Override
    public boolean isRunning() {
        return isAnimRunning;
    }

    @Override
    protected void onBoundsChange(Rect drawBounds) {
        super.onBoundsChange(drawBounds);
        int w = drawBounds.width();
        int h = drawBounds.height();
        onSizeChanged(w, h, mSource.getWidth(), mSource.getHeight());
    }

    /**
     * init drawable status
     *
     * @param w              View's width
     * @param h              View's height
     * @param drawableWidth  Loading Drawable's width
     * @param drawableHeight Loading Drawable's height
     */
    private void onSizeChanged(int w, int h, int drawableWidth, int drawableHeight) {
        if (w != h) {
            throw new IllegalStateException("view must have same width and height, now w = " + w + " h =" + h);
        }
        mCenterXY = w / 2;

        // when view's size little than loading image size, the drawable need scale
        if (w <= drawableWidth) {
            mScaleY = h / (float) drawableWidth;
            mScaleX = w / (float) drawableHeight;
        } else {
            // if view size bigger than drawable size, drawable will not scale bigger, now i will keep
            // drawable size and make it center, but as default drawable will be left top, so now i move it to center position
            mOffset = (w - drawableWidth) / 2;
        }
        initLoadingStatus();
    }

    private void initLoadingStatus() {
        setCurrentDegree(0);
    }

    public void setLoadingDrawable(int resDrawable) {
        mSource = BitmapFactory.decodeResource(mContext.getResources(), resDrawable);
    }

    public void setDuration(int duration) {
        if (duration > 0) {
            mDuration = duration;
        }
    }

    public void setStepCount(int stepCount) {
        if (stepCount > 0) {
            mAverageCount = stepCount;
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return mSource.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mSource.getHeight();
    }

    public void setCurrentDegree(int currentDegree) {
        if (currentDegree >= 0 && currentDegree <= 360) {
            mCurrentDegree = currentDegree;
            rotateLoading(currentDegree);
        }
    }

    /**
     * 获取每次旋转角度
     */
    private int getRotateStep() {
        return 360 / mAverageCount;
    }

    /**
     * 获取每次旋转需要的时间
     */
    private int getRotateStepTime() {
        return mDuration / mAverageCount;
    }

}
