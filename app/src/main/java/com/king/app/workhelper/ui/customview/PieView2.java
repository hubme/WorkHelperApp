package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
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
    private static final int FULL_ANGLE = 360;
    private static final int START_ANGLE = -90;
    private static final String TAG = "aaa";
    private int mArcWidth = dip2px(35);
    private int mCenterCircleRadius = dip2px(60);

    public static final int ASC = 0;
    public static final int DESC = 1;
    private float mTextHeight;

    @IntDef({ASC, DESC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SORT_TYPE {

    }

    private final Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mPieRectF = new RectF();
    private final Path mPath = new Path();

    private List<PieItem> mPies = new ArrayList<>();
    private double mTotalValue;
    private static String TEXT = "哈哈哈\n呵呵呵";

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
        mCirclePaint.setColor(Color.parseColor("#D2691E"));
        mCirclePaint.setStrokeWidth(2);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mTextPaint.setColor(Color.parseColor("#2c2c2c"));
        mTextPaint.setStrokeWidth(dip2px(2));
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextSize(sp2px(16));

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.descent - fontMetrics.ascent;
    }
    
    
    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(dip2px(300), MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        final int centerX = width / 2;
        final int centerY = height / 2;

        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();

        final int actualWidth = width - paddingLeft - paddingRight;
        final int actualHeight = height - paddingTop - paddingBottom;

//        final int size = Math.min(actualWidth, actualHeight);

        final int length = mArcWidth + mCenterCircleRadius;
        mPieRectF.set(centerX - length, centerY - length, centerX + length, centerY + length);

        //画每个扇形
        int startAngle = START_ANGLE;
        for (int i = 0, size = mPies.size(); i < size; i++) {
            final PieItem item = mPies.get(i);
            mCirclePaint.setColor(item.color);
            float angle = (float) (item.value / mTotalValue) * FULL_ANGLE;
            //+1是为了不出现空隙，-1是为了不越界
            canvas.drawArc(mPieRectF, startAngle, i == size - 1 ? angle : angle + 1, true, mCirclePaint);
            startAngle += angle;
        }

        //
        float beforeValue = 0;
        for (int i = 0, size = mPies.size(); i < size; i++) {
            final PieItem item = mPies.get(i);
            float percent = (float) ((beforeValue + item.value / 2) / mTotalValue);
            int pointX = getPointX(centerX, mCenterCircleRadius + mArcWidth, percent);
            int pointY = getPointY(centerY, mCenterCircleRadius + mArcWidth, percent);
            canvas.drawCircle(pointX, pointY, 5, mTextPaint);

            beforeValue += item.value;
        }



        //画中间的圆形
        mCirclePaint.setColor(Color.WHITE);
        canvas.drawCircle(centerX, centerY, mCenterCircleRadius, mCirclePaint);

        //画中间的文字
        drawHorizontalCenterText(canvas, centerX, centerY, "哈哈哈", mTextPaint);

        canvas.drawLine(0, height/2, width, height/2, mTextPaint);
        canvas.drawLine(width/2, 0, width/2, height, mTextPaint);

        mPath.lineTo(0, 0);
        mPath.lineTo(200, 400);
        mPath.lineTo(400, 600);
        mTextPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, mTextPaint);

        mPath.rMoveTo(200, 200);
        mPath.lineTo(220, 220);
        canvas.drawPath(mPath, mTextPaint);
    }

    public void drawPies(List<PieItem> pies, @SORT_TYPE int sortType) {
        if (pies == null || pies.isEmpty()) {
            return;
        }
        if (!mPies.isEmpty()) {
            mPies.clear();
        }
        mPies.addAll(pies);

        /*if (mCenterText != null && !mCenterText.trim().isEmpty()) {
            float width = mTextPaint.measureText(mCenterText);
            mTextLayout = new StaticLayout(mCenterText, mTextPaint, (int) width, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
        }*/

        sortPies(sortType);
        mTotalValue = getTotalValue();
        invalidate();
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

    private int getPointX(int centerX, int radius, double percent) {
        return centerX + (int) (radius * Math.sin(percent * 2 * Math.PI));
    }

    private int getPointY(int centerY, int radius, double percent) {
        return centerY - (int) (radius * Math.cos(percent * 2 * Math.PI));
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static class PieItem {
        public double value;
        @ColorInt
        public int color;
        public int marginLeft = 50;//拐点左边距
        public int marginBottom = 50;//拐点底部边距
        private String primaryText;
        private String secondText;

        public PieItem(String primaryText, String secondText, double value, int color) {
            this.primaryText = primaryText;
            this.secondText = secondText;
            this.value = value;
            this.color = color;
        }

    }
}
