package com.king.app.workhelper.ui.customview;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.king.app.workhelper.app.AppConfig;

/**
 * @author VanceKing
 * @since 2018/2/9.
 */
public class EventSampleViewGroup extends LinearLayout {

    public EventSampleViewGroup(Context context) {
        this(context, null);
    }

    public EventSampleViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventSampleViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.w(AppConfig.LOG_TAG, "ViewGroup#dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.w(AppConfig.LOG_TAG, "ViewGroup#onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
//        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w(AppConfig.LOG_TAG, "ViewGroup#onTouchEvent");
        return super.onTouchEvent(event);
//        return true;
    }
}
