package com.king.applib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import androidx.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.king.applib.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * 滑动删除菜单.https://juejin.im/entry/593f20cbac502e006c1bf5e7
 * Created by guanaj on 2017/6/5.
 */

public class EasySwipeMenuLayout extends ViewGroup {
    private static final String TAG = "EasySwipeMenuLayout";

    private final ArrayList<View> mMatchParentChildren = new ArrayList<>(1);
    private int mLeftViewResID;
    private int mRightViewResID;
    private int mContentViewResID;
    private View mLeftView;
    private View mRightView;
    private View mContentView;
    private MarginLayoutParams mContentViewLp;
    private PointF mLastP;
    private PointF mFirstP;
    private float mFraction = 0.3f;
    private boolean mCanLeftSwipe = true;
    private boolean mCanRightSwipe = true;
    private int mScaledTouchSlop;
    private Scroller mScroller;

    private float finallyDistanceX;

    private static final int MENU_LEFT_OPENED = 0x01;
    private static final int MENU_RIGHT_OPENED = 0x02;
    private static final int MENU_CLOSED = 0x03;
    private static final int MENU_OPENING = 0x04;

    @IntDef({MENU_LEFT_OPENED, MENU_RIGHT_OPENED, MENU_CLOSED, MENU_OPENING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MENU_STATE {
    }

    @MENU_STATE private int mCurrMenuState = MENU_CLOSED;

    public EasySwipeMenuLayout(Context context) {
        this(context, null);
    }

    public EasySwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasySwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);

    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mFirstP = new PointF();
        mLastP = new PointF();

        //创建辅助对象
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mScaledTouchSlop = viewConfiguration.getScaledTouchSlop();
//        Log.i(TAG, "mScaledTouchSlop: " + mScaledTouchSlop);
        Log.i(TAG, "init(EasySwipeMenuLayout.java:80) " + "mScaledTouchSlop: " + mScaledTouchSlop);
        mScroller = new Scroller(context);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EasySwipeMenuLayout, defStyleAttr, 0);

