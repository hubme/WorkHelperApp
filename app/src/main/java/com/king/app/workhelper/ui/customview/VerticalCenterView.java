package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.king.app.workhelper.R;

/**
 * @author VanceKing
 * @since 2017/2/28.
 * see also:http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2013/0304/957.html
 * http://blog.csdn.net/harvic880925/article/details/39080931
 */

public class VerticalCenterView extends View {
    private static final String TEXT = "哈哈哈fFjJ";

    private Paint mPaint;

    private int mWidth;
    private int mHeight;
    private int mCenterX;
    private int mCenterY;


    public VerticalCenterView(Context context) {
        this(context, null);
    }

    public VerticalCenterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalCenterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(30);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.chocolate));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(mCenterX, 0, mCenterX, mHeight, mPaint);
        canvas.drawLine(0, mCenterY, mWidth, mCenterY, mPaint);

        canvas.save();
        canvas.rotate(-90);//使Canvas坐标系按原点(0, 0)逆时针旋转90°,那么x轴正方向变为y轴正方向，y轴负方向变为x轴正方向.
        canvas.translate(-mHeight, 0);//Canvas向x轴负方向偏移mHeight长度。

        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        //注意此处的x、y坐标
        canvas.drawText(TEXT, mCenterY - mPaint.measureText(TEXT) / 2,
                mCenterX + Math.abs(metrics.ascent) / 2, mPaint);

        canvas.restore();//注意还原Canvas坐标系。
    }
}
