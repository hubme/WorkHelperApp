package com.king.app.workhelper.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.king.app.workhelper.R;

/**
 * https://developer.android.com/reference/android/graphics/PorterDuff.Mode
 * <p>
 * Android在绘图时会先检查该画笔Paint对象有没有设置Xfermode，
 * 如果没有设置Xfermode，那么直接将绘制的图形覆盖Canvas对应位置原有的像素；
 * 如果设置了Xfermode，那么会按照Xfermode具体的规则来更新Canvas中对应位置的像素颜色。
 * <p>
 * 源像素的ARGB四个分量会和Canvas上同一位置处的目标像素的ARGB四个分量按照Xfermode定义的规则进行计算，
 * 形成最终的ARGB值，然后用该最终的ARGB值更新目标像素的ARGB值。
 *
 * @author VanceKing
 * @since 2021/9/25
 */
public class PorterDuffXfermodeView extends View {
    private Paint mPaint;
    private PorterDuffXfermode mXfermode;

    public PorterDuffXfermodeView(Context context) {
        this(context, null);
    }

    public PorterDuffXfermodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PorterDuffXfermodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //PorterDuff.Mode.CLEAR: 由于Activity本身屏幕的背景时白色的，所以此处就显示了一个白色的矩形。
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        test3(canvas);
    }

    private void test3(Canvas canvas) {
        int left = 50;
        int top = 50;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.chocolate));

        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.home);
        Log.i("aaa", "srcBitmap。width: " + srcBitmap.getWidth() + " height: " + srcBitmap.getHeight());

        Rect imageRect = new Rect(left, top, srcBitmap.getWidth(), srcBitmap.getHeight());
        RectF imageViewRectF = new RectF(left, top, getWidth() / 2f, 500f);

        Bitmap dstBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas dstCanvas = new Canvas(dstBitmap);
        dstCanvas.drawRoundRect(imageViewRectF, 40, 40, paint);

        int layerId = canvas.saveLayer(imageViewRectF, paint);
        canvas.drawBitmap(dstBitmap, 0f, 0f, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(srcBitmap, imageRect, imageViewRectF, paint);
        paint.setXfermode(null);

        canvas.restoreToCount(layerId);
    }

    private void test2(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        canvas.drawARGB(255, 139, 197, 186);

        // 新建图层，所有像素值的ARGB为(0, 0, 0, 0)
        int layerId = canvas.saveLayer(0, 0, width, height, null);
        int circle = width / 3;
        mPaint.setColor(0xFFFFCC44);
        canvas.drawCircle(circle + 20, circle + 20, circle, mPaint);
        mPaint.setXfermode(mXfermode);
        mPaint.setColor(0xFF66AAFF);
        // 将要绘制的矩形为 src，Canvas 对应位置上的矩形为 dst
        canvas.drawRect(circle, circle, circle * 2.7f, circle * 2.7f, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    private void test1(Canvas canvas) {
        canvas.drawColor(Color.GRAY);

        int W = getWidth();
        int H = getHeight();

        // 生成了一个全新的bitmap，大小就是我们指定的保存区域的大小
        // 新生成的bitmap是全透明的，在调用saveLayer后所有的绘图操作都是在这个bitmap上进行的
        int sc = canvas.saveLayer(0, 0, W, H, null);

        canvas.drawBitmap(makeDst(W, H), 0, 0, mPaint);
        mPaint.setXfermode(mXfermode);
        canvas.drawBitmap(makeSrc(W, H), 0, 0, mPaint);

        // 清除 mode
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);
    }

    private Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFFFFCC44);
        c.drawOval(new RectF(0, 0, w * 3 / 4f, h * 3 / 4f), p);
        return bm;
    }

    static Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFF66AAFF);
        c.drawRect(w / 3f, h / 3f, w * 19 / 20f, h * 19 / 20f, p);
        return bm;
    }
}
