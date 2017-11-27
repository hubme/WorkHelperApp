package com.king.app.workhelper.ui.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.king.app.workhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 新版switchTitle，更改了实现方式与动画效果，显示更流畅
 *
 * @author CJL
 * @since 2014年11月17日
 */
public class SwitchTitle extends HorizontalScrollView {
    /** 动画持续时间 **/
    private static final int ANIM_DURATION = 300;
    /** 动画延迟时间 **/
    private static final int ANIM_DELAY = 100;

    /** tab 文字选中状态颜色 **/
    private int mTxtActiveColor;
    /** tab 文字未选中状态颜色 **/
    private int mTxtDisableColor;
    /** tab 下划线高度 **/
    private int mTabLineHeight;
    /** tab 下划线颜色 **/
    private int mTabLineColor;
    /** 文字大小(px) **/
    private int mTxtSize;
    /** 下划线左边坐标 **/
    private int mLineLeft = 0;
    /** 下划线右边坐标 **/
    private int mLineRight = 0;
    /** 下划线相对于上方文字的缩进值. */
    private int mLineTopIndent = 0;
    /** 下划线的长度的偏移量 */
    private float mLinePaddingFraction;
    /** 每个 tab 的 左边和右边的 padding */
    private int mTabPadding;

    /** LinearLayout, 包含所有tab **/
    private final TabContainer mTabContainer;

    /** 当前选中的位置 **/
    private int mCurrentPosition = 0;

    private ViewPager mViewPager;
    private OnTabClickListener mOnTabClickListener;

    /** tab标题 **/
    private final List<String> mTabTitles = new ArrayList<>();

    /** 下划线左边动画 **/
    private ValueAnimator mLeftAnim;
    /** 下划线右边动画 **/
    private ValueAnimator mRightAnim;
    /** 画tab下划线的paint **/
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public SwitchTitle(Context context) {
        this(context, null);
    }

