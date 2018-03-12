package com.king.applib.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 简单的弹幕 View。通过绘制实现。
 *
 * @author VanceKing
 * @since 2018/3/6.
 */

public class BarrageView2 extends View {
    private static final int STATUS_RUNNING = 1;
    private static final int STATUS_PAUSE = 2;
    private static final int STATUS_STOP = 3;

    private volatile int mStatus = STATUS_STOP;

    //每次重绘的偏移量
    private static final int STEP = 5;

    private final List<Barrage> mBarrages = new ArrayList<>();
    private Random mRandom = new Random();

    private TextPaint mTextPaint;
    private Paint mBgPaint;
    private int mWeight;
    private int mHeight;
    private int mCorner;
    private final RectF mBgRect = new RectF();

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
        mBgPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mCorner = dp2px(20);
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
        if (barrage.startY <= 0) {
            barrage.startY = getRandomStartY();
        }
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
        mWeight = w;
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

    private void drawBarrage(Canvas canvas) {
        for (Barrage barrage : mBarrages) {
            if (barrage.startX == -1) {
                barrage.startX = mWeight;
                barrage.currentX = barrage.startX;
            }
            if (barrage.isOut()) {
                barrage.currentX = barrage.startX;
            } else {
                drawBackground(canvas, barrage);
                mTextPaint.setColor(barrage.textColor);
                canvas.drawText(barrage.content, barrage.currentX, barrage.startY, mTextPaint);
                barrage.currentX -= STEP * barrage.factor;
            }
        }

    }

    private void drawBackground(Canvas canvas, Barrage barrage) {
        mBgPaint.setColor(barrage.getBgColor());
        mBgPaint.setTextSize(sp2px(barrage.getTextSize()));
        final Paint.FontMetrics fontMetrics = mBgPaint.getFontMetrics();
        int paddingLeftRight = dp2px(10);
        int paddingTopBottom = dp2px(3);
        mBgRect.set(barrage.currentX - paddingLeftRight,
                barrage.startY + fontMetrics.top - paddingTopBottom,
                barrage.currentX + barrage.barrageWidth + paddingLeftRight,
                barrage.startY + fontMetrics.bottom + paddingTopBottom);
        canvas.drawRoundRect(mBgRect, mCorner, mCorner, mBgPaint);
    }

    private void setStatus(int status) {
        this.mStatus = status;
    }

    private int getRandomStartY() {
        return mRandom.nextInt(mHeight);
    }

    public static class Barrage {
        private String content;
        @ColorInt private int textColor = Color.WHITE;
        private int textSize = 16;//in sp
        private int startX = -1;
        private int startY = -1;
        private int currentX;
        private int barrageWidth;
        private float factor = 1;
        @ColorInt private int bgColor = Color.TRANSPARENT;

        public Barrage() {
        }

        public Barrage(String content) {
            this.content = content;
        }

        public Barrage(String content, int startY, int bgColor) {
            this.content = content;
            this.startY = startY;
            this.bgColor = bgColor;
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

        public int getBgColor() {
            return bgColor;
        }

        public void setBgColor(@ColorInt int bgColor) {
            this.bgColor = bgColor;
        }

        public int getStartY() {
            return startY;
        }

        public void setStartY(int startY) {
            this.startY = startY;
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
