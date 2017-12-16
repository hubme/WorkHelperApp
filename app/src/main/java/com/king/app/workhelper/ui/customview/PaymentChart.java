package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.king.applib.util.ScreenUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class PaymentChart extends View {
    private static final String TAG = "aaa";
    private static final String MONTH = "月";

    private final int mXTextColor = Color.parseColor("#bababa");//x轴文字颜色
    private final int mPrimaryTextColor = Color.parseColor("#222222");//y轴文字颜色
    private final int mYSubTextColor = Color.parseColor("#1dccd0");//y轴次文字颜色
    private final int mRoundBgColor = Color.parseColor("#f5f5f5");//年份背景色

    //x 轴的刻度
    private final int mXInterval = dp2px(24);
    //x 轴上的文字距离x 轴的距离
    private final int mXValueMargin = dp2px(10);
    //x 轴圆半径
    private final float mXCircleRadius = dp2px(2);
    //y 轴外圆半径
    private final float mYOuterCircleRadius = dp2px(5);
    //y 轴内圆半径
    private final float mYInnerCircleRadius = dp2px(2);

    //右上角文字总高度
    private final int colorDescHeight = dp2px(50);
    //柱子最高点到右上角文字的距离
    private final int topMargin = dp2px(20);
    //柱子顶部距离上面文字的高度
    private final int mYValueTitleMargin = dp2px(30);
    //柱子上面文字之间的距离
    private final int mTextMargin = dp2px(5);
    private final int mRoundRadius = dp2px(5);

    private static final float chartWeight = 0.45f;//中间矩形框占据整个View的比例

    private Shader blueShader;
    private Shader yellowShader;
    private Shader mMultiShader;

    private Paint mGradientPaint;
    private Paint mTextPaint;
    //每个柱子的位置。onDraw()时会更新
    private Rect mItemRect;
    private Rect mTextColorRect;

    private int mOffsetX;
    private int mOffsetY;

    private ItemType mNormalItem;
    private ItemType mOffItem;
    private ItemType mMultiItem;

    private PaymentModel mMaxModel;

    //每个柱子所对应的Model
    private final List<PaymentModel> mDataList = new ArrayList<>();
    private RectF mRoundRect;
    private List<PaymentYearModel> mPaymentYearModels;


    public PaymentChart(Context context) {
        this(context, null);
    }

    public PaymentChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaymentChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mNormalItem = new ItemType(0, "正常缴纳", Color.parseColor("#3b7aff"), Color.parseColor("#b8d6ff"));
        mOffItem = new ItemType(1, "断缴", Color.parseColor("#fe912b"), Color.parseColor("#ffe4cb"));
        mMultiItem = new ItemType(2, "多次缴纳", Color.parseColor("#0dd06d"), Color.parseColor("#ccffe5"));

        mGradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGradientPaint.setColor(mXTextColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(sp2px(12));

        mItemRect = new Rect();
        mTextColorRect = new Rect();
        mRoundRect = new RectF();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(Math.max(mXInterval * mDataList.size() + getPaddingLeft() + getPaddingRight(), ScreenUtil.getScreenWidth(getContext())), 
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDataEmpty(mDataList)) {
            return;
        }

        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        final int actualWidth = width - paddingLeft - paddingRight;
        final int actualHeight = height - paddingTop - paddingBottom;

        final int pillarHeight = (int) (actualHeight * chartWeight);

        float pillarRatio = 0;
        if (mMaxModel.getPay() >= 1) {
            pillarRatio = (float) pillarHeight / mMaxModel.getPay();
        }
        Log.i(TAG, "pillarHeight: " + pillarHeight + ";pillarRatio: " + pillarRatio);

        final int topPosition = colorDescHeight + topMargin;

        //右上角颜色说明
//        drawColorDesc(canvas, width, paddingRight);

        final int size = mDataList.size();
        for (int i = 0; i < size; i++) {
            final PaymentModel model = mDataList.get(i);
            if (model == null) {
                continue;
            }
            //设置每个柱子的位置
            mItemRect.set(paddingLeft + i * mXInterval, topPosition + pillarHeight - Math.round(pillarRatio * model.getPay()),
                    paddingLeft + i * mXInterval + mXInterval, topPosition + pillarHeight);
            //设置每个柱子的渐变色
            setPaintGradientShader(model, topPosition, pillarHeight, pillarRatio);
            //画每个柱子
            canvas.drawRect(mItemRect.left, mItemRect.top, mItemRect.right, mItemRect.bottom, mGradientPaint);
            //画柱子上的文字
            if (model.isShowYValue() && !model.isOff()) {
                drawTopPillar(canvas, model);
            }

            mTextPaint.setColor(mXTextColor);
            canvas.drawCircle(mItemRect.left, mItemRect.bottom, mXCircleRadius, mTextPaint);//x轴小圆点
            if (i % 2 == 0) {
                drawXTitle(canvas, model, mXInterval);
            }
        }
        //x 轴最后一个圆
        mTextPaint.setColor(mXTextColor);
        canvas.drawCircle(mItemRect.left + mXInterval, mItemRect.bottom, mXCircleRadius, mTextPaint);

        //x 轴的单位文字
        float y = mItemRect.bottom + Math.abs(mTextPaint.getFontMetrics().ascent) + mXValueMargin;
        canvas.drawText(MONTH, width - paddingRight/* - mTextPaint.measureText(MONTH)*/ + getScrollX(), y, mTextPaint);

        mRoundRect.set(paddingLeft, y + dp2px(30), width - paddingRight, y + dp2px(60));
        mTextPaint.setColor(mRoundBgColor);
        canvas.drawRoundRect(mRoundRect, mRoundRadius, mRoundRadius, mTextPaint);

        mTextPaint.setColor(mPrimaryTextColor);
        int yearMonthSize = 0;
        for (PaymentYearModel yearModel : mPaymentYearModels) {
            yearMonthSize += yearModel.getData().size();
            int startX = paddingLeft + yearMonthSize * mXInterval - (yearModel.getData().size() * mXInterval) / 2;
//            canvas.drawLine(startX, 0, startX, height, mTextPaint);
            String year = String.format(Locale.US, "%1s年", yearModel.getYear());
            float yearWidth = mTextPaint.measureText(year);
            canvas.drawText(year, startX - yearWidth / 2, mRoundRect.top + mRoundRect.height() / 2 + Math.abs(mTextPaint.getFontMetrics().ascent) / 2, mTextPaint);
        }

    }

    //柱子上的圆环和上面的文字
    private void drawTopPillar(Canvas canvas, PaymentModel model) {
        mTextPaint.setColor(mYSubTextColor);
        canvas.drawCircle(mItemRect.left, mItemRect.top, mYOuterCircleRadius, mTextPaint);
        mTextPaint.setColor(Color.WHITE);
        canvas.drawCircle(mItemRect.left, mItemRect.top, mYInnerCircleRadius, mTextPaint);

        final Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        final String text = String.valueOf(model.getPay());
        float textWidth = mTextPaint.measureText(text);
        final int padding = dp2px(5);
        mRoundRect.set(mItemRect.left - textWidth / 2 - padding, mItemRect.top - mYValueTitleMargin - padding, mItemRect.left + textWidth / 2 + padding, mItemRect.top - mYValueTitleMargin + textHeight + padding);
        mTextPaint.setColor(mRoundBgColor);
        canvas.drawRoundRect(mRoundRect, mRoundRadius, mRoundRadius, mTextPaint);
        mTextPaint.setColor(mPrimaryTextColor);
        drawHorizontalCenterText(canvas, mTextPaint, mRoundRect.left + mRoundRect.width()/2, mRoundRect.top + mRoundRect.height()/2, text);
        
        final String text2 = String.format(Locale.US, "%s月", model.getMonth());
        float textWidth2 = mTextPaint.measureText(text2);
        mTextPaint.setColor(mYSubTextColor);
        canvas.drawText(text2, mItemRect.left - textWidth2 / 2, mItemRect.top - mYValueTitleMargin - mTextMargin - textHeight, mTextPaint);
    }

    private void drawHorizontalCenterText(Canvas canvas,Paint paint, float centerX, float centerY, String text) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float baseLine = -(fontMetrics.ascent + fontMetrics.descent) / 2;
        float textWidth = paint.measureText(text);
        float startX = centerX - textWidth / 2;
        float endY = centerY + baseLine;
        canvas.drawText(text, startX, endY, paint);
    }
    
    //x 轴的文字
    private void drawXTitle(Canvas canvas, PaymentModel model, int stepWidth) {
        mTextPaint.setColor(mXTextColor);
        final String xValue = String.valueOf(model.getMonth());
        float textWidth = mTextPaint.measureText(xValue);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        canvas.drawText(xValue, mItemRect.left + stepWidth / 2 - textWidth / 2, mItemRect.bottom + Math.abs(fontMetrics.ascent) + mXValueMargin, mTextPaint);
    }

    private void setPaintGradientShader(PaymentModel model, int topPosition, int pillarHeight, float markY) {
        if (model.isOff()) {
            mGradientPaint.setShader(buildOffShader(topPosition + pillarHeight - Math.round(markY * mMaxModel.getPay()), topPosition + pillarHeight));
        } else if (model.isMulti()) {
            mGradientPaint.setShader(buildMultiShader(topPosition + pillarHeight - Math.round(markY * mMaxModel.getPay()), topPosition + pillarHeight));
        } else {
            mGradientPaint.setShader(builtNormalShader(topPosition + pillarHeight - Math.round(markY * mMaxModel.getPay()), topPosition + pillarHeight));
        }
    }

    private Shader builtNormalShader(int startY, int endY) {
        if (blueShader == null && startY > 0 && endY > 0) {
            blueShader = new LinearGradient(0, startY, 0, endY, new int[]{mNormalItem.startColor, mNormalItem.endColor}, null, Shader.TileMode.CLAMP);
        }
        return blueShader;
    }

    private Shader buildOffShader(int startY, int endY) {
        if (yellowShader == null && startY > 0 && endY > 0) {
            yellowShader = new LinearGradient(0, startY, 0, endY, new int[]{mOffItem.startColor, mOffItem.endColor}, null, Shader.TileMode.CLAMP);
        }
        return yellowShader;
    }

    private Shader buildMultiShader(int startY, int endY) {
        if (mMultiShader == null && startY > 0 && endY > 0) {
            mMultiShader = new LinearGradient(0, startY, 0, endY, new int[]{mMultiItem.startColor, mMultiItem.endColor}, null, Shader.TileMode.CLAMP);
        }
        return mMultiShader;
    }


    //右上角颜色说明
    private void drawColorDesc(Canvas canvas, int width, int paddingRight) {

        mTextPaint.setColor(mPrimaryTextColor);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;//文字的高度

        int yOffset = 10;
        float maxTextWidth = mTextPaint.measureText(mNormalItem.desc);
        canvas.drawText(mNormalItem.desc, width - paddingRight - maxTextWidth/* + getScrollX()*/, yOffset - fontMetrics.ascent, mTextPaint);
        canvas.drawText(mOffItem.desc, width - paddingRight - maxTextWidth + getScrollX(), yOffset - fontMetrics.ascent + textHeight, mTextPaint);
        canvas.drawText(mMultiItem.desc, width - paddingRight - maxTextWidth + getScrollX(), yOffset - fontMetrics.ascent + 2 * textHeight, mTextPaint);
    }

    public void setData(List<PaymentYearModel> data) {
        if (isDataEmpty(data)) {
            return;
        }
        mPaymentYearModels = data;
        for (PaymentChart.PaymentYearModel yearModel : data) {
            mDataList.addAll(yearModel.getData());
        }
        mMaxModel = getMaxValue(mDataList);
        Log.i(TAG, "最大值：" + mMaxModel.toString());
        dealYValueData();

        invalidate();
    }

    private boolean isDataEmpty(List data) {
        return data == null || data.isEmpty();
    }

    private PaymentModel getMaxValue(List<PaymentModel> data) {
        return Collections.max(data, new Comparator<PaymentModel>() {
            @Override public int compare(PaymentModel o1, PaymentModel o2) {
                if (o1.getPay() > o2.getPay()) {
                    return 1;
                } else if (o1.getPay() < o2.getPay()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    //处理是否显示柱子上的文字
    private void dealYValueData() {
        Set<Integer> yValues = new HashSet<>();
        for (PaymentModel model : mDataList) {
            if (!yValues.contains(model.getPay())) {
                model.setShowYValue(true);
                yValues.add(model.getPay());
            }
        }
    }

    int mStartX = 0;
    int mStartY = 0;

    /*@Override public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getX() + getScrollX();
                mStartY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mOffsetX = (int) event.getX() - mStartX;
                mOffsetY = (int) event.getY() - mStartY;

                *//*Log.i(TAG, "mScreenWidth: "+mScreenWidth+" ;getScrollX: " + getScrollX() + " ;mWidth: " + mWidth);
                if ((mScreenWidth + getScrollX() < mWidth) || (mWidth - mScreenWidth < getScrollX() && getScrollX() > 0)) {
                    slideWithScrollTo();
                }*//*
                int totalWidth = mXInterval * mDataList.size();

                Log.i(TAG, "getScrollX(): " + getScrollX() + " ;mOffsetX: " + mOffsetX + " ;屏幕宽度：" + ScreenUtil.getScreenWidth(getContext()) + " ;getMeasuredWidth: " + getMeasuredWidth() + " ;totalWidth: " + totalWidth);
                //getScrollX > 0.向左滑,右侧显示；getScrollX < 0
                if (getScrollX() >= 0 && getScrollX() <= totalWidth - ScreenUtil.getScreenWidth(getContext())) {
                    scrollTo(-mOffsetX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }*/

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static class PaymentYearModel {
        private int year;
        private List<PaymentModel> data;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public List<PaymentModel> getData() {
            return data;
        }

        public void setData(List<PaymentModel> data) {
            this.data = data;
        }
    }

    public static class PaymentModel {
        private int month;
        private int pay;
        private int count;
        private boolean isShowYValue;

        public PaymentModel(int month, int pay, int count) {
            this.month = month;
            this.pay = pay;
            this.count = count;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getPay() {
            return pay;
        }

        public void setPay(int pay) {
            this.pay = pay;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isShowYValue() {
            return isShowYValue;
        }

        public void setShowYValue(boolean showYValue) {
            isShowYValue = showYValue;
        }

        public boolean isOff() {
            return pay <= 0;
        }

        public boolean isMulti() {
            return count > 1;
        }

        @Override public String toString() {
            return "PaymentModel{" +
                    "month=" + month +
                    ", pay=" + pay +
                    ", count=" + count +
                    '}';
        }
    }

    private static class ItemType {
        public int id;
        public String desc;
        public int startColor;
        public int endColor;

        public ItemType() {
        }

        public ItemType(int id, String desc, int startColor, int endColor) {
            this.id = id;
            this.desc = desc;
            this.startColor = startColor;
            this.endColor = endColor;
        }

        @Override public String toString() {
            return "ItemType{" +
                    "id=" + id +
                    ", desc='" + desc + '\'' +
                    ", startColor=" + startColor +
                    ", endColor=" + endColor +
                    '}';
        }
    }

    /*private enum ItemType {
        NORMAL(0, "正常缴纳", Color.parseColor("#3b7aff"), Color.parseColor("#b8d6ff")),
        OFF(1, "断缴", Color.parseColor("#fe912b"), Color.parseColor("#ffe4cb")),
        MULTI(2, "断缴", Color.parseColor("#0dd06d"), Color.parseColor("#ccffe5"));

        private int id;
        private String desc;
        @ColorInt private int startColor;
        @ColorInt private int endColor;

        ItemType() {
        }

        ItemType(int id, String desc, int startColor, int endColor) {
            this.id = id;
            this.desc = desc;
            this.startColor = startColor;
            this.endColor = endColor;
        }
    }*/
}
