package com.king.app.workhelper.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.model.entity.BannerModel;
import com.king.app.workhelper.ui.viewpager.ViewPagerHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ViewPagerSampleFragment2 extends AppBaseFragment {
    public static final int BANNER_SHOW_DURATION = 3000;
    @BindView(R.id.king_view_pager)
    ViewPager mViewPager;

    private List<BannerModel> mBannerData = new ArrayList<>();
    private int[] mImageIds = new int[]{R.mipmap.intro_1_img, R.mipmap.intro_2_img/*, R.mipmap.intro_3_img*/};

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

    @Override protected void initData() {
        super.initData();

        generateFakeData();

        mBannerAdapter = new BannerAdapter(mBannerData);
        mViewPager.setAdapter(mBannerAdapter);
        ViewPagerHelper.setDefaultPagerSwitchDuration(mViewPager);
//        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
//        mViewPager.setPageTransformer(true, new DepthPageTransformer());

//        startBannerLoop();
    }

    private void generateFakeData() {
        for (int imgId : mImageIds) {
            mBannerData.add(new BannerModel(imgId));
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        stopBannerLoop();
    }

    private class BannerAdapter extends PagerAdapter{
        private final List<ImageView> mImageViews= new ArrayList<>();
        private int mFakeSize ;//实现循环
        private int mActualSize;

        public BannerAdapter(List<BannerModel> bannerData) {
            if (bannerData == null || bannerData.isEmpty()) {
                return;
            }
            mActualSize = bannerData.size();
            if (mActualSize > 1) {
                buildBannerView(bannerData);
                buildBannerView(bannerData);
            }
            mFakeSize = mImageViews.size();
        }

        private void buildBannerView(List<BannerModel> bannerData) {
            for (BannerModel banner : bannerData) {
                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageResource(banner.resId);
                mImageViews.add(imageView);
            }
        }
        
        @Override public Object instantiateItem(ViewGroup container, int position) {
            final int pos = position % mActualSize;
            final ImageView imageView = mImageViews.get(pos);
            if (container.equals(imageView.getParent())) {
                container.removeView(imageView);
            }
            container.addView(imageView);
            return imageView;
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override public int getCount() {
            return mFakeSize;
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            if (getCount() <= 1) {
                return;
            }
            //解决第一页不能向左滑，最后一页不能向右滑的问题.
            int position = mViewPager.getCurrentItem();
            Log.i("aaa", "position: " + position);
            if (position == 0) {//第一页
                position = mActualSize;
                Log.i("aaa", "first page: "+position);
                //要设为false.第一页和最后一页切换时内容是一页的，不设置时间差.
                mViewPager.setCurrentItem(position, false);
            } else if (position == mFakeSize - 1) {//最后一页
                position = mActualSize - 1;
                Log.i("aaa", "last page: "+position);
                mViewPager.setCurrentItem(position, false);
            }
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
        mHandler.removeCallbacksAndMessages(null);
    }
}