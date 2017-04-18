package com.king.app.workhelper.ui.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
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
public class SwitchTitle extends HorizontalScrollView implements OnClickListener {
    private static final String TAG = "aaa";
    /** 动画持续时间 **/
    private static final int ANIM_DURATION = 300;
    /** 动画延迟时间 **/
    private static final int ANIM_DELAY = 100;

    /** tab 文字选中状态颜色 **/
    private int mActiveTextColor;
    /** tab 文字未选中状态颜色 **/
    private int mDisableTextColor;
    /** tab 下划线高度 **/
    private int mTabLineHeight;
    /** tab 下划线颜色 **/
    private int mTabLineColor;
    /** 文字大小(px) **/
    private int mTabTextSize;
    /** tab 文字padding **/
    private int mTabPadding;

    /** 下划线左边坐标 **/
    private int mLineLeft = 0;
    /** 下划线右边坐标 **/
    private int mLineRight = 0;
    /** 下划线相对于上方文字的缩进值. */
    private int mLineTopIndent = 0;

    /** LinearLayout, 包含所有tab **/
    private TabContainer mTabContainer;

    /** 当前选中的位置 **/
    private int mCurrentPosition = 0;

    /** ViewPager **/
    private ViewPager mViewPager;

    /** 没有ViewPager时候点击事件回调 **/
    private OnTabTitleClickListener mOnTabClickListener;
    /** 除文字宽度外的padding值 **/
    private int mExtraPadding = 0;
    /** tab line显示样式，true为短线，false为长线. */
    private boolean mIsShortTabLine = false;
    /** tab标题 **/
    private List<String> mTabTitles = new ArrayList<>();

    /** 下划线左边动画 **/
    private ValueAnimator mLeftAnim;
    /** 下划线右边动画 **/
    private ValueAnimator mRightAnim;
    /** 画tab下划线的paint **/
    Paint mTabLinePaint;
    
    public SwitchTitle(Context context) {
        this(context, null);
    }

