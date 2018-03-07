package com.king.applib.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 简单的弹幕 View。
 *
 * @author VanceKing
 * @since 2018/3/6.
 */

public class BarrageView2 extends View {
    private static final String TAG = "aaa";

    private static final int STATUS_RUNNING = 1;
    private static final int STATUS_PAUSE = 2;
    private static final int STATUS_STOP = 3;

    private volatile int mStatus = STATUS_STOP;

    private static final int MIN_INTERVAL = 2500;//mils
    private static final int MAX_INTERVAL = 3500;//mils
    private static final int MIN_DURATION = 3000;//mils
    private static final int MAX_DURATION = 4500;//mils

    //两个弹幕之间显示的间隔
    private int mMinInterval = MIN_INTERVAL;
    private int mMaxInterval = MAX_INTERVAL;

    //每次重绘的偏移量
    private int mOffset = 5;//in px

    private final List<Barrage> mBarrages = new ArrayList<>();
    private final DelayQueue<Barrage> mDoneBarrages = new DelayQueue<>();
    private Random mRandom = new Random();
    private List<Pair<Integer, Integer>> mGradientPairs;

    private TextPaint mTextPaint;
    private int mWidth;
    private int mHeight;
    private Iterator<Barrage> iterator;

    public BarrageView2(Context context) {
        this(context, null);
    }

    public BarrageView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//        mTextPaint.setTextSize(sp2px(14));
        iterator = mBarrages.iterator();
    }

    public void addBarrages(List<Barrage> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        list.clear();
        for (Barrage barrage : list) {
            addBarrage(barrage);
        }
    }

    public void addBarrage(Barrage barrage) {
        initBarrage(barrage);
        mBarrages.add(barrage);
    }

    private void initBarrage(Barrage barrage) {
        barrage.currentX = barrage.startX;
        mTextPaint.setTextSize(sp2px(barrage.getTextSize()));
        barrage.setBarrageWidth((int) mTextPaint.measureText(barrage.content));
        barrage.startY = getRandomStartY();
    }

    private void resetBarrages() {
        for (Barrage barrage : mBarrages) {
            initBarrage(barrage);
        }
    }

    public void show() {
        setStatus(STATUS_RUNNING);
        invalidate();
    }

    public void hide() {
        hide(false);
    }

    /** 是否恢复到初始状态 */
    public void hide(boolean reset) {
        setStatus(STATUS_STOP);
        if (reset) {
            resetBarrages();
        }
        invalidate();
    }

    public void pause() {
        setStatus(STATUS_PAUSE);
        invalidate();
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mStatus) {
            case STATUS_RUNNING:
                drawBarrage(canvas);
                invalidate();
                break;
            case STATUS_PAUSE://保持最后状态
                drawBarrage(canvas);
                break;
            case STATUS_STOP://重置到初始状态
                canvas.drawColor(Color.TRANSPARENT);
                break;
            default:
                break;
        }
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void drawBarrage(Canvas canvas) {
        while (iterator.hasNext()) {
            Barrage barrage = iterator.next();
            if (barrage.isOut()) {
                Log.i("aaa", barrage.content + "已完成");
                iterator.remove();
                continue;
            }
            mTextPaint.setColor(barrage.textColor);
            canvas.drawText(barrage.content, barrage.currentX, barrage.startY, mTextPaint);
            barrage.currentX -= mOffset * barrage.factor;
        }
        
        
        /*for (Barrage barrage : mBarrages) {
            if (barrage.isOut()) {
                Log.i("aaa", barrage.content + "已完成");
                mBarrages.addLast(barrage);
                continue;
            }
            mTextPaint.setColor(barrage.textColor);
            canvas.drawText(barrage.content, barrage.currentX, barrage.startY, mTextPaint);
            barrage.currentX -= mOffset * barrage.factor;
            
        }*/
    }
    
    /*LinearGradient linearGradient = new LinearGradient(100, 100, 500, 500, Color.GREEN, Color.BLUE, Shader.TileMode.CLAMP);
            mTextPaint.setShader(linearGradient);
            canvas.drawRect(100, 100, 500, 500, mTextPaint);
            RectF mCornerRect = new RectF();
            canvas.drawRoundRect();*/

    /*private LinearGradient getGradientShader(Barrage barrage) {
        mGradientPairs.get()
    }*/

    public void setGradientColors(List<Pair<Integer, Integer>> gradientPairs) {
        mGradientPairs = gradientPairs;
    }

    private void setStatus(int status) {
        this.mStatus = status;
    }

    private int getRandomDelay() {
        return mRandom.nextInt((mMaxInterval - mMinInterval) + 1) + mMinInterval;
    }

    private int getRandomStartY() {
        return mRandom.nextInt(mHeight);
    }

    public static class Barrage implements Delayed {
        private String content;
        @ColorInt private int textColor = Color.BLACK;
        private int textSize = 16;//in sp
        private int startX;
        private int startY;
        private int currentX;
        private int barrageWidth;
        private float factor = 1;

        public Barrage() {
        }

        public Barrage(String content, int startX) {
            this.content = content;
            this.startX = startX;
        }

        public Barrage(String content, int startX, int startY) {
            this(content, startX);
            this.startY = startY;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(@ColorInt int textColor) {
            this.textColor = textColor;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        public void setFactor(float factor) {
            this.factor = factor;
        }

        private boolean isOut() {
            return currentX + barrageWidth < 0 || currentX > startX;
        }

        private void setBarrageWidth(int textWidth) {
            barrageWidth = textWidth;
        }

        @Override public long getDelay(@NonNull TimeUnit unit) {
            return 0;
        }

        @Override public int compareTo(@NonNull Delayed o) {
            return 0;
        }
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
