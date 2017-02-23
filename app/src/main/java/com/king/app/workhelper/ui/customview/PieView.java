package com.king.app.workhelper.ui.customview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.king.applib.log.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 饼图
 *
 * @author VanceKing
 * @since 2017/2/12.
 */

public class PieView extends View {
    private static final int START_ANGLE = -90;
    private static final int FULL_ANGLE = 360;
    private static final int DEFAULT_STROKE_WIDTH = 50;
    private static final int DEFAULT_RADIUS = 150;
    private static final int DOT_RADIUS = 6;//连线终点圆圈的半径
    private static final int BOTTOM_DOT_RADIUS = 10;//底部圆圈的半径
    private static final int SECOND_TEXT_PADDING_BOTTOM = 20;//底部文字距底边的距离
    private static final int SECOND_TEXT_TEXT_SIZE = 12;//底部文字大小(dp)
    private static final int PRIMARY_TEXT_TEXT_SIZE = 12;//连线终点文字大小(dp)

    private String mCenterText;
    private StaticLayout mTextLayout;
    private RectF mPieRectF;

    private TextPaint mTextPaint;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<PieItem> mPies = new ArrayList<>();

    public static final int ASC = 0;
    public static final int DESC = 1;

    @IntDef({ASC, DESC})
    public @interface SORT_TYPE {

    }

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPieRectF = new RectF();

        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(dip2px(getContext(), 16));
        mTextPaint.setAntiAlias(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(resolveSizeAndState(Integer.MAX_VALUE, widthMeasureSpec, 0),
                resolveSizeAndState(500, heightMeasureSpec, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float totalValue = 0;
        for (PieItem pieItem : mPies) {
            if (pieItem != null) {
                totalValue += pieItem.value;
            }
        }
        if (totalValue == 0) {
            return;
        }

        final int width = getWidth();
        final int height = getHeight();
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        final int availableWidth = width - paddingLeft - paddingRight;
        final int availableHeight = height - paddingTop - paddingBottom;

        final int maxWidth = Math.min(availableWidth, availableHeight);
        final int radius = Math.min(DEFAULT_RADIUS, maxWidth / 2);

        final int centerX = paddingLeft + availableWidth / 2;
        final int centerY = paddingTop + availableHeight / 2 - 50;

        // TODO: 2017/2/22 没有处理paddingBottom和paddingRight 
        mPieRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);

        //画每个饼图
        float startAngle = START_ANGLE;
        for (PieItem pieItem : mPies) {
            if (pieItem == null) {
                continue;
            }
            mPaint.setColor(pieItem.color);
            float pieAngle = (float) (pieItem.value / totalValue) * FULL_ANGLE;
            canvas.drawArc(mPieRectF, startAngle, pieAngle + 1, false, mPaint);//不加1后留下缝隙
            startAngle += pieAngle;
        }

        final int tempRadius = radius + DEFAULT_STROKE_WIDTH / 2;
        float beforeValue = 0;
        for (int i = 0, size = mPies.size(); i < size; i++) {//PieItem item : mPies
            PieItem item = mPies.get(i);
            if (item == null) {
                continue;
            }
            mPaint.setColor(item.color);
            mPaint.setStrokeWidth(2);
            float percent = (float) (beforeValue + item.value / 2) / totalValue;

            int p0X = getPointX(centerX, tempRadius, percent);
            int p0Y = getPointY(centerY, tempRadius, percent);

            float p1X, p1Y;
            float textX, textY;
            if (isFirstQuadrant(centerX, centerY, p0X, p0Y) || isSecondQuadrant(centerX, centerY, p0X, p0Y)) {
                if (i == 0 && !isUpThreshold(item.value, totalValue)) {
                    p1X = p0X - 100;
                } else if (i == 1 && !isUpThreshold(item.value, totalValue)) {
                    p1X = p0X;
                } else if (i == 2 && !isUpThreshold(item.value, totalValue)) {
                    p1X = p0X + 100;
                } else {
                    p1X = p0X + item.marginLeft;
                }

                if (i == 0 && !isUpThreshold(item.value, totalValue)) {
                    textX = p1X - mPaint.measureText(item.label) - 20;
                } else {
                    textX = p1X + 5;
                }
            } else {
                p1X = p0X - item.marginLeft;
                textX = p1X - 40;
            }
            if (isFirstQuadrant(centerX, centerY, p0X, p0Y) || isForthQuadrant(centerX, centerY, p0X, p0Y)) {
                p1Y = p0Y - item.marginBottom;
                textY = p1Y - 5;
            } else {
                p1Y = p0Y + item.marginBottom;
                textY = p1Y + 20;
            }

            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawLine(p0X, p0Y, p1X, p1Y, mPaint);//画连线
            canvas.drawCircle(p1X, p1Y, DOT_RADIUS, mPaint);//画连线终点的小圆点

            //画连线终点文字
            mPaint.setStrokeWidth(1);
            mPaint.setTextSize(dip2px(getContext(), PRIMARY_TEXT_TEXT_SIZE));
            mPaint.setColor(Color.BLACK);
            canvas.drawText(item.label, textX, textY, mPaint);

            beforeValue += item.value;
        }


        //画内圆
        /*mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(centerX, centerY, radius - DEFAULT_STROKE_WIDTH / 2, mPaint);*/

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(18);
        mPaint.setStyle(Paint.Style.FILL);

        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        Logger.i("metrics.ascent: " + metrics.ascent + ";metrics.bottom: " + metrics.bottom + ";metrics.descent: " +
                metrics.descent + ";metrics.leading: " + metrics.leading + ";metrics.top: " + metrics.top);


        //画中心点文字
        if (mTextLayout != null) {
            canvas.save();
            float textWidth = mTextPaint.measureText(mCenterText);
            canvas.translate(centerX - textWidth / 2, centerY - (metrics.bottom - metrics.top));
            mTextLayout.draw(canvas);
            canvas.restore();
        }

        int eachWidth = availableWidth / mPies.size();
        for (int i = 0, size = mPies.size(); i < size; i++) {
            PieItem item = mPies.get(i);
            if (item == null) {
                continue;
            }

            //画底部的种类的文字
            mTextPaint.setTextSize(dip2px(getContext(), SECOND_TEXT_TEXT_SIZE));
            mTextPaint.setColor(Color.BLACK);
            Paint.FontMetrics metrics1 = mTextPaint.getFontMetrics();
            drawHorizontalCenterText(canvas, paddingLeft + eachWidth / 2 + i * eachWidth,
                    height - paddingBottom - (metrics1.descent - metrics1.ascent) / 2 - SECOND_TEXT_PADDING_BOTTOM,
                    item.label, mTextPaint);

            //画底部的种类的圆圈
            mPaint.setStrokeWidth(3);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(item.color);
            canvas.drawCircle(paddingLeft + eachWidth / 2 + i * eachWidth,
                    height - paddingBottom - (metrics1.descent - metrics1.ascent) - SECOND_TEXT_PADDING_BOTTOM - BOTTOM_DOT_RADIUS * 2, BOTTOM_DOT_RADIUS,
                    mPaint);
        }

    }

    private int getPointX(int centerX, int radius, double percent) {
        return centerX + (int) (radius * Math.sin(percent * 2 * Math.PI));
    }

    private int getPointY(int centerY, int radius, double percent) {
        return centerY - (int) (radius * Math.cos(percent * 2 * Math.PI));
    }

    public static class PieItem {
        public double value;
        @ColorInt
        public int color;
        public int marginLeft = 50;//拐点左边距
        public int marginBottom = 50;//拐点底部边距
        public int lineLength = 80;
        private String label;

        public PieItem(double value, @ColorInt int color) {
            this.value = value;
            this.color = color;
        }

        public PieItem(String label, double value, int color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }

        public PieItem(String label, double value, int color, int marginLeft, int marginBottom) {
            this.label = label;
            this.value = value;
            this.color = color;
            this.marginLeft = marginLeft;
            this.marginBottom = marginBottom;
        }
    }

    public void drawPies(List<PieItem> pies, @SORT_TYPE int sortType) {
        if (pies != null && !pies.isEmpty()) {
            mPies.clear();
            mPies.addAll(pies);

            if (mCenterText != null && !mCenterText.trim().isEmpty()) {
                float width = mTextPaint.measureText(mCenterText);
                mTextLayout = new StaticLayout(mCenterText, mTextPaint, (int) width, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
            }

            sortPies(sortType);
            postInvalidate();
        }
    }

    public void setCenterText(String text) {
        if (text != null) {
            mCenterText = text;
        }
    }

    private void sortPies(@SORT_TYPE final int type) {
        Collections.sort(mPies, new Comparator<PieItem>() {
            @Override public int compare(PieItem o1, PieItem o2) {
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

    private boolean isUpThreshold(double value, float totalValue) {
        return value / totalValue > 0.1;
    }

    private boolean isFirstQuadrant(int centerX, int centerY, int destX, int destY) {
        return centerX < destX && centerY > destY;
    }

    private boolean isSecondQuadrant(int centerX, int centerY, int destX, int destY) {
        return centerX < destX && centerY < destY;
    }

    private boolean isThirdQuadrant(int centerX, int centerY, int destX, int destY) {
        return centerX > destX && centerY < destY;
    }

    private boolean isForthQuadrant(int centerX, int centerY, int destX, int destY) {
        return centerX > destX && centerY > destY;
    }


    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void drawHorizontalCenterText(Canvas canvas, float centerX, float centerY, String text, Paint paint) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float baseLine = -(fontMetrics.ascent + fontMetrics.descent) / 2;
        float textWidth = paint.measureText(text);
        float startX = centerX - textWidth / 2;
        float endY = centerY + baseLine;
        canvas.drawText(text, startX, endY, paint);

    }
}
