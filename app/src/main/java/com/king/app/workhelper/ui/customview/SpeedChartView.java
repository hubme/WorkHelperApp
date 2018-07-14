package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VanceKing
 * @since 2018/7/13.
 */
public class SpeedChartView extends View {

    private final int mTitleColor = Color.parseColor("#4A4A4A");
    private int mNormalTextColor = Color.parseColor("#999999");

    private final String mTitle = "速配";
    private final int mTitleSize = 17;//sp

    private final String mSubTitle = "速配:分钟/公里";

    private final String averageTitle = "平均:  ";
    private final String fastestTitle = "最快:  ";
    private final String slowestTitle = "最慢:  ";
    private final String fastestText = "最快";

    private final String mKilometreColumn = "公里";
    private final String mMinkmColumn = "速配";
    private final String mDurationColumn = "用时";

    private String mAverageTime = "20'48\"";
    private String mMaxFast = "07'18\"";
    private String mMaxSlow = "31'133\"";
    private List<SpeedItemData> mSpeedItems;

    private int mWidth;
    private int mHeight;
    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;

    private TextPaint mTextPaint;


    //配速底部y坐标
    private float mTitleYPos;
    //概览文字底部y坐标
    private float mOverviewTextYPos;
    //列标题文字底部y坐标
    private float mColumnTitleYPos;

    private final Rect mRect = new Rect();

    private final int itemWidth = 400;

    public SpeedChartView(Context context) {
        this(context, null);
    }

    public SpeedChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        mSpeedItems = new ArrayList<>(8);
        SpeedItemData item1 = new SpeedItemData(1, "10'33\"", "00:30:33");
        item1.setFastest(true);
        mSpeedItems.add(item1);
        mSpeedItems.add(new SpeedItemData(2, "20'32\"", "00:32:39"));
        mSpeedItems.add(new SpeedItemData(3, "30'32\"", "00:32:39"));
        mSpeedItems.add(new SpeedItemData(3, "40'32\"", "00:32:39"));
        mSpeedItems.add(new SpeedItemData(3, "50'32\"", "00:32:39"));
        mSpeedItems.add(new SpeedItemData(6, "59'32\"", "00:32:39"));
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /*int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);*/

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

