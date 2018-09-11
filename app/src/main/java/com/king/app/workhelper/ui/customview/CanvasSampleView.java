package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.ImageUtil;

import static android.graphics.Bitmap.createBitmap;

/**
 * Canvas用法
 *
 * @author VanceKing
 * @since 2016/12/24.
 */

public class CanvasSampleView extends View {
    private static final String TEXT = "哈哈哈";

    private Drawable mDrawable;
    private Paint mPaint;
    private Path mPath;
    private Rect mTextRect;
    private int mWidth;
    private int mHeight;
    private int mHalfWidth;
    private int mHalfHeight;

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
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.chocolate));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(ExtendUtil.dp2px(2));

        mPath = new Path();

        mTextRect = new Rect();
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Logger.i("onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mHalfWidth = mWidth / 2;
        mHalfHeight = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCenterLine(canvas);
//        drawBitmap(canvas);
//        drawDrawable(canvas);
//        drawRect(canvas);
//        drawRoundRect(canvas);
//        drawPath(canvas);
//        drawBezierCurvePath(canvas);
//        drawPoint(canvas);
//        drawOval(canvas);
//        drawCircle(canvas);
//        drawGradient(canvas);
//        scrollTest(canvas);
        testCanvasScale(canvas);
    }

    private void drawCenterLine(Canvas canvas) {
        canvas.drawLine(0, mHalfHeight, mWidth, mHalfHeight, mPaint);
        canvas.drawLine(mHalfWidth, 0, mHalfWidth, mHeight, mPaint);
    }

    private void drawCenterRect(Canvas canvas) {
        Rect rect = new Rect();
        int width = mWidth/4;
        int height = mHeight/4;
//        rect.set(mHalfWidth - width / 2, mHalfHeight - height / 2, mHalfWidth + width / 2, mHalfHeight + height / 2);
        rect.set(0, 0, width, height);
        canvas.drawRect(rect, mPaint);
    }

    private void testCanvasScale(Canvas canvas) {
        canvas.save();

//        drawCenterRect(canvas);
        //#1
        canvas.scale(0.5f, 0.5f);
        canvas.translate(mHalfWidth * (1/0.5f), mHalfHeight * (1/0.5f));
        drawCenterRect(canvas);

        //#2 缩放后移动到中间
        /*canvas.translate(mHalfWidth, mHalfHeight);
        canvas.scale(0.5f, 0.5f);
        canvas.translate(-mWidth/8, -mHeight/8);
        drawCenterRect(canvas);*/

        //平移(dx, dy)->应用zoom->平移(-dx * scale, -dy * scale)
        /*canvas.scale(0.6f, 0.6f, mHalfWidth, mHalfHeight);
        drawCenterRect(canvas);*/
        
        canvas.restore();
    }

    private float startX;
    private float distance;
    private float perDistance;

    /*@Override public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                distance = event.getX() - startX;
                layout(getLeft() + (int) distance, getTop(), getRight() + (int) distance, getBottom());
                break;
            case MotionEvent.ACTION_UP:
                perDistance = event.getX() - startX;
                break;
        }
        return true;
    }*/

    private void scrollTest(Canvas canvas) {
        String text = "AAAAAAAAAAAAAAAAAABBBBBBBBBBBBBCCCCCCCCCCCCCCCCCDDDDDDDDDDD";
        mPaint.setTextSize(60);

//        Rect textBounds = getTextBounds(text, mPaint);
//        Log.i(TAG, "-distance: " + (-distance) + "; textBounds.right: "+textBounds.right);

        canvas.drawText(text, 0, mHeight / 2, mPaint);
    }

    //获取文本的位置
    private Rect getTextBounds(String text, Paint paint) {
        paint.getTextBounds(text, 0, text.length(), mTextRect);
        return mTextRect;
    }

    //画一个渐变矩形
    private void drawGradient(Canvas canvas) {
        int width = 200;
        int height = 200;
        /*
        bitmapShader    位图平铺
        linearGradient  线性渐变
        radialGradient  圆形渐变
        sweepGradient   角度渐变
        composeShader   组合效果（组合以上几种）
         */
        //垂直渐变.在(x0, y0)和(x1, y1)连线上做渐变.最后一个参数是模式。
        Shader vShader = new LinearGradient(0, 50, 0, 50 + height, new int[]{Color.parseColor("#3b7aff"), Color.parseColor("#b8d6ff")}, null, Shader.TileMode.CLAMP);
        mPaint.setShader(vShader);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(50, 50, 50 + width, 50 + height, mPaint);

        Shader hShader = new LinearGradient(400, 0, 400 + width, 0, new int[]{Color.parseColor("#3b7aff"), Color.parseColor("#b8d6ff")}, null, Shader.TileMode.CLAMP);
        mPaint.setShader(hShader);
        canvas.drawRect(400, 50, 400 + width, 50 + height, mPaint);

        //创建一个宽高都是200px的渐变Bitmap
        Bitmap bitmap = ImageUtil.drawable2Bitmap(ContextCompat.getDrawable(getContext(), R.drawable.blue_white_v_gradient), 200, 200);//getResources().getDrawable(R.drawable.blue_white_v_gradient)
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(bitmapShader);
        canvas.drawRect(0, 0, width + 200, height + 200, mPaint);//正常显示的渐变
        canvas.drawRect(450, 100, 450 + 200, 300 + 200, mPaint);//对比发现 bitmap的渐变是固定的。
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
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_4);
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

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
