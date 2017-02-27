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

        mValueAnimator = ValueAnimator.ofInt(-200, 200);
        mValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setIntValues(-200, 200);
        mValueAnimator.setDuration(5000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
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

        mRectF.set(mCenterX - 120, mCenterY - mValue, mCenterX + 120, mCenterY + 200);
        canvas.drawRoundRect(mRectF, 10, 10, mPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
    }

    public void startAnim() {
        mValueAnimator.start();
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
}