        try {
            int indexCount = typedArray.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.EasySwipeMenuLayout_leftMenuView) {
                    mLeftViewResID = typedArray.getResourceId(R.styleable.EasySwipeMenuLayout_leftMenuView, -1);
                } else if (attr == R.styleable.EasySwipeMenuLayout_rightMenuView) {
                    mRightViewResID = typedArray.getResourceId(R.styleable.EasySwipeMenuLayout_rightMenuView, -1);
                } else if (attr == R.styleable.EasySwipeMenuLayout_contentView) {
                    mContentViewResID = typedArray.getResourceId(R.styleable.EasySwipeMenuLayout_contentView, -1);
                } else if (attr == R.styleable.EasySwipeMenuLayout_canLeftSwipe) {
                    mCanLeftSwipe = typedArray.getBoolean(R.styleable.EasySwipeMenuLayout_canLeftSwipe, true);
                } else if (attr == R.styleable.EasySwipeMenuLayout_canRightSwipe) {
                    mCanRightSwipe = typedArray.getBoolean(R.styleable.EasySwipeMenuLayout_canRightSwipe, true);
                } else if (attr == R.styleable.EasySwipeMenuLayout_fraction) {
                    mFraction = typedArray.getFloat(R.styleable.EasySwipeMenuLayout_fraction, 0.5f);
                }
            }

        } finally {
            typedArray.recycle();
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //参考frameLayout测量代码
        final boolean measureMatchParentChildren = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
//        Log.i(TAG, "measureMatchParentChildren :" + measureMatchParentChildren);
        if (!mMatchParentChildren.isEmpty()) {
            mMatchParentChildren.clear();
        }
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        //遍历childViews
        for (int i = 0, count = getChildCount(); i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                //测量子View
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                //如果View中有MATCH_PARENT需要再次测量
                if (measureMatchParentChildren) {
                    if (lp.width == LayoutParams.MATCH_PARENT ||
                            lp.height == LayoutParams.MATCH_PARENT) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }
        // 考虑背景大小
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));

        final int count = mMatchParentChildren.size();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                final View child = mMatchParentChildren.get(i);
                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                final int childWidthMeasureSpec;
                if (lp.width == LayoutParams.MATCH_PARENT) {
                    final int width = Math.max(0, getMeasuredWidth() - lp.leftMargin - lp.rightMargin);
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, lp.leftMargin + lp.rightMargin, lp.width);
                }

                final int childHeightMeasureSpec;
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    final int height = Math.max(0, getMeasuredHeight() - lp.topMargin - lp.bottomMargin);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.height);
                }

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int pLeft = getPaddingLeft();
        final int pRight = getPaddingLeft();
        final int pTop = getPaddingTop();
        final int pBottom = getPaddingTop();

        for (int i = 0, count = getChildCount(); i < count; i++) {
            final View child = getChildAt(i);
            if (mLeftView == null && child.getId() == mLeftViewResID) {
                mLeftView = child;
            } else if (mRightView == null && child.getId() == mRightViewResID) {
                mRightView = child;
            } else if (mContentView == null && child.getId() == mContentViewResID) {
                mContentView = child;
            }

        }

        //布局contentView
        if (mContentView != null) {
            mContentViewLp = (MarginLayoutParams) mContentView.getLayoutParams();
            mContentView.layout(pLeft + mContentViewLp.leftMargin,
                    pTop + mContentViewLp.topMargin,
                    pLeft + mContentViewLp.leftMargin + mContentView.getMeasuredWidth(),
                    pTop + mContentViewLp.topMargin + mContentView.getMeasuredHeight());
        }
        if (mLeftView != null) {
            MarginLayoutParams leftViewLp = (MarginLayoutParams) mLeftView.getLayoutParams();
            mLeftView.layout(0 - mLeftView.getMeasuredWidth() + leftViewLp.leftMargin + leftViewLp.rightMargin,
                    pTop + leftViewLp.topMargin,
                    0 - leftViewLp.rightMargin,
                    pTop + leftViewLp.topMargin + mLeftView.getMeasuredHeight());
        }
        if (mRightView != null) {
            MarginLayoutParams rightViewLp = (MarginLayoutParams) mRightView.getLayoutParams();
            mRightView.layout(mContentView.getRight() + mContentViewLp.rightMargin + rightViewLp.leftMargin,
                    pTop + rightViewLp.topMargin,
                    mContentView.getRight() + mContentViewLp.rightMargin + rightViewLp.leftMargin + mRightView.getMeasuredWidth(),
                    pTop + rightViewLp.topMargin + mRightView.getMeasuredHeight());
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "dispatchTouchEvent ACTION_DOWN");

                mFirstP.set(ev.getRawX(), ev.getRawY());
                mLastP.set(ev.getRawX(), ev.getRawY());

                if (mCurrMenuState != MENU_CLOSED) {
                    refreshMenuState(MENU_CLOSED);
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG, ">>>>dispatchTouchEvent() ACTION_MOVE getScrollX:" + getScrollX());

                final float distanceX = mLastP.x - ev.getRawX();
                final float distanceY = mLastP.y - ev.getRawY();
                if (Math.abs(distanceY) > Math.abs(distanceX) && Math.abs(Math.abs(distanceY) - Math.abs(distanceX)) > mScaledTouchSlop) {//垂直滑动
                    Log.i(TAG, "dispatchTouchEvent 垂直滚动");
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                }
                //当处于水平滑动时，禁止父类拦截
                if (Math.abs(distanceX) > mScaledTouchSlop) {
                    Log.i(TAG, "dispatchTouchEvent 当处于水平滑动时，禁止父类拦截ACTION_MOVE事件");
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                Log.i(TAG, "dispatchTouchEvent(EasySwipeMenuLayout.java:259) distanceX: " + distanceX);
                scrollBy((int) distanceX, 0);//滑动使用scrollBy
                //越界修正
                final int scrollX = getScrollX();
                if (scrollX < 0) {//右滑
                    if (!mCanRightSwipe || mLeftView == null) {//不允许右滑时
                        scrollTo(0, 0);
                    } else {//最多滑动LeftMenu的宽度
                        if (Math.abs(scrollX) > mLeftView.getWidth()) {
                            scrollTo(mLeftView.getLeft(), 0);
                        }
                    }
                } else if (scrollX > 0) {//左滑
                    if (!mCanLeftSwipe || mRightView == null) {
                        scrollTo(0, 0);
                    } else {
                        if (scrollX > mRightView.getWidth()) {
                            scrollTo(mRightView.getRight() - mContentView.getRight() - mContentViewLp.rightMargin, 0);
                        }
                    }
                }
                mLastP.set(ev.getRawX(), ev.getRawY());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "dispatchTouchEvent ACTION_CANCEL OR ACTION_UP getStartX: " + mScroller.getStartX());

                finallyDistanceX = mFirstP.x - ev.getRawX();
                Log.i(TAG, "dispatchTouchEvent finallyDistanceX:" + finallyDistanceX);
                refreshMenuState(getCurrMenuState(finallyDistanceX));
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onInterceptTouchEvent(EasySwipeMenuLayout.java:301) ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动时拦截点击事件
                if (mCurrMenuState == MENU_LEFT_OPENED || mCurrMenuState == MENU_RIGHT_OPENED) {
                    // 当手指拖动值大于mScaledTouchSlop值时，认为应该进行滚动，拦截子控件的事件
                    Log.i(TAG, "onInterceptTouchEvent(EasySwipeMenuLayout.java:308) ACTION_MOVE true");
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "onInterceptTouchEvent(EasySwipeMenuLayout.java:314) ACTION_UP/ACTION_CANCEL");
                //滑动后不触发contentView的点击事件
                if (mCurrMenuState == MENU_LEFT_OPENED || mCurrMenuState == MENU_RIGHT_OPENED) {
                    Log.i(TAG, "onInterceptTouchEvent(EasySwipeMenuLayout.java:308) ACTION_UP/ACTION_CANCEL true");
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    /**
     * 更新状态
     */
    private void refreshMenuState(@MENU_STATE int menuState) {
        if (menuState == MENU_LEFT_OPENED) {
            mScroller.startScroll(getScrollX(), 0, mLeftView.getLeft() - getScrollX(), 0);
        } else if (menuState == MENU_RIGHT_OPENED) {
            mScroller.startScroll(getScrollX(), 0, mRightView.getRight() - mContentView.getRight() - mContentViewLp.rightMargin - getScrollX(), 0);
        } else {
            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
        }
        invalidate();
        mCurrMenuState = menuState;
    }


    @Override
    public void computeScroll() {
        //判断Scroller是否执行完毕：
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //通知View重绘-invalidate()->onDraw()->computeScroll()
            invalidate();
        }
    }


    /**
     * 根据当前的scrollX的值判断松开手后应处于何种状态
     */
    private int getCurrMenuState(final float distanceX) {
        if (!(Math.abs(distanceX) > mScaledTouchSlop)) {
            return mCurrMenuState;
        }
        final int scrollX = getScrollX();
        if (distanceX < 0) {//右滑,展开左边菜单
            if (mLeftView != null) {
                if (Math.abs(mLeftView.getWidth() * mFraction) < Math.abs(scrollX)) {
                    return MENU_LEFT_OPENED;
                }
            }

        } else if (distanceX > 0) {//左滑，展开右侧菜单
            if (mRightView != null) {
                if (Math.abs(mRightView.getWidth() * mFraction) < Math.abs(scrollX)) {
                    return MENU_RIGHT_OPENED;
                }
            }
        }
        return MENU_CLOSED;
    }


    @Override
    protected void onDetachedFromWindow() {
        refreshMenuState(MENU_CLOSED);
        super.onDetachedFromWindow();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshMenuState(MENU_CLOSED);
    }

    public void resetStatus() {
        if (mCurrMenuState != MENU_CLOSED && mScroller != null) {
            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
            invalidate();
        }
    }


    public float getFraction() {
        return mFraction;
    }

    public void setFraction(float mFraction) {
        this.mFraction = mFraction;
    }

    public boolean isCanLeftSwipe() {
        return mCanLeftSwipe;
    }

    public void setCanLeftSwipe(boolean mCanLeftSwipe) {
        this.mCanLeftSwipe = mCanLeftSwipe;
    }

    public boolean isCanRightSwipe() {
        return mCanRightSwipe;
    }

    public void setCanRightSwipe(boolean mCanRightSwipe) {
        this.mCanRightSwipe = mCanRightSwipe;
    }

    public int getStateCache() {
        return mCurrMenuState;
    }

}
