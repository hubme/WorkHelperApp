package com.king.app.workhelper.ui.customview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import com.king.app.workhelper.R;

import java.util.TimeZone;

/**
 * 模拟时钟，自适应宽高。
 * Created by VanceKing on 2016/12/24 0024.
 */

public class AnalogClock extends View {

    private Drawable mDial;
    private Drawable mHourHand;
    private Drawable mMinuteHand;

    private Time mCalendar;

    private float mDialWidth;
    private float mDialHeight;

    private boolean mAttached;
    private boolean mChanged;

    private float mMinutes;
    private float mHour;

    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
                String timeZone = intent.getStringExtra("time_zone");
                mCalendar = new Time(TimeZone.getTimeZone(timeZone).getID());
            }
            if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                onTimeChanged();
                invalidate();
            }
        }
    };

    public AnalogClock(Context context) {
        this(context, null);
    }

    public AnalogClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDial = getResources().getDrawable(R.mipmap.clock_dial);
        mHourHand = getResources().getDrawable(R.mipmap.clock_hand_hour);
        mMinuteHand = getResources().getDrawable(R.mipmap.clock_hand_minute);

        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float widthScale = 1.0f;
        float heightScale = 1.0f;

        //宽度缩放比
        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            widthScale = (float) widthSize / mDialWidth;
        }
        //高度缩放比
        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            heightScale = (float) heightSize / mDialHeight;
        }

        //系数越小，缩放的越大，越能显示完全view
        float scale = Math.min(widthScale, heightScale);

        //设置缩放后的宽高
        setMeasuredDimension(resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0),
                resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final boolean changed = mChanged;
        if (mChanged) {
            mChanged = false;
        }

        int availableWidth = getRight() - getLeft();
        int availableHeight = getBottom() - getTop();

        int x = availableWidth / 2;
        int y = availableHeight / 2;

        final Drawable dial = mDial;
        int width = dial.getIntrinsicWidth();
        int height = dial.getIntrinsicHeight();

        //检查是否需要压缩
        boolean scaled = false;
        if (availableWidth < width || availableHeight < height) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) width, (float) availableHeight / (float) height);
            canvas.save();
            canvas.scale(scale, scale, width, height);
        }

        if (changed) {
            dial.setBounds(x - (width) / 2, y - (height / 2), x + (width) / 2, y + (height / 2));
        }
        dial.draw(canvas);

        canvas.save();
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        final Drawable hourHand = mHourHand;
        if (changed) {
            width = hourHand.getIntrinsicWidth();
            height = hourHand.getIntrinsicHeight();
            hourHand.setBounds(x - (width / 2), y - (height / 2), x + (width / 2), y + (height / 2));
        }
        hourHand.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);

        final Drawable minuteHand = mMinuteHand;
        if (changed) {
            width = minuteHand.getIntrinsicWidth();
            height = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (width / 2), y - (height / 2), x + (width / 2), y + (height / 2));
        }
        minuteHand.draw(canvas);
        canvas.restore();

        if (scaled) {
            canvas.restore();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_TIME_TICK);//Sent every minute
            intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
            intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

            getContext().registerReceiver(mIntentReceiver, intentFilter);

            mCalendar = new Time();
            onTimeChanged();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            mAttached = false;
            getContext().unregisterReceiver(mIntentReceiver);
        }
    }

    private void onTimeChanged() {
        mCalendar.setToNow();
        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;

        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;
        mChanged = true;
    }
}
