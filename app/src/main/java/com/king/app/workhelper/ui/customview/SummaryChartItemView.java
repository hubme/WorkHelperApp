package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.king.app.workhelper.R;

/**
 * @author VanceKing
 * @since 2018/7/24.
 */
public class SummaryChartItemView extends View {

    private int height;
    private int width;
    private TextPaint textPaint;
    private String title;
    private int titleColor;
    private int titleSize;
    private int lineColor;
    private int lineWidth;
    private int lineMargin;
    private int rectWidth;
    private int rectCorner;
    private int rectTopColor;
    private int rectBottomColor;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private float titleHeight;
    private float rectHeightPercent;

    public SummaryChartItemView(Context context) {
        this(context, null);
    }

    public SummaryChartItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SummaryChartItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.SummaryChartItemView);
            title = typedArray.getString(R.styleable.SummaryChartItemView_scvTitle);
            titleColor = typedArray.getColor(R.styleable.SummaryChartItemView_scvTitleColor, Color.BLACK);
            titleSize = typedArray.getDimensionPixelSize(R.styleable.SummaryChartItemView_scvTitleSize, sp2px(14));
            lineColor = typedArray.getColor(R.styleable.SummaryChartItemView_scvLineColor, Color.GRAY);
            lineWidth = typedArray.getDimensionPixelSize(R.styleable.SummaryChartItemView_scvLineWidth, 1);
            lineMargin = typedArray.getDimensionPixelSize(R.styleable.SummaryChartItemView_scvLineMargin, dp2px(5));
            rectWidth = typedArray.getDimensionPixelSize(R.styleable.SummaryChartItemView_scvRectWidth, dp2px(5));
            rectHeightPercent = typedArray.getFraction(R.styleable.SummaryChartItemView_scvRectHeightPercent, 1, 1, 0);
            rectCorner = typedArray.getDimensionPixelSize(R.styleable.SummaryChartItemView_scvRectCorner, dp2px(10));
            rectTopColor = typedArray.getColor(R.styleable.SummaryChartItemView_scvRectTopColor, Color.parseColor("#3EC2FF"));
            rectBottomColor = typedArray.getColor(R.styleable.SummaryChartItemView_scvRectBottomColor, Color.parseColor("#3890EF"));

        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        init();

    }


    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();
    }

    private void init() {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTitle(canvas);
        drawLine(canvas);
        drawGradualRect(canvas);
    }

    private void drawTitle(Canvas canvas) {
        if (title == null) {
            return;
        }
        textPaint.setColor(titleColor);
        textPaint.setTextSize(titleSize);

        final Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float titleWidth = textPaint.measureText(title);
        titleHeight = fontMetrics.descent - fontMetrics.ascent;

        canvas.drawText(title, (width - titleWidth) / 2, paddingTop - fontMetrics.ascent, textPaint);
    }

    private void drawLine(Canvas canvas) {
        textPaint.setColor(lineColor);
        textPaint.setStrokeWidth(lineWidth);

        canvas.drawLine(width / 2, titleHeight + paddingTop + lineMargin, width / 2, height - paddingBottom, textPaint);
    }

    public SummaryChartItemView setTitle(String title) {
        this.title = title;
        return this;
    }

    public SummaryChartItemView setTitleColor(@ColorInt int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public SummaryChartItemView setTitleSize(int titleSize) {
        this.titleSize = titleSize;
        return this;
    }

    public SummaryChartItemView setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public SummaryChartItemView setLineColor(int lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    public SummaryChartItemView setLineMargin(int lineMargin) {
        this.lineMargin = lineMargin;
        return this;
    }

    public SummaryChartItemView setRectWidth(int rectWidth) {
        this.rectWidth = rectWidth;
        return this;
    }

    public SummaryChartItemView setRectHeightPercent(@FloatRange(from = 0, to = 1) float heightPercent) {
        this.rectHeightPercent = heightPercent;
        return this;
    }

    public SummaryChartItemView setRectCorner(int rectCorner) {
        this.rectCorner = rectCorner;
        return this;
    }

    public SummaryChartItemView setRectTopColor(@ColorInt int topColor) {
        this.rectTopColor = topColor;
        return this;
    }

    public SummaryChartItemView setRectBottomColor(@ColorInt int bottomColor) {
        this.rectBottomColor = bottomColor;
        return this;
    }

    private void update() {
        invalidate();
    }

    private void drawGradualRect(Canvas canvas) {
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{rectTopColor, rectBottomColor});
        drawable.setCornerRadii(new float[]{rectCorner, rectCorner, rectCorner, rectCorner, 0, 0, 0, 0});
        float maxHeight = height - (paddingTop + titleHeight + lineMargin) - paddingBottom;
        drawable.setBounds((width - rectWidth) / 2, height - (int) (rectHeightPercent * maxHeight) - paddingBottom, (width + rectWidth) / 2, height - paddingBottom);
        drawable.draw(canvas);
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
