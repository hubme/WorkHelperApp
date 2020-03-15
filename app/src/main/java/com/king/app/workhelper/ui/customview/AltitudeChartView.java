package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author VanceKing
 * @since 2018/7/14.
 */
public class AltitudeChartView extends View {
    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;
    private int mWidth;
    private int mHeight;

    private int mChartTitleYPos;
    private int mOverViewYPos;

    //最顶端和最底端的的线
    private int mLineTopY;
    private int mLineBottomY;
    private int mLineLeftX;
    private int mLineWidth;

    private TextPaint mTextPaint;
    private Paint mPaint;
    private int mNormalColor = Color.parseColor("#999999");

    private final String mChartTitle = "海拔曲线";
    private final String mUnitTitle = "单位：米";
    private final String mTotalMeterTitle = "累计爬升：";
    private String mTotalMeters = "59";
    
    private final List<Integer> mXPos = new ArrayList<>();
    private final List<Integer> mYPos = new ArrayList<>();
    private final Path mPathLine = new Path();
    private final Path mPathArea = new Path();

    private List<List<DotVo>> mListDisDots;

    private final List<Point> mListPoints = new ArrayList<>();

    public AltitudeChartView(Context context) {
        this(context, null);
    }

    public AltitudeChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AltitudeChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mYPos.add(0);
        mYPos.add(10);
        mYPos.add(50);
        mYPos.add(100);
        mYPos.add(150);

        for (int i = 0; i < 15; i++) {
            mXPos.add(i + 1);
        }

        Random rand = new Random();
        int mMax = 44;
        mListDisDots = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            ArrayList<DotVo> temp = new ArrayList<>();
            DotVo tempDotVo = new DotVo("08/18", rand.nextInt((int) mMax));
            temp.add(tempDotVo);
            DotVo tempDotVo1 = new DotVo("08/19", rand.nextInt((int) mMax));
            temp.add(tempDotVo1);
            DotVo tempDotVo2 = new DotVo("08/20", rand.nextInt((int) mMax));
            temp.add(tempDotVo2);
            DotVo tempDotVo3 = new DotVo("08/21", rand.nextInt((int) mMax));
            temp.add(tempDotVo3);
            DotVo tempDotVo4 = new DotVo("08/22", rand.nextInt((int) mMax));
            temp.add(tempDotVo4);
            DotVo tempDotVo5 = new DotVo("08/23", rand.nextInt((int) mMax));
            temp.add(tempDotVo5);
            DotVo tempDotVo6 = new DotVo("09/02", rand.nextInt((int) mMax));
            temp.add(tempDotVo6);

            mListDisDots.add(temp);
        }

        mListPoints.add(new Point(150, 200));
        mListPoints.add(new Point(200, 150));
        mListPoints.add(new Point(300, 350));
        mListPoints.add(new Point(400, 300));
        mListPoints.add(new Point(500, 220));
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

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawChartTitle(canvas);
        drawOverViewTitle(canvas);
        drawYAxis(canvas);
        drawXAxis(canvas);
        drawCurve(canvas);
//        drawCurveArea(canvas);
//        drawLine(canvas);
    }

    private void drawChartTitle(Canvas canvas) {
        mTextPaint.setColor(Color.parseColor("#4A4A4A"));
        mTextPaint.setTextSize(sp2px(17));
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        mChartTitleYPos = mPaddingTop + (int) getTextHeight();
        canvas.drawText(mChartTitle, mPaddingLeft, mChartTitleYPos, mTextPaint);
    }

    private void drawOverViewTitle(Canvas canvas) {
        mTextPaint.setColor(mNormalColor);
        mTextPaint.setTextSize(sp2px(12));
        mTextPaint.setTypeface(Typeface.DEFAULT);

        mOverViewYPos = mChartTitleYPos + dp2px(8) + (int) getTextHeight();
        canvas.drawText(mUnitTitle, mPaddingLeft, mOverViewYPos, mTextPaint);

        final String text = mTotalMeterTitle + mTotalMeters;
        canvas.drawText(mTotalMeterTitle, mWidth - mPaddingRight - getTextWidth(text), mOverViewYPos, mTextPaint);

        mTextPaint.setColor(Color.parseColor("#00C6FF"));
        canvas.drawText(mTotalMeters, mWidth - mPaddingRight - getTextWidth(mTotalMeters), mOverViewYPos, mTextPaint);
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

    //画平滑曲线
    private void drawCurve(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#68D4FF"));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(dp2px(3));
        PathEffect pathEffect = new CornerPathEffect(dp2px(6));
        mPaint.setPathEffect(pathEffect);

        mPathLine.moveTo(100, 150);
//        mPathArea.moveTo(100, 150);
        
        for (int i = 0, size = mListPoints.size(); i < size - 1; i++) {
            mPathLine.reset();
            Point start = mListPoints.get(i);
            Point end = mListPoints.get(i + 1);
            int wt = (start.x + end.x) / 2;
            mPathLine.moveTo(start.x, start.y);
            mPathLine.cubicTo(wt, start.y, wt, end.y, end.x, end.y);
            mPathArea.addPath(mPathLine);
            canvas.drawPath(mPathLine, mPaint);
        }

    }

    private void drawCurveArea(Canvas canvas) {
        canvas.save();

        mPathArea.lineTo(mListPoints.get(mListPoints.size()-1).x, 500);
        mPathArea.lineTo(mListPoints.get(0).x, 500);
        mPathArea.lineTo(mListPoints.get(0).x, mListPoints.get(0).y);
//        mPathArea.close();
        canvas.clipPath(mPathArea);

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.RED, Color.BLUE});
        drawable.setBounds(0, 0, mWidth, mHeight);
        
        drawable.draw(canvas);
        canvas.restore();
    }

    private void drawLine(Canvas canvas) {
        mTextPaint.setStyle(Paint.Style.STROKE);

        mPathLine.moveTo(50, 50);
        mPathLine.lineTo(150, 200);
        mPathLine.lineTo(200, 150);
        mPathLine.lineTo(300, 150);
        mPathLine.lineTo(300, 500);
        mPathLine.lineTo(50, 500);
//        mPathLine.close();

        /*mPathLine.moveTo(50, 50);
        mPathLine.cubicTo(100, 120, 300, 300, 50, 50);
        canvas.drawPath(mPathLine, mTextPaint);*/
        
        
        canvas.clipPath(mPathLine);

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.RED, Color.BLUE});
        drawable.setBounds(0, 0, mWidth, mHeight);

        drawable.draw(canvas);
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

    private static class DotVo {
        private String x;

        private double y;

        public DotVo(String x, double y) {
            this.x = x;
            this.y = y;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
