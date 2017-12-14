package com.king.app.workhelper.ui.customview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.king.applib.util.ExtendUtil;
import com.king.applib.util.ScreenUtil;

/**
 * View滑动实现。
 * 1.view进行绘制的时候会调用onLayout()方法来设置显示的位置，因此我们同样也可以通过修改View的left、top、right、bottom这四种属性来控制View的坐标。
 * 2.offsetLeftAndRight 和 offsetTopAndBottom
 * 3.layoutParams
 * 4.动画
 * 5.scollTo与scollBy
 * 6.Scroller
 * @author huoguangxu
 * @since 2017/12/14.
 */

public class SlideTestView extends View {
    private static final String TAG = "aaa";
    private static final String TEXT = "00000000000000000000000哈哈哈1111111111111111111111111222222222222222223好";

    private int mWidth;
    private int mHeight;
    private Paint mPaint;

    private int mOffsetX;
    private int mOffsetY;

    private int mScreenWidth = ScreenUtil.getScreenWidth(getContext());

    public SlideTestView(Context context) {
        this(context, null);
    }

    public SlideTestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(50);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) mPaint.measureText(TEXT), ExtendUtil.dp2px(300));
//        setMeasuredDimension(ExtendUtil.dp2px(400), ExtendUtil.dp2px(300));
//        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), ExtendUtil.dp2px(300));
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        Log.i(TAG, "mWidth: " + w + "; mHeight: " + h);
//        mWidth = w;
//        mHeight = h;
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        Log.i(TAG, "onDraw。" + "; mWidth: " + mWidth + "; mHeight: " + mHeight);
        canvas.drawText(TEXT, 0, mHeight / 2, mPaint);
    }

    int mStartX = 0;
    int mStartY = 0;
    @Override public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getX()+getScrollX();
                mStartY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mOffsetX = (int) event.getX() - mStartX;
                mOffsetY = (int) event.getY() - mStartY;

                Log.i(TAG, "mScreenWidth: "+mScreenWidth+" ;getScrollX: " + getScrollX() + " ;mWidth: " + mWidth);
                if ((mScreenWidth + getScrollX() < mWidth) || (mWidth - mScreenWidth < getScrollX() && getScrollX() > 0)) {
                    slideWithScrollTo();
                }
                break;
            case MotionEvent.ACTION_UP:
                
                break;
        }
        return true;
    }

    private void slideWithScrollTo() {
        Log.i(TAG, "mOffsetX: " + mOffsetX+" ;getScrollX: "+getScrollX());
        scrollTo(-mOffsetX, 0);
    }

    private void slideWithScrollBy() {
        Log.i(TAG, "mOffsetX: " + mOffsetX);
        scrollBy(-mOffsetX, 0);
    }

    private void slideWithAnimation() {
        ObjectAnimator.ofFloat(this,"translationX",0,300).setDuration(1000).start();
    }

    private void slideWithLayout() {
        if (getRight() > mScreenWidth && mOffsetX < 0) {// 左滑
            Log.i(TAG, "左滑。" + "mOffsetX: " + mOffsetX + "; mScreenWidth: " + mScreenWidth + "; getLeft(): " + getLeft() + "; getRight(): " + getRight());
            layout(getLeft() + (int) mOffsetX, getTop(), Math.max(getRight() + (int) mOffsetX, mScreenWidth), getBottom());
        }

        if (getLeft() < 0 && mOffsetX > 0) {// 右滑
            Log.i(TAG, "右滑。" + "mOffsetX: " + mOffsetX + "; mScreenWidth: " + mScreenWidth + "; getLeft(): " + getLeft() + "; getRight(): " + getRight());
            layout(Math.min(getLeft() + (int) mOffsetX, 0), getTop(), getRight() + (int) mOffsetX, getBottom());
        }

        layout(getLeft() + mOffsetX, getTop() + mOffsetY, getRight() + mOffsetX, getBottom() + mOffsetY);
    }

    private void slideWithOffset() {
        offsetLeftAndRight(mOffsetX);
        offsetTopAndBottom(mOffsetY);
    }

    private void slideWithLayoutParams() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        layoutParams.leftMargin = getLeft() + mOffsetX;
        layoutParams.topMargin = getTop() + mOffsetY;
        setLayoutParams(layoutParams);
    }
}
