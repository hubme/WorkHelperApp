package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.king.app.workhelper.R;

import static android.graphics.Bitmap.createBitmap;

/**
 * Canvas用法
 * Created by VanceKing on 2016/12/24 0024.
 */

public class CanvasSampleView extends View {
    private final String TEXT = "邢雁啡";

    private Drawable mDrawable;
    private Paint mPaint;
    private Path mPath;
    private Rect mTextRect;

    public CanvasSampleView(Context context) {
        this(context, null);
    }

    public CanvasSampleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasSampleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDrawable = getResources().getDrawable(R.mipmap.beautiful_girl);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();

        mTextRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        drawBitmap(canvas);
//        drawDrawable(canvas);
        drawRect(canvas);
//        drawRoundRect(canvas);
//        drawPath(canvas);
//        drawBezierCurvePath(canvas);
//        drawPoint(canvas);
//        drawOval(canvas);
//        drawCircle(canvas);
    }


    private void drawCircle(Canvas canvas) {
        mPaint.setStrokeWidth(5);
        canvas.drawCircle(400, 400, 200, mPaint);
    }

    private void drawOval(Canvas canvas) {
//        canvas.drawOval(10, 10, 200, 200, mPaint);//api 21
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.CYAN);
//        mPaint.setStyle(Paint.Style.FILL);

        RectF rectF = new RectF(10, 10, 300, 200);
        canvas.drawOval(rectF, mPaint);
    }

    private void drawPoint(Canvas canvas) {
        mPaint.setStrokeWidth(10);
//        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPoint(20, 20, mPaint);
    }

    private void drawBezierCurvePath(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.moveTo(100, 320);
        path.quadTo(150, 310, 170, 400);
        canvas.drawPath(path, mPaint);
    }

    private void drawPath(Canvas canvas) {
        mPaint.setStrokeWidth(2);

        mPath.moveTo(10, 10);//默认(0, 0)
        mPath.lineTo(80, 80);
        mPath.lineTo(200, 200);
        mPath.lineTo(250, 150);
        mPath.close();//使这些点构成封闭的多边形
        canvas.drawPath(mPath, mPaint);
    }

    private void drawRoundRect(Canvas canvas) {
//        canvas.drawRoundRect(50, 50, 400, 200, 5, 10, mPaint);//api 21

        mPaint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(50, 50, 400, 200);
        canvas.drawRoundRect(rectF, 10, 10, mPaint);
    }

    //把文字画在矩形区域的中央
    private void drawRect(Canvas canvas) {
        mPaint.setStrokeWidth(5);
        final float offsetX = 20;
        final float offsetY = 40;
        final float width = 600;
        final float height = 400;
        canvas.drawRect(offsetX, offsetY, offsetX + width, offsetY + height, mPaint);

        mPaint.setStrokeWidth(1);
        canvas.drawLine(offsetX, offsetY + height / 2, offsetX + width, offsetY + height / 2, mPaint);
        canvas.drawLine(offsetX + width / 2, offsetY, offsetX + width / 2, offsetY + height, mPaint);

        mPaint.setColor(Color.RED);
        mPaint.setTextSize(getResources().getDimension(R.dimen.ts_huger));

        mPaint.getTextBounds(TEXT, 0, TEXT.length(), mTextRect);
        float y = (offsetY + height + offsetY) / 2 - (mPaint.ascent() + mPaint.descent()) / 2;
        canvas.drawText(TEXT, (offsetX + offsetX + width) / 2 - mTextRect.width() / 2, y, mPaint);
    }

    //把图片画到区域中央
    private void drawDrawable(Canvas canvas) {
        int availableWidth = getRight() - getLeft();
        int availableHeight = getBottom() - getTop();

        int w = 320;//mDrawable.getIntrinsicWidth();
        int h = 150;//mDrawable.getIntrinsicHeight();

        mDrawable.setBounds(availableWidth / 2 - w / 2, availableHeight / 2 - h / 2,
                availableWidth / 2 + w / 2, availableHeight / 2 + h / 2);
        mDrawable.draw(canvas);

    }

    private void drawBitmap(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_4);
//        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.beautiful_girl), 10, 10, null);
        canvas.drawBitmap(getRoundCornerBitmap(bitmap, 10), 50, 50, null);
    }

    //test 图层的叠加效果(遮罩层)
    private Bitmap getRoundCornerBitmap(Bitmap bitmap, float roundPx) {
        Bitmap output = createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        final int color = 0xff424242;

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private Bitmap operateBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(output);
        Matrix matrix = new Matrix();
        matrix.postRotate(300);
        matrix.postScale(0.4f, 0.5f);
        matrix.postTranslate(100, 100);
        canvas.drawBitmap(bitmap, matrix, mPaint);
        return output;
    }
}