package com.king.app.workhelper.ui.customview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * 发散点.
 * @author dongqi
 * @since 2016/3/21.
 */
//CHECKSTYLE:OFF
public class SparkItem {
    private static final String TAG = "SparkItem";
    protected Bitmap mImage;
    public float mCurrentX;
    public float mCurrentY;
    private int pos;
    public float mScale = 1.5f;
    public int mAlpha = 255;
    public float mInitialRotation = 0f;
    public float mRotationSpeed = 0f;
    public float mSpeedX = 0f;
    public float mSpeedY = 0f;
    private Matrix mMatrix;
    private Paint mPaint;
    private float mInitialX;
    private float mInitialY;
    private int mBitmapHalfWidth;
    private int mBitmapHalfHeight;
    protected SparkItem() {
        mMatrix = new Matrix();
        mPaint = new Paint();
    }

    public SparkItem(Bitmap bitmap) {
        this();
        mImage = bitmap;
    }

    public void init() {
        mScale = 1;
        mAlpha = 255;
    }

    /**
     * 配置参数.
     * @param pos spark位置
     * @param speedY Y加速度.
     * @param speedX
     * @param emiterX 起始位置x.
     * @param emiterY 起始位置y.
     * @param scale depreated.
     * @param totalTime
     */
    public void configure(int pos, float speedY, float speedX, float emiterX, float emiterY, float scale, int totalTime) {
        this.pos = pos;
        mBitmapHalfWidth = mImage.getWidth()/2;
        mBitmapHalfHeight = mImage.getHeight()/2;
        mInitialX = emiterX + (pos - 3) * mBitmapHalfWidth;
        mInitialY = emiterY - mBitmapHalfHeight;
        mCurrentX = mInitialX;
        mCurrentY = mInitialY;
        mSpeedY = speedY;
        mSpeedX = speedX;
        mAlpha = 255;
        mScale = 0.2f;
        mTotalTime = totalTime;
    }

    int i = 0;
    private long mCurrentTime = 0;
    private long mTotalTime;
    public boolean isInTime() {
        return mCurrentTime < mTotalTime;
    }
    public boolean update (int step, long currentTime) {
        mCurrentY = mCurrentY - 2 * mSpeedY; // * ( 1 + (Math.abs((pos - SparkView.BITMAP_SIZE) / 2)));
        if (step == 0) {
            mCurrentX = mInitialX;
            mCurrentY = mInitialY;
            mCurrentTime = 0;
            mScale = 0.2f;
        }
        if (mCurrentTime < 50) { // mTotalTime / 4
            mScale = 0.2f;
        } else
        if (mCurrentTime < mTotalTime / 3){
            mScale *= 1.3f;
        } else {
            mScale *= 0.9f;
        }
        mCurrentTime += currentTime;
        return true;
    }

    public void draw (Canvas c) {
        mMatrix.reset();
//        mMatrix.postRotate(mRotation, mBitmapHalfWidth, mBitmapHalfHeight);
        mMatrix.postScale(mScale, mScale, mBitmapHalfWidth, mBitmapHalfHeight);
        mMatrix.postTranslate(mCurrentX, mCurrentY);
//        mPaint.setAlpha(mAlpha);
        c.drawBitmap(mImage, mMatrix, mPaint);
    }
}
//CHECKSTYLE:ON