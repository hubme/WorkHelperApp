package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.king.applib.util.ExtendUtil;

/**
 * 可点击Drawable的EditText
 * Created by HuoGuangxu on 2016/11/14.
 */

public class DrawableEditText extends AppCompatEditText {

    private DrawableLeftListener mLeftListener;
    private DrawableRightListener mRightListener;

    private final int DRAWABLE_LEFT = 0;
    private final int DRAWABLE_TOP = 1;
    private final int DRAWABLE_RIGHT = 2;
    private final int DRAWABLE_BOTTOM = 3;
    private float mExtraSpace;

    public DrawableEditText(Context context) {
        //不能使用this()
        super(context);
        initView();
    }

    public DrawableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DrawableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mExtraSpace = ExtendUtil.dp2px(15);
    }

    /**
     * 设置图片额外点击的区域.默认15dp
     */
    public void setDrawableExtraSpace(@DimenRes int resID) {
        mExtraSpace = getResources().getDimension(resID);
    }

    /**
     * 设置图片额外点击的区域.默认15dp
     * @param extraSpace > 0有效
     */
    public void setDrawableExtraSpace(float extraSpace) {
        if (extraSpace > 0) {
            mExtraSpace = extraSpace;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mRightListener != null) {
                    Drawable drawableRight = getCompoundDrawables()[DRAWABLE_RIGHT];
                    if (drawableRight != null && (event.getX() >= (getRight() - drawableRight.getBounds().width() - mExtraSpace) && event.getX() <= getRight() + mExtraSpace)) {
                        mRightListener.onDrawableRightClick(this);
                        return true;
                    }
                }

                if (mLeftListener != null) {
                    Drawable drawableLeft = getCompoundDrawables()[DRAWABLE_LEFT];
                    if (drawableLeft != null && (event.getX() <= (getLeft() + drawableLeft.getBounds().width() + mExtraSpace) && event.getX() >= getLeft() - mExtraSpace)) {
                        mLeftListener.onDrawableLeftClick(this);
                        return true;
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    public void setDrawableLeftListener(DrawableLeftListener listener) {
        this.mLeftListener = listener;
    }

    public void setDrawableRightListener(DrawableRightListener listener) {
        this.mRightListener = listener;
    }

    public interface DrawableLeftListener {
        void onDrawableLeftClick(View view);
    }

    public interface DrawableRightListener {
        void onDrawableRightClick(View view);
    }
}
