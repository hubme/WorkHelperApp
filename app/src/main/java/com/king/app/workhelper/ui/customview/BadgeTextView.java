package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.king.applib.log.Logger;
import com.king.applib.util.StringUtil;

/**
 * 带徽标的TextView
 * Created by HuoGuangxu on 2016/12/20.
 */

public class BadgeTextView extends TextView {

    /** 是否显示徽标 */
    private boolean mVisible;
    /** 徽标的文字 */
    private String mBadgeText = "";

    private Paint mPaint;

    private static final int DEFAULT_TEXT_SIZE = 15;
    private int mTextSize = DEFAULT_TEXT_SIZE;

    public BadgeTextView(Context context) {
        this(context, null);
    }

    public BadgeTextView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BadgeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize);
    }

    public void drawBadge() {
        Logger.i("MeasuredWidth : "+getMeasuredWidth());
        Logger.i("MeasuredHeight : "+getMeasuredHeight());
    }

    /*void layout(int x, int y, int max) {
        Rect rect = getBounds();
        rect.offsetTo(Math.min(x - rect.width() / 2, max - rect.width() - (int)(0.2f * mHeight)), Math.max(0, y - rect.height() / 2));
        setBounds(rect);
    }*/

    public void setBadgeText(String text) {
        if (StringUtil.isNullOrEmpty(text)) {
            mBadgeText = text;
        }
    }

    public void setBadgeTextSize(int size) {
        if (size > 0) {
            mTextSize = size;
        }
    }

    public void setBadgeVisible(boolean visible) {
        mVisible = visible;
    }
}
