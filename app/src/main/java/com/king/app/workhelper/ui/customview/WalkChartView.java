package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author VanceKing
 * @since 2018/7/14.
 */
public class WalkChartView extends View {

    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;
    private int mWidth;
    private int mHeight;
    private final TextPaint mTextPaint;

    private final int mNormalColor = Color.parseColor("#999999");

    private final String chartTitle = "步频曲线";
    private final String speedTitle = "配速：步/分钟";
    private final String totalStepTitle = "总步数：";
    private final String averageStepTitle = "平均：";
    private final String fastestStepTitle = "最快：";


    private int mChartTitleYPos;
    private int mOverViewYPos;

    //最顶端和最底端的的线
    private int mLineTopY;
    private int mLineBottomY;
    private int mLineLeftX;
    private int mLineWidth;

    private String mTotalSteps = "470";
    private String mAverageSteps = "107";
    private String mFastestSteps = "111";

    private final List<Integer> mXPos = new ArrayList<>();
    private final List<Integer> mYPos = new ArrayList<>();

    public WalkChartView(Context context) {
        this(context, null);
    }

    public WalkChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WalkChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        init();
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();
    }

    private void init() {
        mYPos.add(0);
        mYPos.add(10);
        mYPos.add(50);
        mYPos.add(100);
        mYPos.add(150);

        for (int i = 0; i < 15; i++) {
            mXPos.add(i + 1);
        }
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawChartTitle(canvas);
        drawOverViewText(canvas);
        drawYAxis(canvas);
        drawXAxis(canvas);
        drawPoints(canvas);
    }

    private void drawChartTitle(Canvas canvas) {
        mTextPaint.setColor(Color.parseColor("#4A4A4A"));
        mTextPaint.setTextSize(sp2px(17));
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        mChartTitleYPos = mPaddingTop + (int) getTextHeight();
        canvas.drawText(chartTitle, mPaddingLeft, mChartTitleYPos, mTextPaint);
    }

    private void drawOverViewText(Canvas canvas) {
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setColor(mNormalColor);
        mTextPaint.setTextSize(sp2px(12));

        int offset = 0;

        mOverViewYPos = mChartTitleYPos + dp2px(8) + (int) getTextHeight();
        canvas.drawText(speedTitle, mPaddingLeft, mOverViewYPos, mTextPaint);
        offset += getTextWidth(speedTitle) + dp2px(50);

        canvas.drawText(totalStepTitle, offset, mOverViewYPos, mTextPaint);
        offset += getTextWidth(totalStepTitle);

        mTextPaint.setColor(Color.parseColor("#30C4FF"));
        canvas.drawText(mTotalSteps, offset, mOverViewYPos, mTextPaint);
        offset += getTextWidth(mTotalSteps) + dp2px(10);

        mTextPaint.setColor(mNormalColor);
        canvas.drawText(averageStepTitle, offset, mOverViewYPos, mTextPaint);
        offset += getTextWidth(averageStepTitle);

        mTextPaint.setColor(Color.parseColor("#30C4FF"));
        canvas.drawText(mAverageSteps, offset, mOverViewYPos, mTextPaint);
        offset += getTextWidth(mAverageSteps) + dp2px(10);

        mTextPaint.setColor(mNormalColor);
        canvas.drawText(fastestStepTitle, offset, mOverViewYPos, mTextPaint);
        offset += getTextWidth(fastestStepTitle);

        mTextPaint.setColor(Color.parseColor("#FF496E"));
        canvas.drawText(mFastestSteps, offset, mOverViewYPos, mTextPaint);
    }

    //画纵坐标刻度和横线
    private void drawYAxis(Canvas canvas) {
        mLineTopY = mOverViewYPos + dp2px(9);

//        canvas.drawLine(0, mLineTopY, mWidth, mLineTopY, mTextPaint);

        mTextPaint.setColor(mNormalColor);
        mTextPaint.setTextSize(sp2px(10));
        // TODO: 2018/7/14 数据是从小到大还是从大到小
        Collections.reverse(mYPos);
        int padding = dp2px(16);
        int itemHeight = (int) getTextHeight();
        int textRight = mPaddingLeft + getTextWidth("999");
        mLineLeftX = textRight + dp2px(7);
        mLineWidth = mWidth - mPaddingRight - mLineLeftX;
        int lineColor = Color.parseColor("#EEEEEE");
        for (int i = 0, size = mYPos.size(); i < size; i++) {
            final String value = String.valueOf(mYPos.get(i));
            mTextPaint.setColor(mNormalColor);
            int extra = (padding + itemHeight) * i;
            canvas.drawText(value, textRight - getTextWidth(value), mLineTopY - mTextPaint.getFontMetrics().ascent + extra, mTextPaint);
            mTextPaint.setColor(lineColor);
            int stopY = mLineTopY + itemHeight / 2 + extra;
            canvas.drawLine(mLineLeftX, stopY, mWidth - mPaddingRight, stopY, mTextPaint);

            mLineBottomY = stopY;
        }

    }

    private void drawXAxis(Canvas canvas) {
        if (mXPos.isEmpty()) {
            return;
        }
        mTextPaint.setColor(mNormalColor);
        mTextPaint.setTextSize(sp2px(10));

        int padding = mLineWidth / (mXPos.size());

        float y = mLineBottomY + dp2px(5) + getTextHeight();
        for (int i = 0, size = mXPos.size(); i < size; i++) {
            final String value = String.valueOf(mXPos.get(i));
            canvas.drawText(value, mLineLeftX + padding / 2 + padding * i, y, mTextPaint);
        }
    }

    private void drawPoints(Canvas canvas) {
        mTextPaint.setColor(Color.parseColor("#41C2FE"));
        mTextPaint.setStrokeWidth(dp2px(4));
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPoint(200, 200, mTextPaint);
        canvas.drawPoint(100, 220, mTextPaint);
        canvas.drawPoint(150, 320, mTextPaint);
        canvas.drawPoint(350, 300, mTextPaint);
        canvas.drawPoint(550, 300, mTextPaint);
    }

    private float getTextHeight() {
        return mTextPaint.descent() - mTextPaint.ascent();
    }

    private int getTextWidth(String text) {
        return (int) mTextPaint.measureText(text);
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
