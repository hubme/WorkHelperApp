package com.king.applib.ui.customview.wheelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.IntDef;

import com.king.applib.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/CNCoderX/WheelView
 */
public class WheelView<T extends WheelView.IWheelData> extends View {
    boolean mCyclic;
    int mItemCount;
    int mItemWidth;
    int mItemHeight;
    Rect mClipRectTop;
    Rect mClipRectMiddle;
    Rect mClipRectBottom;

    TextPaint mTextPaint;
    TextPaint mSelectedTextPaint;
    Paint mDividerPaint;
    Paint mHighlightPaint;

    final WheelScroller<T> mScroller;

    private final List<T> mEntries = new ArrayList<>();

    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_TOUCH_SCROLL = 1;
    public static final int SCROLL_STATE_FLING = 2;

    @IntDef({SCROLL_STATE_IDLE, SCROLL_STATE_TOUCH_SCROLL, SCROLL_STATE_FLING})
    @Retention(RetentionPolicy.SOURCE)
    @interface ScrollState {
    }

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelView);
        boolean cyclic = a.getBoolean(R.styleable.WheelView_wv_cyclic, false);
        int itemCount = a.getInt(R.styleable.WheelView_wv_visibleCount, 9);
        int itemWidth = a.getDimensionPixelOffset(R.styleable.WheelView_wv_itemWidth,
                (int) dp2px(160));
        int itemHeight = a.getDimensionPixelOffset(R.styleable.WheelView_wv_itemHeight,
                (int) dp2px(40));
        int textSize = a.getDimensionPixelSize(R.styleable.WheelView_wv_textSize, (int) sp2px());
        int textColor = a.getColor(R.styleable.WheelView_wv_textColor, Color.GRAY);
        int selectedTextColor = a.getColor(R.styleable.WheelView_wv_selectedTextColor, Color.BLACK);
        int dividerColor = a.getColor(R.styleable.WheelView_wv_dividerColor, Color.BLUE);
        int highlightColor = a.getColor(R.styleable.WheelView_wv_highlightColor, Color.TRANSPARENT);
        a.recycle();

        mCyclic = cyclic;
        mItemCount = itemCount;
        mItemWidth = itemWidth;
        mItemHeight = itemHeight;

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);

        mSelectedTextPaint = new TextPaint();
        mSelectedTextPaint.setAntiAlias(true);
        mSelectedTextPaint.setTextAlign(Paint.Align.CENTER);
        mSelectedTextPaint.setTextSize(textSize);
        mSelectedTextPaint.setColor(selectedTextColor);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStrokeWidth(dp2px(1));
        mDividerPaint.setColor(dividerColor);

        mHighlightPaint = new Paint();
        mHighlightPaint.setAntiAlias(true);
        mHighlightPaint.setStyle(Paint.Style.FILL);
        mHighlightPaint.setColor(highlightColor);

        mScroller = new WheelScroller<T>(context, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        } else if (widthSpecMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSpecSize, getPrefHeight());
        } else if (heightSpecMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(getPrefWidth(), heightSpecSize);
        } else {
            setMeasuredDimension(getPrefWidth(), getPrefHeight());
        }
        updateClipRect();
    }

    private void updateClipRect() {
        int clipLeft = getPaddingLeft();
        int clipRight = getMeasuredWidth() - getPaddingRight();
        int clipTop = getPaddingTop();
        int clipBottom = getMeasuredHeight() - getPaddingBottom();
        int clipVMiddle = (clipTop + clipBottom) / 2;

        mClipRectMiddle = new Rect();
        mClipRectMiddle.left = clipLeft;
        mClipRectMiddle.right = clipRight;
        mClipRectMiddle.top = clipVMiddle - mItemHeight / 2;
        mClipRectMiddle.bottom = clipVMiddle + mItemHeight / 2;

        mClipRectTop = new Rect();
        mClipRectTop.left = clipLeft;
        mClipRectTop.right = clipRight;
        mClipRectTop.top = clipTop;
        mClipRectTop.bottom = clipVMiddle - mItemHeight / 2;

        mClipRectBottom = new Rect();
        mClipRectBottom.left = clipLeft;
        mClipRectBottom.right = clipRight;
        mClipRectBottom.top = clipVMiddle + mItemHeight / 2;
        mClipRectBottom.bottom = clipBottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawHighlight(canvas);
        drawItems(canvas);
        drawDivider(canvas);
    }

    private void drawItems(Canvas canvas) {
        final int index = mScroller.getItemIndex();
        final int offset = mScroller.getItemOffset();
        final int hf = (mItemCount + 1) / 2;
        final int minIdx, maxIdx;
        if (offset < 0) {
            minIdx = index - hf - 1;
            maxIdx = index + hf;
        } else if (offset > 0) {
            minIdx = index - hf;
            maxIdx = index + hf + 1;
        } else {
            minIdx = index - hf;
            maxIdx = index + hf;
        }
        for (int i = minIdx; i < maxIdx; i++) {
            drawItem(canvas, i, offset);
        }
    }

    protected void drawItem(Canvas canvas, int index, int offset) {
        String text = getItemText(index);
        if (text == null) {
            return;
        }

        final int centerX = mClipRectMiddle.centerX();
        final int centerY = mClipRectMiddle.centerY();

        // 和中间选项的距离
        final int range = (index - mScroller.getItemIndex()) * mItemHeight - offset;

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        int baseline = (int) ((fontMetrics.top + fontMetrics.bottom) / 2);

        // 绘制与下分界线相交的文字
        if (range > 0 && range < mItemHeight) {
            canvas.save();
            canvas.clipRect(mClipRectMiddle);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline,
                    mSelectedTextPaint);
            canvas.restore();

            canvas.save();
            canvas.clipRect(mClipRectBottom);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline,
                    mTextPaint);
            canvas.restore();
        }
        // 绘制下分界线下方的文字
        else if (range >= mItemHeight) {
            canvas.save();
            canvas.clipRect(mClipRectBottom);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline,
                    mTextPaint);
            canvas.restore();
        }
        // 绘制与上分界线相交的文字
        else if (range < 0 && range > -mItemHeight) {
            canvas.save();
            canvas.clipRect(mClipRectMiddle);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline,
                    mSelectedTextPaint);
            canvas.restore();

            canvas.save();
            canvas.clipRect(mClipRectTop);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline,
                    mTextPaint);
            canvas.restore();
        }
        // 绘制上分界线上方的文字
        else if (range <= -mItemHeight) {
            canvas.save();
            canvas.clipRect(mClipRectTop);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline,
                    mTextPaint);
            canvas.restore();
        }
        // 绘制两条分界线之间的文字
        else {
            canvas.save();
            canvas.clipRect(mClipRectMiddle);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline,
                    mSelectedTextPaint);
            canvas.restore();
        }
    }

    public String getItemText(int index) {
        int size = mEntries.size();
        if (size == 0) {
            return null;
        }
        T data = null;
        if (isCyclic()) {
            int i = index % size;
            if (i < 0) {
                i += size;
            }
            data = mEntries.get(i);
        } else {
            if (index >= 0 && index < size) {
                data = mEntries.get(index);
            }
        }
        if (data == null) {
            return null;
        } else {
            return data.getWheelText();
        }
    }

    private void drawHighlight(Canvas canvas) {
        canvas.drawRect(mClipRectMiddle, mHighlightPaint);
    }

    private void drawDivider(Canvas canvas) {
        // 绘制上层分割线
        canvas.drawLine(mClipRectMiddle.left, mClipRectMiddle.top, mClipRectMiddle.right,
                mClipRectMiddle.top, mDividerPaint);

        // 绘制下层分割线
        canvas.drawLine(mClipRectMiddle.left, mClipRectMiddle.bottom, mClipRectMiddle.right,
                mClipRectMiddle.bottom, mDividerPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        return mScroller.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        mScroller.computeScroll();
    }

    public boolean isCyclic() {
        return mCyclic;
    }

    public void setCyclic(boolean cyclic) {
        mCyclic = cyclic;
        mScroller.reset();
        invalidate();
    }

    public float getTextSize() {
        return mTextPaint.getTextSize();
    }

    public void setTextSize(int textSize) {
        mTextPaint.setTextSize(textSize);
        mSelectedTextPaint.setTextSize(textSize);
        invalidate();
    }

    public int getTextColor() {
        return mTextPaint.getColor();
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    public int getSelectedTextColor() {
        return mSelectedTextPaint.getColor();
    }

    public void setSelectedTextColor(int color) {
        mSelectedTextPaint.setColor(color);
        invalidate();
    }

    public int getItemSize() {
        return mEntries.size();
    }

    public T getItem(int index) {
        if (!mEntries.isEmpty() && index >= 0 && index < mEntries.size()) {
            return mEntries.get(index);
        } else {
            return null;
        }
    }

    public T getCurrentItem() {
        return getItem(getCurrentIndex());
    }

    public int getCurrentIndex() {
        return mScroller.getCurrentIndex();
    }

    public void setCurrentIndex(int index) {
        setCurrentIndex(index, false);
    }

    public void setCurrentIndex(int index, boolean animated) {
        mScroller.setCurrentIndex(index, animated);
    }

    public void setEntries(List<T> entries) {
        mEntries.clear();
        if (entries != null && entries.size() > 0) {
            mEntries.addAll(entries);
        }
        mScroller.reset();
        invalidate();
    }

    private float dp2px(int value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                getResources().getDisplayMetrics());
    }

    private float sp2px() {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20,
                getResources().getDisplayMetrics());
    }

    /**
     * @return 控件的预算宽度
     */
    protected int getPrefWidth() {
        int paddingHorizontal = getPaddingLeft() + getPaddingRight();
        return paddingHorizontal + mItemWidth;
    }

    /**
     * @return 控件的预算高度
     */
    protected int getPrefHeight() {
        int paddingVertical = getPaddingTop() + getPaddingBottom();
        return paddingVertical + mItemHeight * mItemCount;
    }

    public interface OnWheelChangedListener<T extends IWheelData> {
        void onChanged(WheelView<T> view, int oldIndex, CharSequence oldValue, int newIndex,
                       CharSequence newValue);
    }

    public OnWheelChangedListener<T> getOnWheelChangedListener() {
        return mScroller.onWheelChangedListener;
    }

    public void setOnWheelChangedListener(OnWheelChangedListener<T> onWheelChangedListener) {
        mScroller.onWheelChangedListener = onWheelChangedListener;
    }

    public void setOnScrollListener(OnScrollListener<T> mOnScrollListener) {
        mScroller.mOnScrollListener = mOnScrollListener;
    }

    public OnScrollListener<T> getOnScrollListener() {
        return mScroller.mOnScrollListener;
    }

    public interface OnScrollListener<T extends IWheelData> {

        void onScrollStateChange(WheelView<T> view, @ScrollState int scrollState);
    }

    public interface IWheelData {
        String getWheelText();
    }
}
