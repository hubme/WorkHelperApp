package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.king.app.workhelper.R;

/**
 * @author VanceKing
 * @since 2018/7/25.
 */
public class PathView extends View {

    private Paint mPaint;
    private Path mPath;
    private int width;
    private int height;
    private int halfWidth;
    private int halfHeight;

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.chocolate));

        setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint);

        mPath = new Path();
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        halfWidth = w / 2;
        halfHeight = h / 2;
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAssistLine(canvas);

        addPathTest1(canvas);
    }

    private void addPathTest1(Canvas canvas) {
        mPath.lineTo(halfWidth, halfHeight);
        RectF rectF = new RectF(halfWidth + 50, 50, width * 0.75f, halfHeight / 4);
        mPath.addRect(rectF, Path.Direction.CCW);
        canvas.drawPath(mPath, mPaint);
    }

    private void rQuadToTest2(Canvas canvas) {
        mPath.moveTo(0, halfHeight);
        mPath.rQuadTo(width / 4, -halfHeight, halfWidth, 0);
        mPath.rQuadTo(width / 4, halfHeight, halfWidth, 0);

        canvas.drawPath(mPath, mPaint);
    }

    private void rQuadToTest1(Canvas canvas) {
        mPath.moveTo(halfWidth, height / 4);
        mPath.rQuadTo(-halfWidth, height / 4, 0, halfHeight);
        canvas.drawPath(mPath, mPaint);
    }

    private void test3(Canvas canvas) {
        mPath.quadTo(0, halfHeight, halfWidth, halfHeight);
        mPath.quadTo(halfWidth, 0, 0, 0);

        mPath.moveTo(halfWidth, halfHeight);

        mPath.quadTo(halfWidth, 0, width, 0);
        mPath.quadTo(width, halfHeight, halfWidth, halfHeight);

        mPath.moveTo(halfWidth, halfHeight);

        mPath.quadTo(0, halfHeight, 0, height);
        mPath.quadTo(halfWidth, height, halfWidth, halfHeight);

        mPath.moveTo(halfWidth, halfHeight);

        mPath.quadTo(width, halfHeight, width, height);
        mPath.quadTo(halfWidth, height, halfWidth, halfHeight);

        canvas.drawPath(mPath, mPaint);
    }

    private void test1(Canvas canvas) {
        //(x1, y1) 为控制点，(x2, y2) 为结束点
        mPath.quadTo(0, halfHeight, halfWidth, halfHeight);
        canvas.drawPath(mPath, mPaint);
    }

    private void test2(Canvas canvas) {
        mPath.quadTo(0, halfHeight, halfWidth, halfHeight);
        mPath.quadTo(halfWidth, 0, 0, 0);

        canvas.drawPath(mPath, mPaint);
    }

    private void drawAssistLine(Canvas canvas) {
        mPaint.setStrokeWidth(1);
        canvas.drawLine(0, height / 2, width, height / 2, mPaint);
        canvas.drawLine(width / 2, 0, width / 2, height, mPaint);
        mPaint.setStrokeWidth(5);
    }
}