    public SwitchTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SwitchTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);

        mTabContainer = new TabContainer(context);
        mTabContainer.setPadding(0, 0, 0, mLineTopIndent);
        addView(mTabContainer, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setDrawingCacheBackgroundColor(Color.TRANSPARENT);

        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwitchTitle, defStyleAttr, 0);
        mTxtActiveColor = typedArray.getColor(R.styleable.SwitchTitle_tabTxtActiveColor, Color.parseColor("#ff27A5E2"));
        mTxtDisableColor = typedArray.getColor(R.styleable.SwitchTitle_tabTxtDisableColor, Color.parseColor("#ffb0b0b0"));
        mTabLineHeight = typedArray.getDimensionPixelSize(R.styleable.SwitchTitle_tabLineHeight, dpToPx(2));
        mTabLineColor = typedArray.getColor(R.styleable.SwitchTitle_tabLineColor, Color.BLACK);
        mTxtSize = typedArray.getDimensionPixelSize(R.styleable.SwitchTitle_tabTxtSize, sp2px(17));
        mLinePaddingFraction = typedArray.getFraction(R.styleable.SwitchTitle_tabLinePaddingFraction, 1, 1, 0f);
        mTabPadding = typedArray.getDimensionPixelSize(R.styleable.SwitchTitle_tabPadding, dpToPx(5));
        mLineTopIndent = -typedArray.getDimensionPixelSize(R.styleable.SwitchTitle_tabLineTopIndent, 0);
        typedArray.recycle();

        initPaint();
        initAnimations();
    }

    private void initPaint() {
        mPaint.setColor(mTabLineColor);
        mPaint.setStyle(Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() > 0 && mTabContainer.getChildCount() > 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int w = getMeasuredWidth();
            if (mTabContainer.getMeasuredWidth() < w) {
                int childHMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(),
                        mTabContainer.getLayoutParams().height);
                int childWMeasureSpec = MeasureSpec.makeMeasureSpec(w - getPaddingLeft() - getPaddingRight(),
                        MeasureSpec.EXACTLY);
                mTabContainer.measure(childWMeasureSpec, childHMeasureSpec);
            }
        } else {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mTabContainer.getChildCount() > 0) {
            View selTab = mTabContainer.getChildAt(mCurrentPosition);
            mLineLeft = selTab.getLeft();
            mLineRight = selTab.getRight();
            mTabContainer.invalidate();
        }
    }

    /**
     * 根据title添加tab
     *
     * @param titles tab标题
     */
    private void addTabs(List<String> titles) {
        if (mTabContainer.getChildCount() > 0) {
            mTabContainer.removeAllViews();
        }

        for (int i = 0; i < titles.size(); i++) {
            final TabSwitchTitle tabTitle = new TabSwitchTitle(getContext());
            tabTitle.setPadding(mTabPadding, 0, mTabPadding, 0);
            tabTitle.setPosition(i);
            tabTitle.setText(titles.get(i));
            tabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTxtSize);
            if (mCurrentPosition == i) {
                tabTitle.setTextColor(mTxtActiveColor);
            } else {
                tabTitle.setTextColor(mTxtDisableColor);
            }
            tabTitle.setOnClickListener(new OnClickListener() {
                @Override public void onClick(View v) {
                    final int position = tabTitle.getPosition();
                    if (position >= 0 && position < mTabTitles.size() && mCurrentPosition != position) {
                        refreshPosition(position);
                    }
                }
            });
            mTabContainer.addView(tabTitle, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }
    }

    /**
     * 获取当前页位置
     *
     * @return 当前页位置
     */
    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    /**
     * 获取当前页标题
     *
     * @return 当前页标题
     */
    public CharSequence getCurrentTabTitle() {
        return mTabTitles.get(mCurrentPosition);
    }

    /**
     * 设置当前title选中的位置
     *
     * @param desPos 目标位置
     */
    public void refreshPosition(final int desPos) {
        if (desPos < 0 || desPos > mTabTitles.size() - 1 || mCurrentPosition == desPos) {
            return;
        }
        TabSwitchTitle src = ((TabSwitchTitle) mTabContainer.getChildAt(mCurrentPosition));
        TabSwitchTitle dest = (TabSwitchTitle) mTabContainer.getChildAt(desPos);
        src.setTextColor(mTxtDisableColor);
        dest.setTextColor(mTxtActiveColor);

        int scrollPos = dest.getLeft() - (getWidth() - dest.getWidth()) / 2;
        smoothScrollTo(scrollPos, 0);
        final int leftTabHalfWidth = src.getMeasuredWidth() / 2;
        final int rightTabHalfWidth = dest.getMeasuredWidth() / 2;
        runAnimation(src.getLeft() + (int) (mLinePaddingFraction * leftTabHalfWidth), src.getRight() - (int) (mLinePaddingFraction * leftTabHalfWidth),
                dest.getLeft() + (int) (mLinePaddingFraction * rightTabHalfWidth), dest.getRight() - (int) (mLinePaddingFraction * rightTabHalfWidth));

        if (mOnTabClickListener != null) {
            mOnTabClickListener.onTabClicked(desPos, dest);
        }

        if (mViewPager != null) {
            mViewPager.setCurrentItem(desPos);//会调用refreshPosition方法更新tab
        }
        mCurrentPosition = desPos;
    }

    public void setParams(ViewPager viewPager, List<String> titles) {
        if (viewPager == null) {
            throw new IllegalArgumentException("ViewPager must not be null");
        }
        if (titles == null || titles.isEmpty()) {
            return;
        }
        if (!mTabTitles.isEmpty()) {
            mTabTitles.clear();
        }
        mTabTitles.addAll(titles);
        addTabs(mTabTitles);

        mViewPager = viewPager;
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener());
    }

    public void setTabTitles(List<String> titles) {
        if (titles == null || titles.isEmpty()) {
            return;
        }
        if (!mTabTitles.isEmpty()) {
            mTabTitles.clear();
        }
        mTabTitles.addAll(titles);
        addTabs(mTabTitles);
    }

    public void setViewPager(ViewPager viewPager) {
        if (viewPager == null) {
            throw new IllegalArgumentException("ViewPager must not be null");
        }
        mViewPager = viewPager;
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener());
    }

    /** 初始化ValueAnimator **/
    private void initAnimations() {
        mLeftAnim = new ValueAnimator();
        mLeftAnim.setDuration(ANIM_DURATION);
        mLeftAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mLeftAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                mLineLeft = (Integer) animation.getAnimatedValue();
                mTabContainer.invalidate(0, getHeight() - mTabLineHeight, mTabContainer.getWidth(), getHeight());
            }
        });

        mRightAnim = new ValueAnimator();
        mRightAnim.setDuration(ANIM_DURATION);
        mRightAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mRightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                mLineRight = (Integer) animation.getAnimatedValue();
                mTabContainer.invalidate(0, getHeight() - mTabLineHeight, mTabContainer.getWidth(), getHeight());
            }
        });
    }

    /**
     * 开始下划线动画效果
     *
     * @param fromL 下划线开始左边坐标
     * @param fromR 下划线开始右边坐标
     * @param toL   下划线结束左边坐标
     * @param toR   下划线结束右边坐标
     */
    private void runAnimation(final int fromL, final int fromR, final int toL, final int toR) {
        if (mLeftAnim.isRunning()) {
            mLeftAnim.cancel();
        }
        if (mRightAnim.isRunning()) {
            mRightAnim.cancel();
        }

        mLeftAnim.setIntValues(fromL, toL);
        mRightAnim.setIntValues(fromR, toR);
        if (fromL < toL) {
            mLeftAnim.setStartDelay(ANIM_DELAY);
            mRightAnim.setStartDelay(0);
        } else {
            mLeftAnim.setStartDelay(0);
            mRightAnim.setStartDelay(ANIM_DELAY);
        }
        mLeftAnim.start();
        mRightAnim.start();
    }

    /** tab 容器， 选中的项底部含有下划线 **/
    private class TabContainer extends LinearLayout {
        public TabContainer(Context context) {
            super(context);

            setOrientation(HORIZONTAL);
            setFillViewport(true);
            setWillNotDraw(false);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            if (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else if (widthMode == MeasureSpec.EXACTLY && getChildCount() > 0) {
                // MeasureSpec.EXACTLY 仅当所有tab宽度之和小于屏幕宽度时候会调用，此处调整加大每个tab宽度
                final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
                final int childCount = getChildCount();
                int exPadding = (widthSize - getMeasuredWidth()) / childCount;

                for (int i = 0; i < childCount; i++) {
                    final View child = getChildAt(i);
                    int childHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                            getPaddingTop() + getPaddingBottom(), child.getLayoutParams().height);
                    int childWidthSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredWidth() + exPadding, MeasureSpec.EXACTLY);
                    child.measure(childWidthSpec, childHeightSpec);
                }

                setMeasuredDimension(widthSize, MeasureSpec.getSize(heightMeasureSpec));
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (mCurrentPosition < getChildCount() && mLineLeft <= mLineRight) {
                if (mLineRight == 0) { // 初始位置时候重新计算
                    View dest = getChildAt(mCurrentPosition);
                    mLineLeft = dest.getLeft() + (int) (mLinePaddingFraction * dest.getMeasuredWidth() / 2);
                    mLineRight = dest.getRight() - (int) (mLinePaddingFraction * dest.getMeasuredWidth() / 2);

                    // 若要显示的View未完全显示，则滚动scrollView使其在屏幕中间
                    int scrollPos = dest.getLeft() - (getWidth() - dest.getWidth()) / 2;
                    smoothScrollTo(scrollPos, 0);
                }
                canvas.drawRect(mLineLeft, getHeight() - mTabLineHeight, mLineRight, getHeight(), mPaint);
            }
        }
    }

    public TabSwitchTitle getTabText(int position) {
        if (position < 0 || position > mTabContainer.getChildCount() - 1) {
            return null;
        }
        return (TabSwitchTitle) mTabContainer.getChildAt(position);
    }

    private class OnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            refreshPosition(position);
        }

        @Override
        public void onPageScrolled(int currentPage, float pageOffset, int offsetPixels) {
            /*if (currentPage < mCurrentPosition) {//左滑,即使滑动一点currentPage也会减小。但是右滑不会
                TabSwitchTitle src = ((TabSwitchTitle) mTabContainer.getChildAt(mCurrentPosition));
                TabSwitchTitle dest = ((TabSwitchTitle) mTabContainer.getChildAt(currentPage));

                src.setTextColor(getStateColor(mTxtDisableColor, mTxtActiveColor, pageOffset));
                dest.setTextColor(getStateColor(mTxtActiveColor, mTxtDisableColor, pageOffset));
            } else {//右滑和静止状态
                TabSwitchTitle src = ((TabSwitchTitle) mTabContainer.getChildAt(mCurrentPosition));
                Log.i("aaa", src.getText().toString());
                TabSwitchTitle dest = ((TabSwitchTitle) mTabContainer.getChildAt(mCurrentPosition + 1));

                if (offsetPixels == 0) { //静止状态
                    return;
                }
                //右滑
                src.setTextColor(getStateColor(mTxtActiveColor, mTxtDisableColor, pageOffset));
                dest.setTextColor(getStateColor(mTxtDisableColor, mTxtActiveColor, pageOffset));
                if (currentPage != mCurrentPosition) {
                    src.setTextColor(getStateColor(mTxtActiveColor, mTxtDisableColor, pageOffset));
                }
            }*/
            if (offsetPixels == 0) { //静止状态
                TabSwitchTitle src = ((TabSwitchTitle) mTabContainer.getChildAt(mCurrentPosition));
                TabSwitchTitle dest = ((TabSwitchTitle) mTabContainer.getChildAt(currentPage));
                src.setTextColor(mTxtDisableColor);
                dest.setTextColor(mTxtActiveColor);
            }
        }

        /**
         * 获取两个颜色的中间色
         *
         * @param cf         开始颜色
         * @param ct         结束颜色
         * @param pageOffset 进度
         * @return 中间色
         */
        private int getStateColor(int cf, int ct, float pageOffset) {
            int r = (int) (Color.red(cf) + (Color.red(ct) - Color.red(cf)) * pageOffset);
            int g = (int) (Color.green(cf) + (Color.green(ct) - Color.green(cf)) * pageOffset);
            int b = (int) (Color.blue(cf) + (Color.blue(ct) - Color.blue(cf)) * pageOffset);
            return Color.argb(255, r, g, b);
        }
    }

    public void setOnTabClickListener(OnTabClickListener listener) {
        mOnTabClickListener = listener;
    }

    public interface OnTabClickListener {
        void onTabClicked(int position, TabSwitchTitle tab);
    }

    public static class TabSwitchTitle extends android.support.v7.widget.AppCompatTextView {
        private int mPosition;

        public TabSwitchTitle(Context context) {
            this(context, null);
        }

        public TabSwitchTitle(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TabSwitchTitle(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        public TabSwitchTitle setPosition(int position) {
            if (position > 0) {
                mPosition = position;
            }
            return this;
        }

        public int getPosition() {
            return mPosition;
        }

        private void init() {
            setGravity(Gravity.CENTER);
            setBackgroundResource(R.drawable.bg_transparent);
        }
    }

    private int dpToPx(int dpValue) {
        return Math.round(getResources().getDisplayMetrics().density * dpValue);
    }

    private int sp2px(float spValue) {
        return Math.round(getResources().getDisplayMetrics().scaledDensity * spValue);
    }
}