        drawSpeedTitle(canvas);
        drawTop(canvas);
        drawColumnTitle(canvas);
        drawColumnData(canvas);
    }

    //速配 速配:分钟/公里
    private void drawSpeedTitle(Canvas canvas) {
        //速配
        mTextPaint.setColor(mTitleColor);
        mTextPaint.setTextSize(sp2px(mTitleSize));
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textWidth = mTextPaint.measureText(mTitle);
        mTitleYPos = mPaddingTop - fontMetrics.ascent;
        canvas.drawText(mTitle, mPaddingLeft, mTitleYPos, mTextPaint);

        //速配:分钟/公里
        mTextPaint.setColor(mNormalTextColor);
        mTextPaint.setTextSize(sp2px(12));
        mTextPaint.setTypeface(Typeface.DEFAULT);
        canvas.drawText(mSubTitle, mPaddingLeft + textWidth + dp2px(5), mPaddingTop - fontMetrics.ascent, mTextPaint);
    }

    //平均 最快 最慢
    private void drawTop(Canvas canvas) {
        mTextPaint.setTextSize(sp2px(12));
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setColor(mNormalTextColor);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        mOverviewTextYPos = mTitleYPos + dp2px(4) + textHeight;
        float offset = mPaddingLeft;

        //"平均"
        canvas.drawText(averageTitle, offset, mOverviewTextYPos, mTextPaint);
        offset += mTextPaint.measureText(averageTitle);

        //20'48"
        mTextPaint.setColor(Color.parseColor("#30C4FF"));
        canvas.drawText(mAverageTime, offset, mOverviewTextYPos, mTextPaint);
        if (TextUtils.isEmpty(mAverageTime)) {
            offset += mTextPaint.measureText("     ");
        } else {
            offset += mTextPaint.measureText(mAverageTime);
        }

        //最快
        offset += dp2px(10);
        mTextPaint.setColor(mNormalTextColor);
        canvas.drawText(fastestTitle, offset, mOverviewTextYPos, mTextPaint);
        offset += mTextPaint.measureText(fastestTitle);

        //07'18"
        mTextPaint.setColor(Color.parseColor("#30C4FF"));
        canvas.drawText(mMaxFast, offset, mOverviewTextYPos, mTextPaint);
        if (TextUtils.isEmpty(mAverageTime)) {
            offset += mTextPaint.measureText("     ");
        } else {
            offset += mTextPaint.measureText(mMaxFast);
        }

        //最慢
        offset += dp2px(10);
        mTextPaint.setColor(mNormalTextColor);
        canvas.drawText(slowestTitle, offset, mOverviewTextYPos, mTextPaint);
        offset += mTextPaint.measureText(slowestTitle);


        //31'133"
        mTextPaint.setColor(Color.parseColor("#FF496E"));
        canvas.drawText(mMaxSlow, offset, mOverviewTextYPos, mTextPaint);

    }

    //画列名。公里 配速 用时
    private void drawColumnTitle(Canvas canvas) {
        mTextPaint.setTextSize(sp2px(11));
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setColor(mNormalTextColor);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        mColumnTitleYPos = mOverviewTextYPos + dp2px(14) + textHeight;
        canvas.drawText(mKilometreColumn, mPaddingLeft, mColumnTitleYPos, mTextPaint);
        float column2X = mPaddingLeft + mTextPaint.measureText(mKilometreColumn) + dp2px(70);
        canvas.drawText(mMinkmColumn, column2X, mColumnTitleYPos, mTextPaint);

        canvas.drawText(mDurationColumn, mWidth - mPaddingRight - mTextPaint.measureText(mDurationColumn), mColumnTitleYPos, mTextPaint);
    }

    private void drawColumnData(Canvas canvas) {
        if (mSpeedItems == null || mSpeedItems.isEmpty()) {
            return;
        }
        final int padding = dp2px(10);
        final int itemDataHeight = dp2px(20);

        final int corner = dp2px(20);
        final int startColor = Color.parseColor("#883ec2ff");
        final int endColor = Color.parseColor("#cc3890ef");
        for (int i = 0, size = mSpeedItems.size(); i < size; i++) {
            final SpeedItemData itemData = mSpeedItems.get(i);
            
            //确定坐标，方便绘制
            int left = mPaddingLeft + dp2px(25);
            int top = (int) mColumnTitleYPos + dp2px(16) + (padding + itemDataHeight) * i;
            mRect.set(left, top, left + itemWidth, top + itemDataHeight);
            int baseLine = mRect.top + itemDataHeight / 2;


            //“用时”文字
            if (itemData.isFastest) {
                drawFastestText(canvas, baseLine, mRect.right - dp2px(10), mRect.top, mWidth - mPaddingRight, mRect.bottom, corner);
            } else {
                drawCenterText(canvas, mWidth - mPaddingRight - mTextPaint.measureText(itemData.duration) / 2, baseLine, itemData.duration, mTextPaint);
            }

            drawSpeedColumn(canvas, itemData, baseLine, startColor, endColor, corner);


            //“公里数”文字
            mTextPaint.setColor(mNormalTextColor);
            drawCenterText(canvas, mPaddingLeft + dp2px(10), baseLine, String.valueOf(itemData.kilometre), mTextPaint);

        }
    }

    //画配速渐变矩形和文字
    private void drawSpeedColumn(Canvas canvas, SpeedItemData itemData, int baseLine, int startColor, int endColor, int corner) {
        //渐变圆角矩形
        final GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{startColor, endColor});
        drawable.setCornerRadii(new float[]{0, 0, corner, corner, corner, corner, 0, 0});
        drawable.setBounds(mRect.left, mRect.top, mRect.right, mRect.bottom);
        drawable.draw(canvas);

        //渐变矩形上的文字
        mTextPaint.setTextSize(sp2px(10));
        mTextPaint.setColor(Color.WHITE);
        final String text = String.valueOf(itemData.speed);
        float textWidth = mTextPaint.measureText(text);
        drawCenterText(canvas, mRect.right - dp2px(18) - textWidth / 2, baseLine, text, mTextPaint);

    }

    //用时最快的渐变矩形
    private void drawFastestText(Canvas canvas, int baseLine, int left, int top, int right, int bottom, int corner) {
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.parseColor("#99e0efff"), Color.parseColor("#ffd3f1ff")});
        drawable.setCornerRadii(new float[]{0, 0, corner, corner, corner, corner, 0, 0});
        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
        //“最快”文字

        mTextPaint.setTextSize(sp2px(11));
        mTextPaint.setColor(Color.parseColor("#4cc3fe"));
        drawCenterText(canvas, mWidth - mPaddingRight - mTextPaint.measureText(fastestText) / 2 - dp2px(7), baseLine, fastestText, mTextPaint);
    }

    public void setAverageTime(String time) {
        mAverageTime = time;
    }

    public static class SpeedItemData {
        private int kilometre;//1
        private String speed;//31'33"
        private String duration;//00:30:33
        private boolean isFastest;

        public SpeedItemData() {
        }

        public SpeedItemData(int kilometre, String speed, String duration) {
            this.kilometre = kilometre;
            this.speed = speed;
            this.duration = duration;
        }

        public boolean isFastest() {
            return isFastest;
        }

        public void setFastest(boolean fastest) {
            isFastest = fastest;
        }
    }

    private void drawCenterText(Canvas canvas, float centerX, float centerY, String text, Paint paint) {
        final Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float baseLine = -(fontMetrics.ascent + fontMetrics.descent) / 2;
        float textWidth = paint.measureText(text);
        float startX = centerX - textWidth / 2;
        float endY = centerY + baseLine;
        canvas.drawText(text, startX, endY, paint);
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
