package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
        mPaint.setStrokeWidth(5);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.chocolate));
        mPath = new Path(); 
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        mPath.moveTo(50, 50);
        mPath.lineTo(300, 400);
        mPath.rLineTo(100, 50);
        mPath.lineTo(600, 500);
        mPath.close();

        canvas.clipPath(mPath);
//        canvas.drawPath(mPath, mPaint);
        canvas.drawColor(Color.BLUE);
    }
}
