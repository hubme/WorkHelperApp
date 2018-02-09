package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.king.app.workhelper.app.AppConfig;

/**
 * @author VanceKing
 * @since 2017/4/17.
 */

public class EventSampleView extends View {
    public EventSampleView(Context context) {
        this(context, null);
    }

    public EventSampleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventSampleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //返回 true 表示接下来的事件由当前 View 来处理，不会向下传递了。
    @Override public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e(AppConfig.LOG_TAG, "View#dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
//        return true;
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        Log.e(AppConfig.LOG_TAG, "View#onTouchEvent");
        return super.onTouchEvent(event);
//        return true;
    }
}
