package com.king.app.workhelper.ui.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.king.app.workhelper.R;

/**
 * @author VanceKing
 * @since 2017/2/26 0026.
 */

public class TestView extends View {
    private static final String TEXT = "哈哈哈\r\n呵呵呵呵";
    private static final int RADIUS = 200;
    private Paint mPaint;
    private StaticLayout mStaticLayout;
    private TextPaint mTextPaint;
    private int mWidth;
    private int mHeight;
    private RectF mRectF;
    private int mCenterX;
    private int mCenterY;

    private ValueAnimator mValueAnimator;
    private RotateAnimation mProgressRotateAnim;
    private int mValue;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.chocolate));

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(dip2px(getContext(), 18));
        mStaticLayout = new StaticLayout(TEXT, mTextPaint, (int) mTextPaint.measureText(TEXT), Layout.Alignment.ALIGN_CENTER, 1, 0, true);

        mRectF = new RectF();

        mValueAnimator = ValueAnimator.ofInt(0, 360);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
//        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setDuration(2000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        mProgressRotateAnim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mProgressRotateAnim.setDuration(1500);
        mProgressRotateAnim.setRepeatCount(Animation.INFINITE);
        mProgressRotateAnim.setInterpolator(new LinearInterpolator());//不停顿
        mProgressRotateAnim.setFillAfter(true);//停在最后
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.rotate(mValue, mCenterX, mCenterY);
        /*for (double i = 0; i < 8; i++) {
            mPaint.setAlpha(162 + 162 * (int)i / 8);
            canvas.drawCircle(getPointX(100, i / 8), getPointY(100, i / 8), 20, mPaint);
        }*/

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        canvas.drawCircle(mCenterX, mCenterY, 100, mPaint);

        canvas.drawCircle(mCenterX, mCenterY, 3, mPaint);

        mRectF.set(mCenterX - 80, mCenterY - 80, mCenterX + 80, mCenterY + 80);
        canvas.drawArc(mRectF, -120, 60, false, mPaint);

        mRectF.set(mCenterX - 70, mCenterY - 70, mCenterX + 70, mCenterY + 70);
        canvas.drawArc(mRectF, -120, 60, false, mPaint);

        mRectF.set(mCenterX - 80, mCenterY - 80, mCenterX + 80, mCenterY + 80);
        canvas.drawArc(mRectF, 60, 60, false, mPaint);

        mRectF.set(mCenterX - 70, mCenterY - 70, mCenterX + 70, mCenterY + 70);
        canvas.drawArc(mRectF, 60, 60, false, mPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
    }

    public void startAnim() {
//        mValueAnimator.start();
//        mProgressRotateAnim.start();
        startAnimation(mProgressRotateAnim);
    }

    //把换行的文字画到圆的正中央
    private void drawCenterTextInCircle(Canvas canvas) {
        canvas.drawCircle(mCenterX, mCenterY, RADIUS, mPaint);

        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.chocolate));
        canvas.drawLine(mCenterX - RADIUS, mCenterY, mCenterX + RADIUS, mCenterY, mPaint);
        canvas.drawLine(mCenterX, mCenterY - RADIUS, mCenterX, mCenterY + RADIUS, mPaint);

        canvas.save();
        float textHeight = getTextHeight(mTextPaint) * mStaticLayout.getLineCount();
        canvas.translate(mCenterX - mTextPaint.measureText(TEXT) / 2, mCenterY - textHeight / 2);
        mStaticLayout.draw(canvas);
        canvas.restore();
    }

    private float getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent;
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getPointX(int radius, double percent) {
        return mCenterX + (int) (radius * Math.sin(percent * 2 * Math.PI));
    }

    private int getPointY(int radius, double percent) {
        return mCenterY - (int) (radius * Math.cos(percent * 2 * Math.PI));
    }
}
