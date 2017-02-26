package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.applib.log.Logger;

/**
 * @author VanceKing
 * @since 2017/2/26 0026.
 */

public class TestView extends View {
    private static final String TEXT = "哈哈哈\r\n呵呵呵呵";
    private static final int RADIUS = 200;
    private Paint mPaint;
    private StaticLayout mStaticLayout;
    private TextPaint mTextPaint;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.pink));

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(dip2px(getContext(), 18));
        mStaticLayout = new StaticLayout(TEXT, mTextPaint, (int) mTextPaint.measureText(TEXT), Layout.Alignment.ALIGN_CENTER, 1, 0, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int width = getWidth();
        final int height = getHeight();
        final int centerX = width / 2;
        final int centerY = height / 2;

        canvas.drawCircle(centerX, centerY, RADIUS, mPaint);

        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.chocolate));
        canvas.drawLine(centerX - RADIUS, centerY, centerX + RADIUS, centerY, mPaint);
        canvas.drawLine(centerX, centerY - RADIUS, centerX, centerY + RADIUS, mPaint);

        canvas.save();
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        Logger.i("ascent: " + fontMetrics.ascent + ";descent: " + fontMetrics.descent +
                ";bottom: " + fontMetrics.bottom + ";leading: " + fontMetrics.leading +
                ";top: " + fontMetrics.top);

        float textHeight = (fontMetrics.bottom - fontMetrics.top) * mStaticLayout.getLineCount();
        canvas.translate(centerX - mTextPaint.measureText(TEXT) / 2, centerY - textHeight / 2);
        mStaticLayout.draw(canvas);
        canvas.restore();
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
