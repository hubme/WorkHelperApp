package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.king.app.workhelper.R;

/**
 * 虚线View
 *
 * @author VanceKing
 * @since 2017/9/20.
 */

public class DashView extends View {
    private Paint mDashPaint;
    private Path mDashPath;

    public DashView(Context context) {
        this(context, null);
    }

    public DashView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        float mDashWidth = 8;
        float mDashHeight = 2;
        float mIntervalWidth = 10;
        float mDashOffset = 0;
        int mDashColor = Color.BLACK;

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.DashView);

            mDashColor = typedArray.getColor(R.styleable.DashView_dashColor, Color.BLACK);
            mDashWidth = typedArray.getDimension(R.styleable.DashView_dashWidth, 8);
            mDashHeight = typedArray.getDimension(R.styleable.DashView_dashHeight, 2.0f);
            mIntervalWidth = typedArray.getDimension(R.styleable.DashView_intervalWidth, 10);
            mDashOffset = typedArray.getDimension(R.styleable.DashView_dashOffset, 0);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        mDashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDashPaint.setColor(mDashColor);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setStrokeWidth(mDashHeight);
        mDashPaint.setPathEffect(new DashPathEffect(new float[]{mDashWidth, mIntervalWidth}, mDashOffset));

        mDashPath = new Path();
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int width = getWidth();
        final int height = getHeight();

        mDashPath.moveTo(0, height / 2);
        mDashPath.lineTo(width, height / 2);
        canvas.drawPath(mDashPath, mDashPaint);//canvas.drawLine()没效果
    }
}
