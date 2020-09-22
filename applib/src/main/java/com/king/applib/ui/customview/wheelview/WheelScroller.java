package com.king.applib.ui.customview.wheelview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.Scroller;

/**
 * https://github.com/CNCoderX/WheelView
 */
class WheelScroller<T extends WheelView.IWheelData> extends Scroller {
    private int mScrollOffset;
    private float lastTouchY;
    private float mLastDownEventY;
    private boolean isScrolling;

    final WheelView<T> mWheelView;
    private VelocityTracker mVelocityTracker;

    WheelView.OnWheelChangedListener<T> onWheelChangedListener;
    WheelView.OnScrollListener<T> mOnScrollListener;

    public static final int JUSTIFY_DURATION = 400;
    private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;

    private int mTouchSlop;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;
    private int mScrollState = WheelView.SCROLL_STATE_IDLE;

    public WheelScroller(Context context, WheelView<T> wheelView) {
        super(context);
        mWheelView = wheelView;
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity() / SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT;
    }

    public void computeScroll() {
        if (isScrolling) {
            isScrolling = computeScrollOffset();
            doScroll(getCurrY() - mScrollOffset);
            if (isScrolling) {
                mWheelView.postInvalidate();
            } else {
                // 滚动结束后，重新调整位置
                justify();
                onScrollStateChange(WheelView.SCROLL_STATE_IDLE);
            }
        }
    }

    private int currentIndex = -1;

    private void doScroll(int distance) {
        mScrollOffset += distance;
        if (!mWheelView.isCyclic()) {
            // 限制滚动边界
            final int maxOffset = (mWheelView.getItemSize() - 1) * mWheelView.mItemHeight;
            if (mScrollOffset < 0) {
                mScrollOffset = 0;
            } else if (mScrollOffset > maxOffset) {
                mScrollOffset = maxOffset;
            }
        }
        notifyWheelChangedListener();
    }

    void notifyWheelChangedListener() {
        int oldValue = currentIndex;
        int newValue = getCurrentIndex();
        if (oldValue != newValue) {
            currentIndex = newValue;
            if (onWheelChangedListener != null) {
                onWheelChangedListener.onChanged(mWheelView, oldValue,
                        mWheelView.getItemText(oldValue), newValue,
                        mWheelView.getItemText(newValue));
            }
        }
    }

    public int getCurrentIndex() {
        final int itemHeight = mWheelView.mItemHeight;
        final int itemSize = mWheelView.getItemSize();
        if (itemSize == 0) {
            return -1;
        }

        int itemIndex;
        if (mScrollOffset < 0) {
            itemIndex = (mScrollOffset - itemHeight / 2) / itemHeight;
        } else {
            itemIndex = (mScrollOffset + itemHeight / 2) / itemHeight;
        }
        int currentIndex = itemIndex % itemSize;
        if (currentIndex < 0) {
            currentIndex += itemSize;
        }
        return currentIndex;
    }

    public void setCurrentIndex(int index, boolean animated) {
        int position = index * mWheelView.mItemHeight;
        int distance = position - mScrollOffset;
        if (distance == 0) {
            return;
        }
        if (animated) {
            isScrolling = true;
            startScroll(0, mScrollOffset, 0, distance, JUSTIFY_DURATION);
        } else {
            doScroll(distance);
        }
        mWheelView.invalidate();
    }

    public int getItemIndex() {
        return mWheelView.mItemHeight == 0 ? 0 : mScrollOffset / mWheelView.mItemHeight;
    }

    public int getItemOffset() {
        return mWheelView.mItemHeight == 0 ? 0 : mScrollOffset % mWheelView.mItemHeight;
    }

    public void reset() {
        isScrolling = false;
        mScrollOffset = 0;
        notifyWheelChangedListener();
        forceFinished(true);
    }

    /**
     * 当滚轮结束滑行后，调整滚轮的位置，需要调用该方法
     */
    void justify() {
        final int itemHeight = mWheelView.mItemHeight;
        final int offset = mScrollOffset % itemHeight;
        if (offset > 0 && offset < itemHeight / 2) {
            isScrolling = true;
            startScroll(0, mScrollOffset, 0, -offset, JUSTIFY_DURATION);
            mWheelView.invalidate();
        } else if (offset >= itemHeight / 2) {
            isScrolling = true;
            startScroll(0, mScrollOffset, 0, itemHeight - offset, JUSTIFY_DURATION);
            mWheelView.invalidate();
        } else if (offset < 0 && offset > -itemHeight / 2) {
            isScrolling = true;
            startScroll(0, mScrollOffset, 0, -offset, JUSTIFY_DURATION);
            mWheelView.invalidate();
        } else if (offset <= -itemHeight / 2) {
            isScrolling = true;
            startScroll(0, mScrollOffset, 0, -itemHeight - offset, JUSTIFY_DURATION);
            mWheelView.invalidate();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchY = mLastDownEventY = event.getY();
                forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float currentMoveY = event.getY();
                if (mScrollState != WheelView.SCROLL_STATE_TOUCH_SCROLL) {
                    int deltaDownY = (int) Math.abs(currentMoveY - mLastDownEventY);
                    if (deltaDownY > mTouchSlop) {
                        onScrollStateChange(WheelView.SCROLL_STATE_TOUCH_SCROLL);
                    }
                }
                int deltaY = (int) (currentMoveY - lastTouchY);
                if (deltaY != 0) {
                    doScroll(-deltaY);
                    mWheelView.invalidate();
                }
                lastTouchY = currentMoveY;
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                float velocityY = mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumFlingVelocity) {
                    isScrolling = true;
                    fling(0, mScrollOffset, 0, (int) -velocityY, 0, 0, Integer.MIN_VALUE,
                            Integer.MAX_VALUE);
                    mWheelView.invalidate();
                    onScrollStateChange(WheelView.SCROLL_STATE_FLING);
                } else {
                    justify();
                    onScrollStateChange(WheelView.SCROLL_STATE_IDLE);
                }
            case MotionEvent.ACTION_CANCEL:
                // 当触发抬起、取消事件后，回收VelocityTracker
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void onScrollStateChange(int scrollState) {
        if (mScrollState == scrollState) {
            return;
        }
        mScrollState = scrollState;
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChange(mWheelView, scrollState);
        }
    }
}
