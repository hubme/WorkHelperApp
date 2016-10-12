package com.king.app.workhelper.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.ui.viewpager.ViewPagerHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ViewPagerActivity extends AppBaseActivity {
    public static final int BANNER_SHOW_DURATION = 2000;
    @BindView(R.id.king_view_pager)
    ViewPager mViewPager;

    private List<ImageView> mImageViews = new ArrayList<>();
    private int[] mImageIds = new int[]{R.mipmap.intro_1_img, R.mipmap.intro_2_img, R.mipmap.intro_3_img};

    private Handler mHandler = new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int size = mBannerAdapter.getCount();
            int cItem = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem((cItem + 1) % size);
            mHandler.sendEmptyMessageDelayed(0, BANNER_SHOW_DURATION);
        }
    };
    private BannerAdapter mBannerAdapter;

    /**
     * 切换到下一个banner图片的任务
     **/
    private Runnable mLoopBannerRun = new Runnable() {
        @Override
        public void run() {
            int size = mBannerAdapter.getCount();
            int cItem = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem((cItem + 1) % size);
        }
    };

    @Override public int getContentLayout() {
        return R.layout.activity_view_pager;
    }

    @Override protected void initContentView() {
        super.initContentView();
    }

    @Override protected void initData() {
        super.initData();

        for (int imgId : mImageIds) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgId);
            mImageViews.add(imageView);
        }

        mBannerAdapter = new BannerAdapter();
        mViewPager.setAdapter(mBannerAdapter);
        ViewPagerHelper.setDefaultPagerSwitchDuration(mViewPager);
//        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
//        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        startBannerLoop();
    }

    private class BannerAdapter extends PagerAdapter{
        @Override public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViews.get(position));
        }

        @Override public int getCount() {
            return mImageIds.length;
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /**
     * 延迟切换下一个banner
     * <p>
     * 会清除已存在的延迟切换操作
     */
    private void startBannerLoop() {
        if (mBannerAdapter.getCount() > 1) {
            mHandler.sendEmptyMessageDelayed(0, BANNER_SHOW_DURATION);
        }
    }

    private void stopBannerLoop() {
        mHandler.removeCallbacks(mLoopBannerRun);
    }
}