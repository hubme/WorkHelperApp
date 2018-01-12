package com.king.applib.simplebanner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.king.applib.simplebanner.listener.OnBannerClickListener;
import com.king.applib.simplebanner.loader.BannerInterface;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 轮播图
 *
 * @author VanceKing
 * @since 2017/5/9.
 */

/*
假设原始界面是:A、B、C.则构造成C、A、B、C、A.当position==0(C界面)时，设置setCurrentItem==3(也是C界面),可以向右滑.
当position==4(A)时，设置setCurrentItem==1(也是A界面),可以向左滑,达到循环的目的.
 */
public class SimpleBanner<T, V extends View> extends FrameLayout implements ViewPager.OnPageChangeListener {
    private static final int DEFAULT_SCROLL_DURATION = 1000;
    private static final int DEFAULT_DELAY_DURATION = 3000;
    private int mIndicatorSize = 6;//dip
    private int mIndicatorMargin = 6;//dip
    @ColorInt
    private int mSelectedIndicatorColor = Color.parseColor("#77000000");
    @ColorInt
    private int mUnselectedIndicatorColor = Color.parseColor("#88ffffff");

    private BannerViewPager mBanner;
    private BannerAdapter mBannerAdapter;
    private int mBannerCount;
    private BannerLoopTask mLoopTask;

    private BannerInterface<T, V> mBannerLoader;
    private LinearLayout mIndicatorPanel;
    private final List<View> mIndicatorViews = new ArrayList<>();
    private final List<View> mBannerViews = new ArrayList<>();
    private ShapeDrawable mSelectedDrawable;
    private ShapeDrawable mUnSelectedDrawable;

    public SimpleBanner(Context context) {
        this(context, null);
    }

