package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.IntDef;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.applib.util.ExtendUtil;

/**
 * 把文字画在圆形中央的TextView
 * Created by VanceKing on 2016/12/25 0025.
 */

public class CircleCenterTextView extends View {
    private final float DEFAULT_CIRCLE_RADIUS = ExtendUtil.dp2px(68);
    private final int DEFAULT_TEXT_SIZE = ExtendUtil.dp2px(15);
    private final int DEFAULT_TEXT_COLOR = Color.WHITE;
    private final int DEFAULT_CIRCLE_COLOR = 0xFF03A9F4;

    private Paint mPaint;
    private TextPaint mHorizontalTextPaint;
    private StaticLayout mStaticLayout;

    public static final int Horizontal = 1;
    public static final int Vertical = 2;

    @IntDef({Horizontal, Vertical})
    public @interface Orientation {
    }

    private int mOrientation;
    private int mBackgroundColor;
    private float mRadius;
    private String mText = "";
    private int mTextColor;
    private float mTextSize;

    public CircleCenterTextView(Context context) {
        this(context, null);
    }

    public CircleCenterTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleCenterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        applyAttrs(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleCenterTextView);
        mOrientation = typedArray.getInt(R.styleable.CircleCenterTextView_orientation, Horizontal);
        mBackgroundColor = typedArray.getColor(R.styleable.CircleCenterTextView_circleColor, DEFAULT_CIRCLE_COLOR);
        mRadius = typedArray.getDimension(R.styleable.CircleCenterTextView_radius, DEFAULT_CIRCLE_RADIUS);
        mText = typedArray.getString(R.styleable.CircleCenterTextView_text);
        mTextColor = typedArray.getColor(R.styleable.CircleCenterTextView_textColor, DEFAULT_TEXT_COLOR);
        mTextSize = typedArray.getDimension(R.styleable.CircleCenterTextView_textSize, DEFAULT_TEXT_SIZE);

        typedArray.recycle();

        init();
    }

    private void applyAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleCenterTextView);
        final int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CircleCenterTextView_orientation:
                    mOrientation = typedArray.getInt(attr, 1);
                    break;
                case R.styleable.CircleCenterTextView_circleColor:
                    mBackgroundColor = typedArray.getColor(attr, DEFAULT_CIRCLE_COLOR);
                    break;
                case R.styleable.CircleCenterTextView_radius:
                    mRadius = typedArray.getDimension(attr, DEFAULT_CIRCLE_RADIUS);
                    break;
                case R.styleable.CircleCenterTextView_text:
                    mText = typedArray.getString(attr);
                    break;
                case R.styleable.CircleCenterTextView_textColor:
                    mTextColor = typedArray.getColor(attr, DEFAULT_TEXT_COLOR);
                    break;
                case R.styleable.CircleCenterTextView_textSize:
                    mTextSize = typedArray.getDimension(attr, DEFAULT_TEXT_SIZE);
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mHorizontalTextPaint = new TextPaint();
        mHorizontalTextPaint.setAntiAlias(true);
        mHorizontalTextPaint.setDither(true);
        mHorizontalTextPaint.setColor(mTextColor);
        mHorizontalTextPaint.setTextSize(mTextSize);
        //字体宽度超过1像素时自动换行
        mStaticLayout = new StaticLayout(mText, mHorizontalTextPaint, 1, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(resolveSizeAndState((int) mRadius, widthMeasureSpec, 0),
                resolveSizeAndState((int) mRadius, heightMeasureSpec, 0));
//        setMeasuredDimension(measureSpec(widthMeasureSpec), measureSpec(heightMeasureSpec));
    }

    //see View#resolveSizeAndState
    private int measureSpec(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min((int) DEFAULT_CIRCLE_RADIUS, specSize);
        } else {
            result = (int) DEFAULT_CIRCLE_RADIUS;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        float centerX = paddingLeft + width / 2;
        float centerY = paddingTop + height / 2;

        float radius = Math.min(Math.min(width, height) / 2, mRadius);
        mPaint.setColor(mBackgroundColor);
        canvas.drawCircle(centerX, centerY, radius, mPaint);

        if (mOrientation == Vertical) {
            drawVerticalCenterText(canvas, centerX, centerY);
        } else {
            drawHorizontalCenterText(canvas, centerX, centerY, mText);
        }
    }

    private void drawVerticalCenterText(Canvas canvas, float centerX, float centerY) {
        canvas.translate(centerX, centerY - mStaticLayout.getHeight() / 2);
        mStaticLayout.draw(canvas);
    }

    /**
     * 水平方向
     *
     * @param canvas  画布
     * @param centerX 中心点x坐标
     * @param centerY 中心点y坐标
     * @param text    文字
     */
    private void drawHorizontalCenterText(Canvas canvas, float centerX, float centerY, String text) {
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float baseLine = -(fontMetrics.ascent + fontMetrics.descent) / 2;
        float textWidth = mPaint.measureText(text);
        float startX = centerX - textWidth / 2;
        float endY = centerY + baseLine;
        canvas.drawText(text, startX, endY, mPaint);
    }

    public void setOrientation(@Orientation int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            invalidate();
        }
    }
}
