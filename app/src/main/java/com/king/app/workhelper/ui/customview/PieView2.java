package com.king.app.workhelper.ui.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author VanceKing
 * @since 2018/1/2.
 */

public class PieView2 extends View {
    //小于此比例不显示折线
    private static final double LOWER_LIMIT = 0.005;
    private static final double CHART_PERCENT = 0.38;
    private static final int FULL_ANGLE = 360;
    private static final int START_ANGLE = -90;
    //圆环宽度
    private int mArcWidth = dip2px(35);
    //拐点到圆环的距离
    private int mLength = dip2px(15);
    //拐点到终点的距离
    private int length2 = dip2px(70);
    //中间圆环的半径
    private int mCenterCircleRadius = dip2px(45);
    //折线终点圆环的半径
    private int mSmallPointRadius = dip2px(3);
    //代表品类的圆环半径
    private int mCategoryCircleRadius = dip2px(6);
    //品类圆环距离文字的距离
    private int mCategoryCirclePadding = dip2px(12);
    //折线上下的文字距离折线的距离
    private int mExtraPadding = dip2px(3);

    public static final int ASC = 0;
    public static final int DESC = 1;
    private ValueAnimator mAnimator;
    private float mAnimatedFraction;

    @IntDef({ASC, DESC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SORT_TYPE {

    }

    private float mTextHeight;
    private int mCenterX;
    private int mCenterY;

    private int mBlackTextColor = Color.parseColor("#2c2c2c");
    private StaticLayout mTextLayout;
    private final Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint mCenterTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mCategoryCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final RectF mPieRectF = new RectF();
    final int[] mMiddlePoint = new int[2];
    final int[] mEndPoint = new int[2];

    private final List<PieItem> mPies = new ArrayList<>();
    private double mTotalValue;
    private String mCenterText;
    private boolean mIsDrawText;
    private boolean mIsStartDraw;

    public PieView2(Context context) {
        this(context, null);
    }

    public PieView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mCirclePaint.setStyle(Paint.Style.FILL);

        mTextPaint.setColor(mBlackTextColor);
        mTextPaint.setTextSize(sp2px(15));
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.descent - fontMetrics.ascent;

        mCenterTextPaint.setColor(mBlackTextColor);
        mCenterTextPaint.setTextSize(sp2px(15));

        mCategoryCirclePaint.setStyle(Paint.Style.STROKE);
        mCategoryCirclePaint.setStrokeWidth(dip2px(3));

        mAnimator = new ValueAnimator();
        mAnimator.setFloatValues(0, 1);
        mAnimator.setStartDelay(200);
        mAnimator.setDuration(800);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedFraction = animation.getAnimatedFraction();
                invalidate();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mIsDrawText = false;
                mIsStartDraw = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsDrawText = true;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        mCenterX = width / 2;
        mCenterY = (int) (height * CHART_PERCENT);

        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();

        final int availableWidth = width - paddingLeft - paddingRight;
        final int availableHeight = height - paddingTop - paddingBottom;

        final int length = mArcWidth + mCenterCircleRadius;
        mPieRectF.set(mCenterX - length, mCenterY - length, mCenterX + length, mCenterY + length);

        if (mIsStartDraw) {
            drawArc(canvas);
        }

        //画中间的圆形
        mCirclePaint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterX, mCenterY, mCenterCircleRadius, mCirclePaint);
        //画中间的文字
        drawCenterText(canvas);

        if (mIsDrawText) {
            //画折线和上下的文字
            drawLine(canvas);
        }

        drawCategory(canvas, availableWidth, availableHeight, paddingLeft, paddingRight);
    }

    public void drawPies(String text, List<PieItem> pies, @SORT_TYPE int sortType) {
        if (text == null || TextUtils.isEmpty(text.trim()) || pies == null || pies.isEmpty()) {
            return;
        }
        if (!mPies.isEmpty()) {
            mPies.clear();
        }
        mPies.addAll(pies);

        mCenterText = text;
        mTextLayout = new StaticLayout(mCenterText, mCenterTextPaint, (int) mTextPaint.measureText(text), Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);

        sortPies(sortType);
        mTotalValue = getTotalValue();

        if (!mAnimator.isRunning()) {
            mAnimator.start();
        }
    }

    private void drawArc(Canvas canvas) {
        //画每个扇形
        int startAngle = START_ANGLE;
        for (int i = 0, size = mPies.size(); i < size; i++) {
            final PieItem item = mPies.get(i);
            mCirclePaint.setColor(item.color);
            float angle = (float) (item.value / mTotalValue) * FULL_ANGLE * mAnimatedFraction;
            //+1是为了不出现空隙，-1是为了不越界
            canvas.drawArc(mPieRectF, startAngle, i == size - 1 ? angle : angle + 1, true, mCirclePaint);
            startAngle += angle;
        }
    }

