package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;

import static android.view.View.MeasureSpec.getMode;
import static android.view.View.MeasureSpec.getSize;

/**
 * Created by HuoGuangxu on 2016/10/12.
 */

public class StepView extends View {
    private final String DEFAULT_TEXT = "哈哈哈";
    private final int DEFAULT_WIDTH = 500;
    private final int DEFAULT_HEIGHT = 500;
    private Paint mPaint;
    private Paint mTextPaint;
    private Rect mTextRect;
    /** 圆的半径 */
    private float mRadius;
    /** 圆的颜色 */
    @ColorInt
    private int mCircleColor;
    /** 圆中的字体颜色 */
    private int mInnerTextColor;
    /** 圆内的文字 */
    private String mInnerText;
    /** 分几个步骤 */
    private int mSteps = 1;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initPaint();
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StepView);
        mCircleColor = typedArray.getColor(R.styleable.StepView_step_circle_color, Color.RED);
        mRadius = typedArray.getFloat(R.styleable.StepView_step_circle_radius, 40);
        mInnerTextColor = typedArray.getColor(R.styleable.StepView_inner_text_color, Color.WHITE);
        mInnerText = typedArray.getString(R.styleable.StepView_inner_text);
        mSteps = typedArray.getInt(R.styleable.StepView_step_steps, 1);
        if (mInnerText == null) {
            Logger.i("mInnerText == null");
            mInnerText = "";
        }

        typedArray.recycle();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(50);
    }

    private void init() {
        mRadius = ExtendUtil.dp2px(40);
        mTextRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Logger.i("onMeasure()");

        int widthMode = getMode(widthMeasureSpec);
        int widthSize = getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, DEFAULT_HEIGHT);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Logger.i("onDraw()");

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
//        int radius = Math.min(width, height);

        final int y = paddingTop + height / 2;
        final int des = (width - 2 * (int) mRadius) / (mSteps - 1);
        for (int i = 0; i < mSteps; i++) {
            canvas.drawCircle(paddingLeft + mRadius + i * des, y, mRadius, mPaint);
        }

//        mTextPaint.getTextBounds(mInnerText, 0, mInnerText.length(), mTextRect);
//        canvas.drawText(mInnerText, paddingLeft + width / 2 - mTextRect.width() / 2, paddingTop + height / 2 + mTextRect.height() / 2, mTextPaint);
    }

    private void drawText(Canvas canvas) {

    }

    private void setCircleRadius(@ColorInt int color) {
        mCircleColor = color;
    }

    private void setSteps(int steps) {
        if (steps > 0) {
            mSteps = steps;
        }
    }
}
