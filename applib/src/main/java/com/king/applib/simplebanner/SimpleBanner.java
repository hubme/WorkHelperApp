package com.king.applib.simplebanner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.king.applib.R;
import com.king.applib.simplebanner.listener.OnBannerClickListener;
import com.king.applib.simplebanner.loader.ImageLoaderInterface;
import com.king.applib.util.LogUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author huoguangxu
 * @since 2017/5/9.
 */

/*
假设原始界面是:A、B、C.则构造成C、A、B、C、A.当position==0(C界面)时，设置setCurrentItem==3(也是C界面),可以向右滑.
当position==4(A)时，设置setCurrentItem==1(也是A界面),可以向左滑,达到循环的目的.
 */
public class SimpleBanner extends RelativeLayout implements ViewPager.OnPageChangeListener {
    public static final String TAG = "aaa";
    private static final int DEFAULT_SCROLL_DURATION = 1000;
    private static final int DEFAULT_DELAY_DURATION = 3000;
    private static final int DEFAULT_INDICATOR_SIZE = 4;//dp
    private static final int DEFAULT_INDICATOR_MARGIN = 2;//dp

    private Context mContext;
    private BannerViewPager mBanner;
    private BannerAdapter mBannerAdapter;
    private OnBannerClickListener mOnBannerClickListener;
    private ScheduledExecutorService mExecutor;

    private Handler mHandler = new Handler();
    private int mBannerCount;
    private BannerLoopTask mLoopTask;

    private ImageLoaderInterface mImageLoader;
    private LinearLayout mIndicatorPanel;
    private List<View> mIndicatorViews;
    private Drawable mSelectedDrawable;
    private Drawable mUnSelectedDrawable;

    public SimpleBanner(Context context) {
        this(context, null);
    }