    private void drawLine(Canvas canvas) {
        float beforeValue = 0;
        for (int i = 0, size = mPies.size(); i < size; i++) {
            final PieItem item = mPies.get(i);

            float percent = (float) ((beforeValue + item.value / 2) / mTotalValue);
            int pointX = getStartPointX(mCenterX, mCenterCircleRadius + mArcWidth, percent);
            int pointY = getStartPointY(mCenterY, mCenterCircleRadius + mArcWidth, percent);

            calcMiddleAndEndPoint(pointX, pointY, percent, i);
            if (item.value / mTotalValue > LOWER_LIMIT) {
                mCirclePaint.setColor(item.color);
                canvas.drawLine(pointX, pointY, mMiddlePoint[0], mMiddlePoint[1], mCirclePaint);
                canvas.drawLine(mMiddlePoint[0], mMiddlePoint[1], mEndPoint[0], mEndPoint[1], mCirclePaint);
                canvas.drawCircle(mEndPoint[0], mEndPoint[1], mSmallPointRadius, mCirclePaint);
                drawPointText(canvas, item);
            }

            beforeValue += item.value;
        }
    }

    private void drawPointText(Canvas canvas, PieItem item) {
        if (mCenterX - mEndPoint[0] > 0) {//二、三象限
            //横线上部的文字
            mTextPaint.setColor(mBlackTextColor);
            canvas.drawText(item.primaryText, mEndPoint[0] + mExtraPadding, mEndPoint[1] - mExtraPadding - 5, mTextPaint);
            //横线下部的文字
            mTextPaint.setColor(item.color);
            canvas.drawText(item.secondText, mEndPoint[0] + mExtraPadding, mEndPoint[1] + mTextHeight, mTextPaint);
        } else {//一，四象限
            float textWidth = mTextPaint.measureText(item.primaryText);
            mTextPaint.setColor(mBlackTextColor);
            canvas.drawText(item.primaryText, mEndPoint[0] - textWidth - mExtraPadding, mEndPoint[1] - mExtraPadding - 5, mTextPaint);
            mTextPaint.setColor(item.color);
            canvas.drawText(item.secondText, mEndPoint[0] - textWidth - mExtraPadding, mEndPoint[1] + mTextHeight, mTextPaint);
        }
    }

