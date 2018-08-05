package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.king.applib.util.ExtendUtil;

/**
 * 顶部指定区域不拦截事件的 ScrollView。
 *
 * @author VanceKing
 * @since 2018/8/5.
 */
public class TopSpaceScrollView extends ScrollView {

    private int scrollTop;
    private int topSpace = ExtendUtil.dp2px(100);

    public TopSpaceScrollView(Context context) {
        super(context);
    }

    public TopSpaceScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TopSpaceScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getY() < topSpace - scrollTop) {
                    return false;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        scrollTop = t;
        Log.i("aaa", "scrollTop: " + scrollTop);
    }
}
