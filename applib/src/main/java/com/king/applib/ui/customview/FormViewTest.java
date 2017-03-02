package com.king.applib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import com.king.applib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格
 *
 * @author huoguangxu
 * @since 2017/2/14.
 */

public class FormViewTest extends View {
    private List<FormLine> mFormLines = new ArrayList<>();
    private Paint mPaint;

    @ColorInt private int mBorderColor;
    private int mBorderWidth;
    private float mTextSize;
    @ColorInt private int mTextColor;
    private RectF mRectF;
    private float[] mWeights;

    public FormViewTest(Context context) {
        this(context, null);
    }

    public FormViewTest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FormView);
        mBorderColor = typedArray.getColor(R.styleable.FormView_border_color, Color.BLACK);
        mBorderWidth = typedArray.getInt(R.styleable.FormView_border_width, 2);
        mTextSize = typedArray.getDimension(R.styleable.FormView_text_size, getResources().getDimension(R.dimen.ts_small));
        mTextColor = typedArray.getColor(R.styleable.FormView_text_color, Color.BLACK);
        typedArray.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);

        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSize(Integer.MAX_VALUE, widthMeasureSpec), resolveSize(400, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int mLineCount = mFormLines.size();//行数
        int mColumnCount = getMaxColumnLength();//最大列数
        if (mColumnCount == 0) {
            return;
        }

        float totalWeights = 0;
        if (mWeights != null) {
            for (float f : mWeights) {
                totalWeights += f;
            }
        } else {
            totalWeights = mColumnCount;
        }

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom() - mBorderWidth;//减去边框的高度，否则粗细不一致。

        //每行的高度
        final int lineItemHeight = availableHeight / mLineCount;
        //每列的宽度
        final int columnItemWidth = availableWidth / mColumnCount;

        for (int i = 0; i < mLineCount; i++) {
            FormLine line = mFormLines.get(i);
            if (line == null || line.lineItems == null) {
                continue;
            }

            float aaa = 0;
            for (int j = 0; j < mColumnCount; j++) {
                FormItem item = mFormLines.get(i).lineItems.get(j);
                if (item == null || item.heightSpan == 0 || item.widthSpan == 0) {
                    continue;
                }

                //画边框
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(mBorderWidth);
                mPaint.setColor(mBorderColor);
                float up = mWeights[j] / totalWeights * availableWidth;
                mRectF.set(paddingLeft + aaa,
                        paddingTop + i * lineItemHeight + mBorderWidth /2,
                        paddingLeft + up,
                        paddingTop + (i + item.heightSpan) * lineItemHeight + mBorderWidth /2);
                canvas.drawRect(mRectF, mPaint);
                
                //画text背景
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                mPaint.setStrokeWidth(0);
                mPaint.setColor(item.backgroundColor);
                mRectF.set(paddingLeft + mBorderWidth + aaa,
                        paddingTop + mBorderWidth + i * lineItemHeight + mBorderWidth /2,
                        paddingLeft - mBorderWidth + up,
                        paddingTop + (i + item.heightSpan) * lineItemHeight - mBorderWidth + mBorderWidth /2);
                canvas.drawRect(mRectF, mPaint);
                aaa = up;
                //画文字
                if (item.text != null && !item.text.trim().isEmpty()) {
                    mPaint.setColor(mTextColor);
                    mPaint.setTextSize(mTextSize);
                    drawHorizontalCenterText(canvas, mRectF.left + (mRectF.right - mRectF.left)/2, mRectF.top + (mRectF.bottom - mRectF.top)/2, item.text, mPaint);
                }

            }
        }
        
        
    }

    //由于画矩形(drawRect)会出现线条粗细不一致的情况
    private void drawRectF(Canvas canvas, RectF rectF, Paint paint) {
        canvas.drawLine(rectF.left, rectF.top, rectF.right, rectF.top, paint);//上
        canvas.drawLine(rectF.left, rectF.bottom, rectF.right, rectF.bottom, paint);//下
        canvas.drawLine(rectF.left, rectF.top, rectF.left, rectF.bottom, paint);//左
        canvas.drawLine(rectF.right, rectF.top, rectF.right, rectF.bottom, paint);//右
    }


    public void setData(List<FormLine> formLines) {
        if (formLines != null && !formLines.isEmpty()) {
            mFormLines.clear();
            mFormLines.addAll(formLines);
            postInvalidate();
        }
    }

    
    /**
     * {@link #setData(List) setData()}之前使用有效
     * @param weights 每列的权重
     */
    public void setColumnWeights(float... weights) {
        mWeights = weights;
    }

    private int getMaxColumnLength() {
        int result = 0;
        for (FormLine item : mFormLines) {
            if (item == null) {
                continue;
            }
            if (item.lineItems != null && result < item.lineItems.size()) {
                result = item.lineItems.size();
            }
        }
        return result;
    }

    private void drawHorizontalCenterText(Canvas canvas, float centerX, float centerY, String text, Paint paint) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float baseLine = fontMetrics.descent - fontMetrics.ascent / 2;
        float startX = centerX - paint.measureText(text) / 2;
        float endY = centerY + baseLine;
        canvas.drawText(text, startX, endY, paint);

    }

    public static class FormLine {
        public List<FormItem> lineItems = new ArrayList<>();

        public FormLine(List<FormItem> lineItems) {
            this.lineItems = lineItems;
        }
    }

    public static class FormItem {
        public String text;
        public float widthSpan = 1;//width = widthSpan * 最小单元格的宽度
        public float heightSpan = 1;//height = heightSpan * 最小单元格的高度
        @ColorInt
        public int backgroundColor = Color.WHITE;

        public FormItem(String text) {
            this.text = text;
        }
        
        public FormItem(String text, float widthSpan) {
            this.text = text;
            this.widthSpan = widthSpan;
        }

        public FormItem(String text, @ColorInt int backgroundColor) {
            this.text = text;
            this.backgroundColor = backgroundColor;
        }

        public FormItem(String text, float widthSpan, int backgroundColor) {
            this.text = text;
            this.widthSpan = widthSpan;
            this.backgroundColor = backgroundColor;
        }
    }
}