    public SimpleBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        initData();
        initViewPagerScroll();
    }

    private void initView() {
        mBanner = buildViewPager();
        addView(mBanner);

        mIndicatorPanel = buildIndicatorPanel();
        addView(mIndicatorPanel);
    }

    private void initData() {
        mBannerAdapter = new BannerAdapter();
        mBanner.setAdapter(mBannerAdapter);

        mLoopTask = new BannerLoopTask();

        mSelectedDrawable = getIndicatorDrawable();
        mUnSelectedDrawable = getIndicatorDrawable();
    }

    private BannerViewPager buildViewPager() {
        BannerViewPager banner = new BannerViewPager(getContext());
        banner.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return banner;
    }

    private LinearLayout buildIndicatorPanel() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        layoutParams.bottomMargin = dp2px(mIndicatorMargin);

        LinearLayout indicatorPanel = new LinearLayout(getContext());
        indicatorPanel.setOrientation(LinearLayout.HORIZONTAL);
        indicatorPanel.setLayoutParams(layoutParams);

        return indicatorPanel;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                startLoop();
                break;
            case MotionEvent.ACTION_DOWN:
                stopLoop();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoop();
    }

    /**
     * 放在最后执行
     */
    public void updateBanner(List<T> images) {
        if (mBannerLoader == null) {
            throw new NullPointerException("please set up a BannerInterface.");
        }
        if (images == null || images.isEmpty()) {
            return;
        }

        generateBannerView(images);
        mBannerAdapter.update(mBannerViews);
        mBannerCount = mBannerAdapter.getCount();
        setIndicator();
        setBanner();
        startLoop();
    }

    //构造界面。A、B、C--->C、A、B、C、A
    private void generateBannerView(List<T> images) {
        if (images == null || images.isEmpty()) {
            return;
        }
        if (!mBannerViews.isEmpty()) {
            mBannerViews.clear();
        }

        if (images.size() == 1) {
            mBannerViews.add(buildBannerView(images.get(0)));
        } else {
            //先添加最后一个
            mBannerViews.add(buildBannerView(images.get(images.size() - 1)));

            for (T banner : images) {
                mBannerViews.add(buildBannerView(banner));
            }

            //最后添加第一个
            mBannerViews.add(buildBannerView(images.get(0)));
        }
    }

    private View buildBannerView(T t) {
        final V view = mBannerLoader.createBannerView(getContext());
        mBannerLoader.displayBanner(getContext(), t, view);
        return view;
    }

    private void setIndicator() {
        mSelectedDrawable.getPaint().setColor(mSelectedIndicatorColor);
        mUnSelectedDrawable.getPaint().setColor(mUnselectedIndicatorColor);
        buildIndicatorViews();
        setupIndicator();
        updateIndicator(0);
    }

    private void setBanner() {
        if (mBannerCount > 1) {
            mBanner.addOnPageChangeListener(this);
            mBanner.setScrollable(true);
            mBanner.setCurrentItem(1, false);
        } else {
            mBanner.setScrollable(false);
            mBanner.setCurrentItem(0, false);
        }
    }

    public void startLoop() {
        removeCallbacks(mLoopTask);
        if (mBannerCount <= 1) {
            return;
        }
        postDelayed(mLoopTask, DEFAULT_DELAY_DURATION);
    }

    public void stopLoop() {
        removeCallbacks(mLoopTask);
    }

    private class BannerLoopTask implements Runnable {

        @Override
        public void run() {
            int mCurrentItem = mBanner.getCurrentItem();
            mCurrentItem = (mCurrentItem + 1) % mBannerCount;

            mBanner.setCurrentItem(mCurrentItem);
            postDelayed(mLoopTask, DEFAULT_DELAY_DURATION);
        }
    }

    public SimpleBanner<T, V> setBannerLoader(BannerInterface<T, V> imageLoader) {
        mBannerLoader = imageLoader;
        return this;
    }

    private ShapeDrawable getIndicatorDrawable() {
        OvalShape ovalShape = new OvalShape();
        return new ShapeDrawable(ovalShape);
    }

    public SimpleBanner<T, V> setSelectedIndicatorColor(@ColorInt int color) {
        mSelectedIndicatorColor = color;
        return this;
    }

    public SimpleBanner<T, V> setUnSelectedIndicatorColor(@ColorInt int color) {
        mUnselectedIndicatorColor = color;
        return this;
    }

    public SimpleBanner<T, V> setIndicatorMargin(int marginInDp) {
        if (marginInDp > 0) {
            mIndicatorMargin = marginInDp;
        }
        return this;
    }

    public SimpleBanner<T, V> setIndicatorSize(int sizeInDp) {
        mIndicatorSize = sizeInDp;
        return this;
    }

    private void buildIndicatorViews() {
        if (!mIndicatorViews.isEmpty()) {
            mIndicatorViews.clear();
        }
        final int size = dp2px(mIndicatorSize);
        LinearLayout.LayoutParams firstParams = new LinearLayout.LayoutParams(size, size);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.leftMargin = dp2px(mIndicatorMargin);
        for (int i = 0; i < mBannerCount - 2; i++) {//由于多生成2个View，要减去
            View view = new View(getContext());
            if (i == 0) {
                view.setLayoutParams(firstParams);
            } else {
                view.setLayoutParams(params);
            } 
            setViewBackground(view, mUnSelectedDrawable);
            mIndicatorViews.add(view);
        }
    }

    private void setupIndicator() {
        //避免更新数据后View重复
        if (mIndicatorPanel.getChildCount() > 0) {
            mIndicatorPanel.removeAllViews();
        }
        for (View view : mIndicatorViews) {
            mIndicatorPanel.addView(view);
        }
    }

    private void updateIndicator(int position) {
        final int count = mIndicatorViews.size();
        if (position < 0 || position > count - 1) {
            return;
        }
        for (int i = 0; i < count; i++) {
            final View view = mIndicatorViews.get(i);
            if (i == position) {
                setViewBackground(view, mSelectedDrawable);
            } else {
                setViewBackground(view, mUnSelectedDrawable);
            }
        }
    }

    //获取映射后Banner的下标
    private int getFakePosition(int realPosition) {
        return realPosition - 1;
    }

    private void setViewBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            ViewPagerScroller mScroller = new ViewPagerScroller(getContext());
            mScroller.setDuration(DEFAULT_SCROLL_DURATION);
            mField.set(mBanner, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        updateIndicator(getFakePosition(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        int mCurrentItem = mBanner.getCurrentItem();
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE://界面完全停止时，偷换显示的界面.
                if (mCurrentItem == 0) {//换第一个
                    mBanner.setCurrentItem(mBannerCount - 2, false);
                } else if (mCurrentItem == mBannerCount - 1) {//换最后一个
                    mBanner.setCurrentItem(1, false);
                }
                break;
            case ViewPager.SCROLL_STATE_DRAGGING://滑动中，手指还没有离开屏幕
                break;
            case ViewPager.SCROLL_STATE_SETTLING://滑动中，手指已经离开屏幕
                break;
            default:
                break;
        }
    }

    public SimpleBanner<T, V> setOnBannerClickListener(OnBannerClickListener listener) {
        mBannerAdapter.setOnBannerClickListener(listener);
        return this;
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
