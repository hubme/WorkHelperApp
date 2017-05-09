package com.king.applib.simplebanner;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.king.applib.R;
import com.king.applib.util.LogUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author huoguangxu
 * @since 2017/5/9.
 */

public class SimpleBanner extends RelativeLayout implements ViewPager.OnPageChangeListener {
    public static final String TAG = "aaa";
    public static final int DEFAULT_SCROLL_DURATION = 1000;
    public static final int DEFAULT_DELAY_DURATION = 3000;
    private Context mContext;
    private BannerViewPager mBanner;
    private BannerAdapter mBannerAdapter;
    private OnBannerClickListener mOnBannerClickListener;
    private ScheduledExecutorService mExecutor;
    private boolean mIsAutoLoop = false;
    private int mCurrentItem;

    private Handler mHandler = new Handler();
    private int mBannerCount;
    private BannerLoopTask mLoopTask;

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
        if (mIsAutoLoop) {
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
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoop();
    }

    private void initView(View view) {
        mBannerAdapter = new BannerAdapter();
        mBanner = (BannerViewPager) view.findViewById(R.id.banner_view_pager);
        mBanner.addOnPageChangeListener(this);
        mBanner.setAdapter(mBannerAdapter);
    }

    private void initData() {
        mExecutor = Executors.newScheduledThreadPool(1);
        mLoopTask = new BannerLoopTask();
    }

    public void updateBanner(List<ImageView> images) {
        if (images.size() > 1) {
            startLoop();
            mCurrentItem = 1;
        }
        mBannerAdapter.update(images);
        mBanner.setCurrentItem(mCurrentItem);
        mBannerCount = mBannerAdapter.getCount();
    }

    public void startLoop() {
//        mExecutor.scheduleAtFixedRate(new BannerLoopTask(), 3000, 2000, TimeUnit.MILLISECONDS);

        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new BannerLoopTask(), DEFAULT_DELAY_DURATION);
        mIsAutoLoop = true;
    }

    public void stopLoop() {
//        mExecutor.shutdown();
        mHandler.removeCallbacksAndMessages(null);
        mIsAutoLoop = false;
    }

    private class BannerLoopTask implements Runnable {

        @Override public void run() {
            final int pos = mBanner.getCurrentItem();
            LogUtil.i(TAG, "current position: " + pos);

            mCurrentItem = mCurrentItem % (mBannerCount + 1) + 1;

            if (mCurrentItem == 1) {
                mBanner.setCurrentItem(mCurrentItem, false);
                mHandler.post(mLoopTask);
            } else {
                mBanner.setCurrentItem(mCurrentItem);
                mHandler.postDelayed(mLoopTask, DEFAULT_DELAY_DURATION);
            }
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


    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override public void onPageSelected(int position) {

    }

    @Override public void onPageScrollStateChanged(int state) {
        mCurrentItem = mBanner.getCurrentItem();
        LogUtil.i(TAG, "mCurrentItem: " + mCurrentItem);
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                if (mCurrentItem == 0) {
                    mBanner.setCurrentItem(mBannerCount, false);
                } else if (mCurrentItem == mBannerCount + 1) {
                    mBanner.setCurrentItem(1, false);
                }
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                if (mCurrentItem == mBannerCount + 1) {
                    mBanner.setCurrentItem(1, false);
                } else if (mCurrentItem == 0) {
                    mBanner.setCurrentItem(mBannerCount, false);
                }
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                break;
            default:
                break;
        }
    }

    public interface OnBannerClickListener {
        void onBannerClick(int position);
    }

    public void setOnBannerClickListener(OnBannerClickListener listener) {
        mBannerAdapter.setOnBannerClickListener(new BannerAdapter.OnBannerClickListener() {
            @Override public void onBannerClick(int position) {

            }
        });
    }
}
