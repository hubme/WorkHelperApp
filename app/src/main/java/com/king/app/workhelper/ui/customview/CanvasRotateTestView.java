package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.king.applib.base.customview.BaseView;

/**
 * Canvas rotate 操作测试。
 *
 * @author VanceKing
 * @since 2018/2/3.
 */

public class CanvasRotateTestView extends BaseView {
    private static final String TEXT = "哈哈哈";

    public CanvasRotateTestView(Context context) {
        super(context);
    }

    public CanvasRotateTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CanvasRotateTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void initTextPaint(TextPaint textPaint) {
        super.initTextPaint(textPaint);
        textPaint.setTextSize(getResources().getDimensionPixelSize(com.king.applib.R.dimen.ts_huge));
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        testRotate(canvas);

    }

    private void testRotate(Canvas canvas) {
        int rectWidth = 200;
        int rectHeight = 400;
        int x1 = getViewHalfWidth();
        int y1 = 0;
        canvas.drawRect(x1, y1, x1 + rectWidth, y1 + rectHeight, mPaint);

        canvas.translate(x1, y1);
        canvas.rotate(30);
        canvas.drawRect(x1, y1, x1 + rectWidth, y1 + rectHeight, mPaint);

    }

    private void testTranslate1(Canvas canvas) {
        drawCenterText(canvas, TEXT, getWidth() / 2, getHeight() / 2, mTextPaint);//@1

        canvas.save();//保存 canvas 坐标系，以便恢复
        canvas.translate(0, 100);//canvas 坐标系向下移动 100 个像素。此时 canvas 上的内容都会向下偏移 100 个像素。
        drawCenterText(canvas, TEXT, getWidth() / 2, getHeight() / 2, mTextPaint);//和@1对比，内容确实向下偏移 100 个像素。
        drawCenterText(canvas, "abc", getWidth() / 2, 0, mTextPaint);//@2
        canvas.restore();

        drawCenterText(canvas, "def", getWidth() / 2, 0, mTextPaint);//和@2相比，canvas 坐标系恢复到初始状态。
    }

    //canvas 坐标系右、下为正方向；左、上为负方向。
    private void testTranslate(Canvas canvas) {
        drawCenterText(canvas, TEXT, getWidth() / 2, getHeight() / 2, mTextPaint);//@1
        //相对@1左上移动
        drawCenterText(canvas, "aaa", getWidth() / 2 - 100, getHeight() / 2 - 100, mTextPaint);
        //相对@1右下移动
        drawCenterText(canvas, "bbb", getWidth() / 2 + 100, getHeight() / 2 + 100, mTextPaint);

    }
}
