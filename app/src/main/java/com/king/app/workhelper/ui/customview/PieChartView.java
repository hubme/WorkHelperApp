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

public class PieChartView extends View {
    private static final int START_ANGLE = -90;
    private static final int FULL_ANGLE = 360;
    private static final int DEFAULT_STROKE_WIDTH = 50;
    private static final int DEFAULT_RADIUS = 150;
    private static final int DOT_RADIUS = 6;//连线终点圆圈的半径
    private static final int BOTTOM_DOT_RADIUS = 10;//底部圆圈的半径
    private static final int SECOND_TEXT_PADDING_BOTTOM = 20;//底部文字距底边的距离
    private static final int SECOND_TEXT_TEXT_SIZE = 12;//底部文字大小(dp)

    private static final int PRIMARY_TEXT_TEXT_SIZE = 12;//连线终点文字大小(dp)
    private static final int LINE_END_DOT_RADIUS = 6;//连线终点圆圈的半径
    private static final int PRIMARY_TEXT_MARGIN = 3;//线上的文字距线的距离
    private static final int SECOND_TEXT_MARGIN = 3;//线下的文字距线的距离
    private static final int END_TEXT_MARGIN = 5;//终点文字距终点的距离

    private static final int CENTER_TEXT_TEXT_SIZE = 16;//圆中心的文字大小(dp)
    private static final int TURN_LINE_LENGTH = 80;//拐点距圆圈的距离

    // TODO: 2017/2/24 根据文字自动调整 
    private static final int END_LINE_LENGTH = 80;//连线终点距拐点的距离

    private String mCenterText = "哈哈哈\r\n呵呵呵呵";
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

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPieRectF = new RectF();

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(dip2px(getContext(), CENTER_TEXT_TEXT_SIZE));
        mTextLayout = new StaticLayout(mCenterText, mTextPaint, (int) mTextPaint.measureText(mCenterText),
                Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

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
        final int centerY = paddingTop + availableHeight / 2;

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
        for (int i = 0, size = mPies.size(); i < size; i++) {
            PieItem item = mPies.get(i);
            if (item == null) {
                continue;
            }
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(1);
            mPaint.setColor(item.color);
            mPaint.setTextSize(dip2px(getContext(), PRIMARY_TEXT_TEXT_SIZE));
            float percent = (float) (beforeValue + item.value / 2) / totalValue;

            int pStartX = getPointX(centerX, tempRadius, percent);
            int pStartY = getPointY(centerY, tempRadius, percent);

            int pTurnX = getPointX(centerX, tempRadius + TURN_LINE_LENGTH, percent);
            int pTurnY;
            if (i == 2 && isBelowThreshold(item.value, totalValue)) {
                Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
                final float lineHeight = fontMetrics.descent - fontMetrics.ascent;
                pTurnY = getPointY(centerY, tempRadius + TURN_LINE_LENGTH - (int)lineHeight, percent);
            } else {
                pTurnY = getPointY(centerY, tempRadius + TURN_LINE_LENGTH, percent);
            }

            int pEndX;
            int pEndY = pTurnY;

            canvas.drawLine(pStartX, pStartY, pTurnX, pTurnY, mPaint);//画折线

            if (isFirstQuadrant(centerX, centerY, pTurnX, pTurnY) || isSecondQuadrant(centerX, centerY, pTurnX, pTurnY)) {
                if (i == 0 && isBelowThreshold(item.value, totalValue)) {
                    final float textWidth = Math.max(mPaint.measureText(item.primaryText), mPaint.measureText(item.secondText));
                    pEndX = pTurnX - (int) textWidth;
                } else if (i == 2 && isBelowThreshold(item.value, totalValue)) {
                    pEndX = pTurnX + 2 * END_LINE_LENGTH;
                } else {
                    pEndX = pTurnX + END_LINE_LENGTH;
                }
            } else {
                pEndX = pTurnX - END_LINE_LENGTH;
            }
            canvas.drawLine(pTurnX, pTurnY, pEndX, pEndY, mPaint);//画水平线
            canvas.drawCircle(pEndX, pEndY, LINE_END_DOT_RADIUS, mPaint);//画终点圆点

            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            final float lineHeight = fontMetrics.descent - fontMetrics.ascent;
            final float textWidth = mPaint.measureText(item.primaryText);

            if (isFirstQuadrant(centerX, centerY, pTurnX, pTurnY) || isSecondQuadrant(centerX, centerY, pTurnX, pTurnY)) {
                canvas.drawText(item.primaryText,
                        pEndX - textWidth - LINE_END_DOT_RADIUS / 2 - END_TEXT_MARGIN,
                        pEndY - lineHeight / 2 + fontMetrics.descent - PRIMARY_TEXT_MARGIN, mPaint);
                canvas.drawText(item.secondText,
                        pEndX - textWidth - LINE_END_DOT_RADIUS / 2 - END_TEXT_MARGIN,
                        pEndY + lineHeight / 2 + fontMetrics.descent + SECOND_TEXT_MARGIN, mPaint);
            } else {
                canvas.drawText(item.primaryText,
                        pEndX + LINE_END_DOT_RADIUS / 2 + END_TEXT_MARGIN,
                        pEndY - fontMetrics.descent - PRIMARY_TEXT_MARGIN, mPaint);
                canvas.drawText(item.secondText,
                        pEndX + LINE_END_DOT_RADIUS / 2 + END_TEXT_MARGIN,
                        pEndY + lineHeight - fontMetrics.descent + SECOND_TEXT_MARGIN, mPaint);
            }

            beforeValue += item.value;
        }


        //画内圆
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(centerX, centerY, radius - DEFAULT_STROKE_WIDTH / 2, mPaint);

        //画圆中心的文字
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        Logger.i("metrics.ascent: " + metrics.ascent + ";metrics.bottom: " + metrics.bottom + ";metrics.descent: " +
                metrics.descent + ";metrics.leading: " + metrics.leading + ";metrics.top: " + metrics.top);
        if (mTextLayout != null) {
            canvas.save();
            final float textHeight = (metrics.bottom - metrics.top) * mTextLayout.getLineCount();
            canvas.translate(centerX - mTextPaint.measureText(mCenterText) / 2, centerY - textHeight / 2);
            mTextLayout.draw(canvas);
            canvas.restore();
        }

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        /*int eachWidth = availableWidth / mPies.size();
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
                    item.primaryText, mTextPaint);

            //画底部的种类的圆圈
            mPaint.setStrokeWidth(3);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(item.color);
            canvas.drawCircle(paddingLeft + eachWidth / 2 + i * eachWidth,
                    height - paddingBottom - (metrics1.descent - metrics1.ascent) - SECOND_TEXT_PADDING_BOTTOM - BOTTOM_DOT_RADIUS * 2, BOTTOM_DOT_RADIUS,
                    mPaint);
        }*/

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
        private String primaryText;
        private String secondText;

        public PieItem(String primaryText, String secondText, double value, int color) {
            this.primaryText = primaryText;
            this.secondText = secondText;
            this.value = value;
            this.color = color;
        }
    }

    public void drawPies(List<PieItem> pies, @SORT_TYPE int sortType) {
        if (pies != null && !pies.isEmpty()) {
            mPies.clear();
            mPies.addAll(pies);

            if (mCenterText != null && !mCenterText.trim().isEmpty()) {
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

    private boolean isBelowThreshold(double value, float totalValue) {
        return value / totalValue <= 0.01;
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
