package com.king.app.workhelper.ui.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.applib.util.ExtendUtil;

import java.util.ArrayList;

/**
 * @author dongqi
 * @since 2016/3/18.
 */
//CHECKSTYLE:OFF
public class SparkView extends View {
    private static final String TAG = "SparkView";
    private Matrix mMatrix;
    public int mAlpha = 255;
    protected Drawable mImage;
    private static final long TIMMERTASK_INTERVAL = 50;
    private int paddding;
    private ArrayList<SparkItem> mSparks = new ArrayList<>();
    public static final int BITMAP_SIZE = 6;
    public SparkView(Context context) {
        super(context);
        initView();
    }

    public SparkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SparkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mMatrix = new Matrix();
        mAlpha = 255;
        mImage = ContextCompat.getDrawable(getContext(), R.mipmap.spark);

        paddding = ExtendUtil.dp2px(getContext(), 10);
        for (int i = 0; i < BITMAP_SIZE; i++) {
            Bitmap bitmap = ((BitmapDrawable) ContextCompat.getDrawable(getContext(), R.mipmap.spark)).getBitmap();
            SparkItem sparkItem = new SparkItem(bitmap);
            mSparks.add(sparkItem);
        }
        startx = (int) ((getRight() - getLeft()) / 2.0f - paddding);
    }

    int x;
    float startx;
    private void onUpdate(long miliseconds) {
        for (SparkItem item : mSparks) {
            if (item.isInTime()) {
                x += 2;
                item.update(x, TIMMERTASK_INTERVAL);
            } else {
                x = 0;
                item.update(x, 0);
            }
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        mMatrix.reset();
        for (SparkItem item : mSparks) {
            item.draw(canvas);
        }
    }

    public void show(Context context) {
        int i = 0;
        //int top = Utility.dip2px(getContext(), 70);
        int top = (this.getLayoutParams().height / 2) - ExtendUtil.dp2px(getContext(), 44);
        /*获取屏幕的像素值*/
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int left = (metrics.widthPixels / 2) - ExtendUtil.dp2px(getContext(), 29);
        //int left = Utility.dip2px(getContext(), 80);
        for (SparkItem item : mSparks) {
            i = i + 1;
            item.configure(i, (float) ((BITMAP_SIZE - Math.abs(i - 3)) * 0.8), 1.5f, left, top
                    , (1 + ((BITMAP_SIZE - Math.abs(i - 3))  / 4.0f)), 800 + (i * 80));
        }
        startx = (int) ((getRight() - getLeft()) / 2.0f);
        isStart = true;
        mHandler.postDelayed(runnable, 0);
    }

    private Handler mHandler = new Handler();
    private boolean isStart;
    public void stop() {
        isStart = false;
        mHandler.removeCallbacks(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isStart) {
                onUpdate(TIMMERTASK_INTERVAL);
                mHandler.postDelayed(runnable, TIMMERTASK_INTERVAL);
            }
        }
    };

}
//CHECKSTYLE:ON