    public SimpleBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.applib_layout_banner, this, true);
        initView(view);
        initData();

        initViewPagerScroll();
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSize(Integer.MAX_VALUE, widthMeasureSpec), resolveSize(dp2px(mContext, 200), heightMeasureSpec));
    }

    private void initView(View view) {
        mBannerAdapter = new BannerAdapter();
        mBanner = (BannerViewPager) view.findViewById(R.id.banner_view_pager);
        mBanner.setAdapter(mBannerAdapter);

        mIndicatorPanel = (LinearLayout) view.findViewById(R.id.indicator_panel);
    }

    private void initData() {
        mExecutor = Executors.newScheduledThreadPool(1);
        mLoopTask = new BannerLoopTask();
    }

    public void updateBanner(List<BannerModel> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        List<ImageView> imageViews = generateBannerData(images);
        mBannerAdapter.update(imageViews);
        mBannerCount = mBannerAdapter.getCount();
        if (mBannerCount > 1) {
            mBanner.setCurrentItem(1);
            mBanner.addOnPageChangeListener(this);
            mBanner.setScrollable(true);

            mSelectedDrawable = getIndicatorDrawable(Color.RED);
            mUnSelectedDrawable = getIndicatorDrawable(Color.BLACK);
            mIndicatorViews = buildIndicatorView();
            setupIndicator();
            updateIndicator(mBannerCount - 3);//选中第一个，多加两个-2，下标-1

//            startLoop();
        }
    }

    //构造界面。A、B、C--->C、A、B、C、A
    private List<ImageView> generateBannerData(List<BannerModel> images) {
        if (images == null || images.isEmpty()) {
            return null;
        }

        List<ImageView> imageViews = new ArrayList<>();
        if (images.size() == 1) {
            imageViews.add(getImageView(images.get(0).imageId));
        } else {
            //先添加最后一个
            imageViews.add(getImageView(images.get(images.size() - 1).imageId));

            for (BannerModel banner : images) {
                imageViews.add(getImageView(banner.imageId));
            }

            //最后添加第一个
            imageViews.add(getImageView(images.get(0).imageId));
        }
        return imageViews;
    }

    private ImageView getImageView(@DrawableRes int resId) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(resId);
        return imageView;
    }

    public void startLoop() {
        if (mBannerCount <= 1) {
            return;
        }

//        mExecutor.scheduleAtFixedRate(new BannerLoopTask(), 3000, 2000, TimeUnit.MILLISECONDS);

        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(mLoopTask, DEFAULT_DELAY_DURATION);
    }

    public void stopLoop() {
        if (mBannerCount <= 1) {
            return;
        }
//        mExecutor.shutdown();
        mHandler.removeCallbacksAndMessages(null);
    }

    private class BannerLoopTask implements Runnable {

        @Override
        public void run() {
            int mCurrentItem = mBanner.getCurrentItem();
            mCurrentItem = (mCurrentItem + 1) % mBannerCount;

            mBanner.setCurrentItem(mCurrentItem);
            mHandler.postDelayed(mLoopTask, DEFAULT_DELAY_DURATION);
            /*if (mCurrentItem == 1) {
                mBanner.setCurrentItem(mCurrentItem, false);
                mHandler.post(mLoopTask);
            } else {
                mBanner.setCurrentItem(mCurrentItem);
                mHandler.postDelayed(mLoopTask, DEFAULT_DELAY_DURATION);
            }*/
        }
    }

    public void setImageLoader(ImageLoaderInterface imageLoader) {
        mImageLoader = imageLoader;
    }

    private Drawable getIndicatorDrawable(@ColorInt int color) {
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable shape = new ShapeDrawable(ovalShape);
        shape.getPaint().setColor(color);
//        shape.setIntrinsicHeight(dp2px(mContext, 4));
//        shape.setIntrinsicWidth(dp2px(mContext, 4));
        return shape;
    }

    private List<View> buildIndicatorView() {
        final int size = dp2px(mContext, DEFAULT_INDICATOR_SIZE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.leftMargin = dp2px(mContext, DEFAULT_INDICATOR_MARGIN);
        List<View> indicatorViews = new ArrayList<>();
        for (int i = 0; i < mBannerCount - 2; i++) {//由于多生成2个View，要减去
            View view = new View(mContext);
            view.setLayoutParams(params);
            setViewBackground(view, mUnSelectedDrawable);
            indicatorViews.add(view);
        }
        return indicatorViews;
    }

    private void setupIndicator() {
        for (View view : mIndicatorViews) {
            mIndicatorPanel.addView(view);
        }
    }

    private void updateIndicator(int position) {
        final int pos = position - 2;
        final int count = mIndicatorViews.size();
        if (pos < 0 || pos > count - 1) {
            return;
        }
        for (int i = 0; i < count - 1; i++) {
            View view = mIndicatorViews.get(i);
            if (i == pos) {
                setViewBackground(view, mSelectedDrawable);
            } else {
                setViewBackground(view, mUnSelectedDrawable);
            } 
        }
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
            BannerScroller mScroller = new BannerScroller(mContext);
            mScroller.setDuration(DEFAULT_SCROLL_DURATION);
            mField.set(mBanner, mScroller);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        LogUtil.i(TAG, "onPageScrolled--->position: " + position + " ;positionOffset: " + positionOffset + " ;positionOffsetPixels: " + positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        LogUtil.i(TAG, "onPageSelected--->position: " + position);
        updateIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        /*switch (state) {
            case ViewPager.SCROLL_STATE_IDLE://界面完全停止
                LogUtil.i(TAG, "SCROLL_STATE_IDLE");
                break;
            case ViewPager.SCROLL_STATE_DRAGGING://滑动中，手指还没有离开屏幕
                LogUtil.i(TAG, "SCROLL_STATE_DRAGGING");
                break;
            case ViewPager.SCROLL_STATE_SETTLING://滑动中，手指已经离开屏幕
                LogUtil.i(TAG, "SCROLL_STATE_SETTLING");
                break;
            default:
                break;
        }*/


        int mCurrentItem = mBanner.getCurrentItem();
//        LogUtil.i(TAG, "mCurrentItem: " + mCurrentItem);
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE://界面完全停止时，偷换显示的界面.
//                LogUtil.i(TAG, "SCROLL_STATE_IDLE");
                if (mCurrentItem == 0) {//换第一个
                    mBanner.setCurrentItem(mBannerCount - 2, false);
                } else if (mCurrentItem == mBannerCount - 1) {//换最后一个
                    mBanner.setCurrentItem(1, false);
                }
                break;
            case ViewPager.SCROLL_STATE_DRAGGING://滑动中，手指还没有离开屏幕
//                LogUtil.i(TAG, "SCROLL_STATE_DRAGGING");
                break;
            case ViewPager.SCROLL_STATE_SETTLING://滑动中，手指已经离开屏幕
//                LogUtil.i(TAG, "SCROLL_STATE_SETTLING");
                break;
            default:
                break;
        }
    }

    public void setOnBannerClickListener(OnBannerClickListener listener) {
        mBannerAdapter.setOnBannerClickListener(listener);
    }

    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