    private void drawCenterText(Canvas canvas) {
        //画中心点文字
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        if (mTextLayout != null) {
            canvas.save();
            float textWidth = mTextPaint.measureText(mCenterText);
            canvas.translate(mCenterX - textWidth / 2, mCenterY - (metrics.bottom - metrics.top));
            mTextLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void drawCategory(Canvas canvas, int availableWidth, int availableHeight, int paddingLeft, int paddingBottom) {
        final int eachWidth = availableWidth / mPies.size();
        for (int i = 0, size = mPies.size(); i < size; i++) {
            final PieItem item = mPies.get(i);

            //画底部的种类的文字
            mTextPaint.setColor(mBlackTextColor);
            drawHorizontalCenterText(canvas, paddingLeft + eachWidth / 2 + i * eachWidth,
                    availableHeight - paddingBottom - mTextHeight,
                    item.secondText, mTextPaint);

            //画底部的种类的圆圈
            mCategoryCirclePaint.setColor(item.color);
            canvas.drawCircle(paddingLeft + eachWidth / 2 + i * eachWidth,
                    availableHeight - paddingBottom - mTextHeight - 2 * mCategoryCircleRadius - mCategoryCirclePadding,
                    mCategoryCircleRadius,
                    mCategoryCirclePaint);
        }
    }

    private void sortPies(@SORT_TYPE final int type) {
        Collections.sort(mPies, new Comparator<PieItem>() {
            @Override
            public int compare(PieItem o1, PieItem o2) {
                if (o1.value < o2.value) {
                    return type == ASC ? -1 : 1;
                } else if (o1.value > o2.value) {
                    return type == ASC ? 1 : -1;
                } else {
                    return 0;
                }
            }
        });
    }

    private double getTotalValue() {
        double result = 0;
        for (PieItem item : mPies) {
            result += item.value;
        }
        return result;
    }

    private void drawHorizontalCenterText(Canvas canvas, float centerX, float centerY, String text, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float baseLine = -(fontMetrics.ascent + fontMetrics.descent) / 2;
        float textWidth = paint.measureText(text);
        float startX = centerX - textWidth / 2;
        float endY = centerY + baseLine;
        canvas.drawText(text, startX, endY, paint);

    }

    private int getStartPointX(int centerX, int radius, double percent) {
        return centerX + (int) (radius * Math.sin(percent * 2 * Math.PI));
    }

    private int getStartPointY(int centerY, int radius, double percent) {
        return centerY - (int) (radius * Math.cos(percent * 2 * Math.PI));
    }

    private void calcMiddleAndEndPoint(int startPointX, int startPointY, double percent, int position) {
        final int x = mCenterX - startPointX;
        final int y = mCenterY - startPointY;
        if (x == 0 && y > 0) {//y轴正半轴
            mMiddlePoint[0] = mCenterX;
            mMiddlePoint[1] = startPointY - mLength;

            mEndPoint[0] = mMiddlePoint[0] - length2;
        } else if (x == 0 && y < 0) {//y轴负半轴
            mMiddlePoint[0] = mCenterX;
            mMiddlePoint[1] = startPointY + mLength;

            mEndPoint[0] = mMiddlePoint[0] + length2;
        } else if (x < 0 && y == 0) {//x轴正半轴
            mMiddlePoint[0] = startPointX + mLength;
            mMiddlePoint[1] = mCenterY;

            mEndPoint[0] = mMiddlePoint[0] + length2;
        } else if (x > 0 && y == 0) {//x轴负半轴
            mMiddlePoint[0] = startPointX - mLength;
            mMiddlePoint[1] = mCenterY;

            mEndPoint[0] = mMiddlePoint[0] - length2;
        } else if (isFirstQuadrant(startPointX, startPointY)) {
            if (position == 0) {
                if (percent < 0.1) {
                    //向左
                    mMiddlePoint[0] = startPointX - (int) (mLength * Math.sin(percent * 2 * Math.PI));
                    mMiddlePoint[1] = startPointY - (int) (mLength * Math.cos(percent * 2 * Math.PI));
                    mEndPoint[0] = mMiddlePoint[0] - length2;
                } else {
                    mMiddlePoint[0] = startPointX + (int) (mLength * Math.sin(percent * 2 * Math.PI));
                    mMiddlePoint[1] = startPointY - (int) (mLength * Math.cos(percent * 2 * Math.PI));
                    mEndPoint[0] = mMiddlePoint[0] + length2;
                }
            } else if (position == 1) {
                mMiddlePoint[0] = startPointX + (int) (mLength * Math.sin(percent * 2 * Math.PI));
                mMiddlePoint[1] = startPointY - (int) (mLength * Math.cos(percent * 2 * Math.PI)) - dip2px(30);
                mEndPoint[0] = mMiddlePoint[0] + length2;
            } else if (position == 2) {
                mMiddlePoint[0] = startPointX + (int) (mLength * Math.sin(percent * 2 * Math.PI));
                mMiddlePoint[1] = startPointY - (int) (mLength * Math.cos(percent * 2 * Math.PI));
                mEndPoint[0] = mMiddlePoint[0] + length2;
            }


        } else if (isSecondQuadrant(startPointX, startPointY)) {
            mMiddlePoint[0] = startPointX + (int) (mLength * Math.sin(percent * 2 * Math.PI));
            mMiddlePoint[1] = startPointY - (int) (mLength * Math.cos(percent * 2 * Math.PI));

            mEndPoint[0] = mMiddlePoint[0] + length2;
        } else if (isThirdQuadrant(startPointX, startPointY)) {
            mMiddlePoint[0] = startPointX + (int) (mLength * Math.sin(percent * 2 * Math.PI));
            mMiddlePoint[1] = startPointY - (int) (mLength * Math.cos(percent * 2 * Math.PI));

            mEndPoint[0] = mMiddlePoint[0] - length2;
        } else if (isForthQuadrant(startPointX, startPointY)) {
            mMiddlePoint[0] = startPointX + (int) (mLength * Math.sin(percent * 2 * Math.PI));
            mMiddlePoint[1] = startPointY - (int) (mLength * Math.cos(percent * 2 * Math.PI));

            mEndPoint[0] = mMiddlePoint[0] - length2;
        } else {
            mMiddlePoint[0] = 0;
            mMiddlePoint[1] = 1;
        }
        mEndPoint[1] = mMiddlePoint[1];
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private boolean isFirstQuadrant(int x, int y) {
        return mCenterX < x && mCenterY > y;
    }

    private boolean isSecondQuadrant(int x, int y) {
        return mCenterX < x && mCenterY < y;
    }

    private boolean isThirdQuadrant(int x, int y) {
        return mCenterX > x && mCenterY < y;
    }

    private boolean isForthQuadrant(int x, int y) {
        return mCenterX > x && mCenterY > y;
    }

    public static class PieItem {
        public double value;
        @ColorInt
        public int color;
        public String primaryText;
        public String secondText;

        public PieItem(String primaryText, String secondText, double value, int color) {
            this.primaryText = primaryText;
            this.secondText = secondText;
            this.value = value;
            this.color = color;
        }

    }
}
