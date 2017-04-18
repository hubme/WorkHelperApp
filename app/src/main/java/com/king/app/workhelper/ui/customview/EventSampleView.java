package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.king.applib.log.Logger;

/**
 * @author huoguangxu
 * @since 2017/4/17.
 */

public class EventSampleView extends LinearLayout {
    float eventX = 0;
    float eventY = 0;
    
    public EventSampleView(Context context) {
        this(context, null);
    }

    public EventSampleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventSampleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                eventX = event.getX();
                eventY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                if (Math.abs(x - eventX) > 100) {
                    Logger.i("水平方向滑动");
                    return true;
                }
                if (Math.abs(y - eventY) > 100) {
                    Logger.i("垂直方向滑动");
                    return true;
                }

                break;
        }
        return super.onTouchEvent(event);
    }
}