    public SwitchTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }
    
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mTabContainer = new TabContainer(context);
        addView(mTabContainer, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        setDrawingCacheBackgroundColor(Color.TRANSPARENT);

        Resources res = context.getResources();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.gjjSwitchTitle, defStyleAttr, 0);
        mActiveTextColor = a.getColor(R.styleable.gjjSwitchTitle_tabTxtActiveColor,
                res.getColor(R.color.gjjSwitchTitleTxtActive));
        mDisableTextColor = a.getColor(R.styleable.gjjSwitchTitle_tabTxtDisableColor,
                res.getColor(R.color.gjjSwitchTitleTxtDisable));
        mTabLineHeight = a.getDimensionPixelSize(R.styleable.gjjSwitchTitle_tabLineHeight,
                res.getDimensionPixelSize(R.dimen.SwitchTitleLineHeight));
        mTabLineColor = a.getColor(R.styleable.gjjSwitchTitle_tabLineColor, res.getColor(R.color.gjjSwitchTitleLine));
        mTabTextSize = a.getDimensionPixelSize(R.styleable.gjjSwitchTitle_tabTxtSize,
                res.getDimensionPixelSize(R.dimen.SwitchTitleTxtSize));
        mTabPadding = a.getDimensionPixelSize(R.styleable.gjjSwitchTitle_tabsPadding,
                res.getDimensionPixelSize(R.dimen.SwitchTitleTabsPadding));
        Log.i(TAG, "mTabPadding: " + mTabPadding);
        mIsShortTabLine = a.getBoolean(R.styleable.gjjSwitchTitle_tabIsShortLine, false);
        mLineTopIndent = -a.getDimensionPixelSize(R.styleable.gjjSwitchTitle_tabLineTopIndent, 0);

        mTabContainer.setPadding(0, 0, 0, mLineTopIndent);
        mExtraPadding = mIsShortTabLine ? mTabPadding : mTabPadding / 2;
        a.recycle();

        initLinePaint();
        initLineAnimations();
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

        if (mTabContainer != null && mTabContainer.getChildCount() > 0) {
            View selTab = mTabContainer.getChildAt(mCurrentPosition);
            mLineLeft = selTab.getLeft();
            mLineRight = selTab.getRight();
            mTabContainer.invalidate();
        }
    }

    private void initLinePaint() {
        mTabLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTabLinePaint.setColor(mTabLineColor);
        mTabLinePaint.setStyle(Paint.Style.FILL);
    }

    private void initLineAnimations() {
        mLeftAnim = new ValueAnimator();
        mLeftAnim.setDuration(ANIM_DURATION);
        mLeftAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mLeftAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLineLeft = (Integer) animation.getAnimatedValue();
                mTabContainer.invalidate(0, getHeight() - mTabLineHeight, mTabContainer.getWidth(), getHeight());
            }
        });

        mRightAnim = new ValueAnimator();
        mRightAnim.setDuration(ANIM_DURATION);
        mRightAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mRightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLineRight = (Integer) animation.getAnimatedValue();
                mTabContainer.invalidate(0, getHeight() - mTabLineHeight, mTabContainer.getWidth(), getHeight());
            }
        });
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
            TabText tabText = new TabText(getContext(), i);
            tabText.setText(titles.get(i));
            if (mCurrentPosition == i) {
                tabText.setTextColor(mActiveTextColor);
            } else {
                tabText.setTextColor(mDisableTextColor);
            }
            tabText.setOnClickListener(this);
            mTabContainer.addView(tabText, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }
    }

    public TabContainer getTabContainer() {
        return mTabContainer;
    }

    /**
     * 获取当前页位置
     *
     * @return 当前页位置
     */
    public int getNowPageId() {
        return mCurrentPosition;
    }

    /**
     * 获取当前页标题
     *
     * @return 当前页标题
     */
    public CharSequence getNowPageTitle() {
        return mTabTitles.get(mCurrentPosition);
    }

    /**
     * 设置标题
     *
     * @param titles    tab标题
     * @param viewPager ViewPager
     * @param listener  ViewPager 的OnPageChangeListener
     */
    public void setParams(ViewPager viewPager, List<String> titles, OnTabTitleClickListener listener) {
        if (viewPager == null || titles == null || titles.isEmpty()) {
            throw new IllegalArgumentException("viewPager 或 title不能为null");
        }
        mTabTitles.clear();
        mTabTitles.addAll(titles);
        addTabs(mTabTitles);

        mOnTabClickListener = listener;
        mViewPager = viewPager;

        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.addOnPageChangeListener(new TabPageChangeListener());
    }

    /**
     * 设置独立的参数，与viewpager 无关
     *
     * @param titles titles
     * @param cl     click callback
     */
    public void setParams(List<String> titles, OnTabTitleClickListener cl) {
        if (titles == null || titles.isEmpty()) {
            throw new IllegalArgumentException("title不能为null");
        }
        mTabTitles.clear();
        mTabTitles.addAll(titles);

        addTabs(mTabTitles);
        mViewPager = null;
        mOnTabClickListener = cl;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TabText) {
            int newPosition = ((TabText) v).getPos();
            if (newPosition == mCurrentPosition) {
                return;
            }
            if (mViewPager != null) {
                mViewPager.setCurrentItem(newPosition);
            }
            changePosition(newPosition);
        }
    }

    private void changePosition(int newPosition) {
        TabText src = ((TabText) mTabContainer.getChildAt(mCurrentPosition));
        TabText dest = (TabText) mTabContainer.getChildAt(newPosition);
        runAnimation(src, dest);
        mCurrentPosition = newPosition;
        if (mOnTabClickListener != null) {
            mOnTabClickListener.onTabTitleClick(newPosition);
        }
    }

    /**
     * 开始动画
     *
     * @param srcTabText 开始tab
     * @param desTabText 结束tab
     */
    private void runAnimation(TabText srcTabText, TabText desTabText) {
        if (srcTabText != desTabText && getWidth() != 0) { // getWidth=0表示还未调用onMeasure,view还未显示,不用动画
            int scrollPos = desTabText.getLeft() - (SwitchTitle.this.getWidth() - desTabText.getWidth()) / 2;
            smoothScrollTo(scrollPos, 0);
            runAnimation(srcTabText.getLeft() + mExtraPadding, srcTabText.getRight() - mExtraPadding,
                    desTabText.getLeft() + mExtraPadding, desTabText.getRight() - mExtraPadding);
        }
        srcTabText.setTextColor(mDisableTextColor);
        desTabText.setTextColor(mActiveTextColor);
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
    public class TabContainer extends LinearLayout {

        public TabContainer(Context context) {
            super(context);
            setOrientation(HORIZONTAL);
            setFillViewport(true);//让子View的高度填满父元素
            setWillNotDraw(false);//设置绘制View
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (getChildCount() > mCurrentPosition && mLineLeft <= mLineRight) {
                if (mLineRight == 0) { // 初始位置时候重新计算
                    TabText dest = (TabText)getChildAt(mCurrentPosition);
                    mLineLeft = dest.getLeft() + mExtraPadding;
                    mLineRight = dest.getRight() - mExtraPadding;

                    // 若要显示的View未完全显示，则滚动scrollView使其在屏幕中间
                    int scrollPos = dest.getLeft() - (SwitchTitle.this.getWidth() - dest.getWidth()) / 2;
                    smoothScrollTo(scrollPos, 0);
                }
                canvas.drawRect(mLineLeft, getHeight() - mTabLineHeight, mLineRight, getHeight(), mTabLinePaint);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            if (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else if (widthMode == MeasureSpec.EXACTLY && getChildCount() > 0) {
                // MeasureSpec.EXACTLY 仅当所有tab宽度之和小于屏幕宽度时候会调用，此处调整加大每个tab宽度

                final int w = MeasureSpec.getSize(widthMeasureSpec);
                final int childCount = getChildCount();
                int exPadding = (w - getMeasuredWidth()) / childCount;

                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);

                    int childHMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                            getPaddingTop() + getPaddingBottom(), child.getLayoutParams().height);
                    int childWMeasureSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredWidth() + exPadding,
                            MeasureSpec.EXACTLY);
                    child.measure(childWMeasureSpec, childHMeasureSpec);
                }

                setMeasuredDimension(w, MeasureSpec.getSize(heightMeasureSpec));

                // 动画时候会自动刷新位置，不用调整。若动画进行时，此处invalidate会导致下划线闪到终点再回来继续动画
                if (exPadding != 0 && !mLeftAnim.isRunning() && !mRightAnim.isRunning()) {
                    mExtraPadding = exPadding / 2 + (mIsShortTabLine ? mTabPadding : mTabPadding / 2);
                    View dest = getChildAt(mCurrentPosition);
                    int left = dest.getLeft() + mExtraPadding;
                    if (left != mLineLeft) { // 由于是均分的，因此判断左边就行了
                        mLineLeft = left;
                        mLineRight = dest.getRight() - mExtraPadding;
                        postInvalidate(dest.getLeft(), getHeight() - mTabLineHeight, dest.getRight(), getHeight());
                    }
                }
            }
        }
    }

    /** tab 上的文字 **/
    private class TabText extends android.support.v7.widget.AppCompatTextView {
        /** 该Tab位置 **/
        private int mPos;

        public TabText(Context context, int pos) {
            super(context);
            mPos = pos;
            init();
        }

        private void init() {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize);
            setGravity(Gravity.CENTER);
            setBackgroundResource(R.drawable.gjj_top_click_selector);
            setPadding(mTabPadding, 0, mTabPadding, 0);
        }

        private int getPos() {
            return mPos;
        }
    }

    /**
     * ViewPager OnPageChangeListener
     *
     * @author CJL
     * @since 2014年11月17日
     */
    private class TabPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            changePosition(position);
        }

        @Override
        public void onPageScrolled(int currentPage, float pageOffset, int offsetPixels) {
            if (mCurrentPosition > currentPage) { // idx -
                TabText src = ((TabText) mTabContainer.getChildAt(mCurrentPosition));
                TabText dest = ((TabText) mTabContainer.getChildAt(currentPage));

                src.setTextColor(getStateColor(mDisableTextColor, mActiveTextColor, pageOffset));
                dest.setTextColor(getStateColor(mActiveTextColor, mDisableTextColor, pageOffset));
            } else if (mCurrentPosition == currentPage) { //idx +
                if (offsetPixels == 0) { // 稳定状态
                    return;
                }
                TabText src = ((TabText) mTabContainer.getChildAt(mCurrentPosition));
                TabText dest = ((TabText) mTabContainer.getChildAt(mCurrentPosition + 1));

                src.setTextColor(getStateColor(mActiveTextColor, mDisableTextColor, pageOffset));
                dest.setTextColor(getStateColor(mDisableTextColor, mActiveTextColor, pageOffset));
            }
        }

        @Override
        public void onPageScrollStateChanged(int status) {
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
            return Color.argb(255, r, g, b); // SUPPRESS CHECKSTYLE
        }
    }

    public interface OnTabTitleClickListener {
        void onTabTitleClick(int position);
    }
}
