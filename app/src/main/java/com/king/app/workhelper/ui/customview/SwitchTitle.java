package com.king.app.workhelper.ui.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
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
import android.widget.TextView;

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
    private int mNowSelectedPos = 0;

    /** ViewPager **/
    private ViewPager mViewPager;

    /** OnPageChangeListener **/
    private PageChangeListener mOnPageChange;
    /** 没有ViewPager时候点击事件回调 **/
    private OnClickCallback mOnClickCallBack;
    /** 除文字宽度外的padding值 **/
    private int mExtraPadding = 0;
    /** tabline显示模式，true为短线，false为长线.*/
    private boolean mIsShortTabLine = false;
    /** tab标题 **/
    private List<String> mTitles = new ArrayList<String>();

    /** 下划线左边动画 **/
    private ValueAnimator mLeftAnim;
    /** 下划线右边动画 **/
    private ValueAnimator mRightAnim;
    /** 画tab下划线的paint **/
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /** 没有ViewPager时候点击Tab回调 **/
    public interface OnClickCallback {
        /** 点击tab回调 @param pos 点击位置 **/
        void onTitleClick(int pos);
    }

    /** ViewPager的OnPageChangeListener **/
    public interface PageChangeListener {
        /** ViewPager pageChange回调 @param position 当前ViewPager位置 **/
        void pageChange(int position);
    }

    /**
     * Constructor
     *
     * @param context
     *            Context
     * @param attrs
     *            AttributeSet
     * @param defStyleAttr
     *            defStyleAttr
     */
    public SwitchTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * Constructor
     *
     * @param context
     *            Context
     * @param attrs
     *            AttributeSet
     */
    public SwitchTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor
     *
     * @param context
     *            Context
     */
    public SwitchTitle(Context context) {
        this(context, null);
    }

    /**
     * 初始化
     *
     * @param context
     *            Context
     * @param attrs
     *            AttributeSet
     * @param defStyleAttr
     *            defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mTabContainer = new TabContainer(context);
        addView(mTabContainer, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        setDrawingCacheBackgroundColor(Color.TRANSPARENT);

        Resources res = context.getResources();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.gjjSwitchTitle, defStyleAttr, 0);
        mTxtActiveColor = a.getColor(R.styleable.gjjSwitchTitle_tabTxtActiveColor,
                res.getColor(R.color.gjjSwitchTitleTxtActive));
        mTxtDisableColor = a.getColor(R.styleable.gjjSwitchTitle_tabTxtDisableColor,
                res.getColor(R.color.gjjSwitchTitleTxtDisable));
        mTabLineHeight = a.getDimensionPixelSize(R.styleable.gjjSwitchTitle_tabLineHeight,
                res.getDimensionPixelSize(R.dimen.SwitchTitleLineHeight));
        mTabLineColor = a.getColor(R.styleable.gjjSwitchTitle_tabLineColor, res.getColor(R.color.gjjSwitchTitleLine));
        mTxtSize = a.getDimensionPixelSize(R.styleable.gjjSwitchTitle_tabTxtSize,
                res.getDimensionPixelSize(R.dimen.SwitchTitleTxtSize));
        mTabPadding = a.getDimensionPixelSize(R.styleable.gjjSwitchTitle_tabsPadding,
                res.getDimensionPixelSize(R.dimen.SwitchTitleTabsPadding));
        mIsShortTabLine = a.getBoolean(R.styleable.gjjSwitchTitle_tabIsShortLine, false);
        mLineTopIndent = -a.getDimensionPixelSize(R.styleable.gjjSwitchTitle_tabLineTopIndent, 0);

        mTabContainer.setPadding(0, 0, 0, mLineTopIndent);
        mExtraPadding = mIsShortTabLine ? mTabPadding : mTabPadding / 2;
        a.recycle();

        mPaint.setColor(mTabLineColor);
        mPaint.setStyle(Style.FILL);
        initAnimations();
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
            View selTab = mTabContainer.getChildAt(mNowSelectedPos);
            mLineLeft = selTab.getLeft();
            mLineRight = selTab.getRight();
            mTabContainer.invalidate();
        }
    }

    /**
     * 根据title添加tab
     *
     * @param titles
     *            tab标题
     */
    private void addTabs(List<String> titles) {
        mExtraPadding = mIsShortTabLine ? mTabPadding : mTabPadding / 2;
        mTabContainer.removeAllViews();
        for (int i = 0; i < titles.size(); i++) {
            TabText tt = new TabText(getContext(), i);
            tt.setText(titles.get(i));
            if (mNowSelectedPos == i) {
                tt.setTextColor(mTxtActiveColor);
            } else {
                tt.setTextColor(mTxtDisableColor);
            }
            tt.setOnClickListener(this);
            mTabContainer.addView(tt, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }
    }

    public TabContainer getTabContainer() {
        return mTabContainer;
    }

    public void setBadgeView(int position, String text) {
        mTabContainer.getChildAt(position);
        BadgeTextView view = new BadgeTextView(getContext());
        view.setBadgeText(text);
    }

    /**
     * 获取当前页位置
     *
     * @return 当前页位置
     */
    public int getNowPageId() {
        return mNowSelectedPos;
    }

    /**
     * 获取当前页标题
     *
     * @return 当前页标题
     */
    public CharSequence getNowPageTitle() {
        return mTitles.get(mNowSelectedPos);
    }

    /**
     * 设置当前title选中的位置
     *
     * @param pos
     *            位置
     */
    public void refreshPos(final int pos) {
        if (mTitles.size() == 0) {
            mNowSelectedPos = pos;
            return;
        }

        int toPos = Math.max(0, Math.min(pos, mTitles.size() - 1));

        if (mNowSelectedPos != toPos) {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(toPos);
            } else {
                TabText src = ((TabText) mTabContainer.getChildAt(mNowSelectedPos));
                TabText dest = (TabText) mTabContainer.getChildAt(toPos);
                runAnimation(src, dest);

                if (mOnClickCallBack != null) {
                    mOnClickCallBack.onTitleClick(mNowSelectedPos);
                }
            }
        }
    }

    /**
     * 设置标题
     *
     * @param titles
     *            tab标题
     * @param vp
     *            ViewPager
     * @param listener
     *            ViewPager 的OnPageChangeListener
     */
    public void setParams(ViewPager vp, List<String> titles, PageChangeListener listener) {
        mTitles.clear();
        if (titles == null) { // 默认
            mTitles.add("普通公积金");
            mTitles.add("补充公积金");
        } else {
            mTitles.addAll(titles);
        }
        if (vp == null || mTitles.size() == 0) {
            throw new IllegalArgumentException();
        }

        mViewPager = vp;
        mNowSelectedPos = Math.max(0, Math.min(mNowSelectedPos, mTitles.size() - 1));
        addTabs(mTitles);
        mOnPageChange = listener;
        mOnClickCallBack = null;

        mViewPager.setCurrentItem(mNowSelectedPos);
        mViewPager.addOnPageChangeListener(new MOnPageChangeListener());
    }

    /**
     * 设置独立的参数，与viewpager 无关
     *
     * @param titles
     *            titles
     * @param cl
     *            click callback
     */
    public void setParams(List<String> titles, OnClickCallback cl) {
        if (null != titles && titles.size() != 0) {
            mTitles.clear();
            mTitles.addAll(titles);
        } else {
            return;
        }
        mNowSelectedPos = Math.max(0, Math.min(mNowSelectedPos, mTitles.size() - 1));
        addTabs(mTitles);
        mViewPager = null;
        mOnPageChange = null;
        mOnClickCallBack = cl;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TabText) {
            int pos = ((TabText) v).getPos();
            if (pos == mNowSelectedPos) {
                return;
            }

            if (mViewPager != null) {
                mViewPager.setCurrentItem(pos);
            } else if (mOnClickCallBack != null) {
                refreshPos(pos);
//                mOnClickCallBack.onTitleClick(pos);
            } else {
                refreshPos(pos);
            }
        }
    }

    /**
     * 开始动画
     *
     * @param src
     *            开始tab
     * @param dest
     *            结束tab
     */
    private void runAnimation(TabText src, TabText dest) {
        if (src != dest && getWidth() != 0) { // getWidth=0表示还未调用onMeasure,view还未显示,不用动画
            int scrollPos = dest.getLeft() - (SwitchTitle.this.getWidth() - dest.getWidth()) / 2;
            smoothScrollTo(scrollPos, 0);
            runAnimation(src.getLeft() + mExtraPadding, src.getRight() - mExtraPadding,
                    dest.getLeft() + mExtraPadding, dest.getRight() - mExtraPadding);
        }
        mNowSelectedPos = dest.getPos();
        src.setTextColor(mTxtDisableColor);
        dest.setTextColor(mTxtActiveColor);
    }

    /** 初始化ValueAnimator **/
    private void initAnimations() {
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
     * 开始下划线动画效果
     *
     * @param fromL
     *            下划线开始左边坐标
     * @param fromR
     *            下划线开始右边坐标
     * @param toL
     *            下划线结束左边坐标
     * @param toR
     *            下划线结束右边坐标
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

        /**
         * Constructor
         *
         * @param context
         *            Context
         */
        public TabContainer(Context context) {
            super(context);
            setOrientation(HORIZONTAL);
            setFillViewport(true);
            setWillNotDraw(false);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (getChildCount() > mNowSelectedPos && mLineLeft <= mLineRight) {
                if (mLineRight == 0) { // 初始位置时候重新计算
                    View dest = getChildAt(mNowSelectedPos);
                    mLineLeft = dest.getLeft() + mExtraPadding;
                    mLineRight = dest.getRight() - mExtraPadding;

                    // 若要显示的View未完全显示，则滚动scrollView使其在屏幕中间
//                  if (mLineLeft < getScrollX() || mLineRight > getScrollX() + CaiyiSwtichTitle.this.getWidth()) {
                    int scrollPos = dest.getLeft() - (SwitchTitle.this.getWidth() - dest.getWidth()) / 2;
                    smoothScrollTo(scrollPos, 0);
//                  }
                }
                canvas.drawRect(mLineLeft, getHeight() - mTabLineHeight, mLineRight, getHeight(), mPaint);
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
                    View dest = getChildAt(mNowSelectedPos);
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

    /** tab TextView **/
    public class TabText extends TextView {
        /** 该Tab位置 **/
        private int mPos;

        /**
         * Constructor
         *
         * @param context
         *            Context
         * @param pos
         *            位置
         */
        public TabText(Context context, int pos) {
            super(context);
            mPos = pos;
            init(context);
        }

        /** @return 位置 **/
        private int getPos() {
            return mPos;
        }

        /**
         * 初始化
         *
         * @param context
         *            Context
         **/
        private void init(Context context) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mTxtSize);
            setGravity(Gravity.CENTER);
            setBackgroundResource(R.drawable.gjj_top_click_selector);
            setPadding(mTabPadding, 0, mTabPadding, 0);
        }
    }

    /**
     * ViewPager OnPageChangeListener
     *
     * @author CJL
     * @since 2014年11月17日
     */
    private class MOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            if (mOnPageChange != null) {
                mOnPageChange.pageChange(arg0);
            }
            TabText src = ((TabText) mTabContainer.getChildAt(mNowSelectedPos));
            TabText dest = ((TabText) mTabContainer.getChildAt(arg0));

            runAnimation(src, dest);
        }

        @Override
        public void onPageScrolled(int currentPage, float pageOffset, int offsetPixels) {
            if (mNowSelectedPos > currentPage) { // idx -
                TabText src = ((TabText) mTabContainer.getChildAt(mNowSelectedPos));
                TabText dest = ((TabText) mTabContainer.getChildAt(currentPage));

                src.setTextColor(getStateColor(mTxtDisableColor, mTxtActiveColor, pageOffset));
                dest.setTextColor(getStateColor(mTxtActiveColor, mTxtDisableColor, pageOffset));
            } else if (mNowSelectedPos == currentPage) { //idx +
                if (offsetPixels == 0) { // 稳定状态
                    return;
                }
                TabText src = ((TabText) mTabContainer.getChildAt(mNowSelectedPos));
                TabText dest = ((TabText) mTabContainer.getChildAt(mNowSelectedPos + 1));

                src.setTextColor(getStateColor(mTxtActiveColor, mTxtDisableColor, pageOffset));
                dest.setTextColor(getStateColor(mTxtDisableColor, mTxtActiveColor, pageOffset));
            } else {
                Log.d("---", "<<<<<<");
            }
        }

        /**
         * 获取两个颜色的中间色
         *
         * @param cf
         *            开始颜色
         * @param ct
         *            结束颜色
         * @param pageOffset
         *            进度
         * @return 中间色
         */
        private int getStateColor(int cf, int ct, float pageOffset) {
            int r = (int) (Color.red(cf) + (Color.red(ct) - Color.red(cf)) * pageOffset);
            int g = (int) (Color.green(cf) + (Color.green(ct) - Color.green(cf)) * pageOffset);
            int b = (int) (Color.blue(cf) + (Color.blue(ct) - Color.blue(cf)) * pageOffset);
            return Color.argb(255, r, g, b); // SUPPRESS CHECKSTYLE
        }

        @Override
        public void onPageScrollStateChanged(int status) {
        }
    }
}
