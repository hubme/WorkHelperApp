package com.king.applib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import com.king.applib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格
 *
 * @author VanceKing
 * @since 2017/2/14.
 */

public class FormView extends View {
    @ColorInt
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private List<FormLine> mFormLines = new ArrayList<>();
    private Paint mPaint;

    private @ColorInt int mBorderColor;
    private int mBorderWidth;
    private float mTextSize;

    public FormView(Context context) {
        this(context, null);
    }

    public FormView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FormView);
        mBorderColor = typedArray.getColor(R.styleable.FormView_border_color, Color.BLACK);
        mBorderWidth = typedArray.getInt(R.styleable.FormView_border_width, 1);
        mTextSize = typedArray.getDimension(R.styleable.FormView_text_size, getResources().getDimension(R.dimen.ts_small));
        typedArray.recycle();

        init();
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

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        //每行的高度
        final int lineItemHeight = availableHeight / mLineCount;
        //每列的宽度
        final int columnItemWidth = availableWidth / mColumnCount;

        for (int i = 0; i < mLineCount; i++) {
            if (mFormLines.get(i) == null || mFormLines.get(i).lineItems == null) {
                continue;
            }

            for (int j = 0; j < mColumnCount; j++) {
                FormItem item = mFormLines.get(i).lineItems.get(j);
                if (item == null || item.heightSpan == 0 || item.widthSpan == 0) {
                    continue;
                }

                //画外层矩形
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                mPaint.setStrokeWidth(mBorderWidth);
                mPaint.setColor(mBorderColor);
                canvas.drawRect(paddingLeft + j * columnItemWidth, paddingTop + i * lineItemHeight, paddingLeft + (j + item.widthSpan) * columnItemWidth,
                        paddingTop + (i + item.heightSpan) * lineItemHeight, mPaint);

                //画内层矩形
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                mPaint.setStrokeWidth(0);
                mPaint.setColor(item.backgroundColor);
                canvas.drawRect(paddingLeft + mBorderWidth + j * columnItemWidth, paddingTop + mBorderWidth + i * lineItemHeight,
                        paddingLeft - mBorderWidth + (j + item.widthSpan) * columnItemWidth,
                        paddingTop + (i + item.heightSpan) * lineItemHeight - mBorderWidth, mPaint);

                //画文字
                if (item.text != null && !item.text.trim().isEmpty()) {
                    mPaint.setColor(DEFAULT_TEXT_COLOR);
                    mPaint.setTextSize(mTextSize);
                    drawHorizontalCenterText(canvas, paddingLeft + (item.widthSpan * columnItemWidth) / 2 + j * columnItemWidth,
                            paddingTop + (item.heightSpan * lineItemHeight) / 2 + i * lineItemHeight, item.text, mPaint);
                }

            }
        }
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
    }

    public void setData(List<FormLine> formLines) {
        if (formLines != null && !formLines.isEmpty()) {
            mFormLines.addAll(formLines);
            postInvalidate();
        }
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
        float baseLine = -(fontMetrics.ascent + fontMetrics.descent) / 2;
        float textWidth = paint.measureText(text);
        float startX = centerX - textWidth / 2;
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

        public FormItem(String text, @ColorInt int backgroundColor) {
            this.text = text;
            this.backgroundColor = backgroundColor;
        }

        public FormItem(String text, float widthSpan, float heightSpan) {
            this.text = text;
            this.widthSpan = widthSpan;
            this.heightSpan = heightSpan;
        }

        public FormItem(String text, float widthSpan, float heightSpan, int backgroundColor) {
            this.text = text;
            this.widthSpan = widthSpan;
            this.heightSpan = heightSpan;
            this.backgroundColor = backgroundColor;
        }
    }
}
