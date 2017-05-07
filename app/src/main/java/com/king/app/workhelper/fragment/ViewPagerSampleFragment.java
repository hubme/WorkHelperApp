package com.king.app.workhelper.fragment;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.ui.viewpager.ViewPagerHelper;
import com.king.applib.util.AppUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * ViewPager相关
 * Created by VanceKing on 2016/11/26 0026.
 */

public class ViewPagerSampleFragment extends AppBaseFragment {
    public static final int BANNER_SHOW_DURATION = 3000;//mils
    private final int FAKE_BANNER_SIZE = 6;
    private final int DEFAULT_BANNER_SIZE = 3;
    private int mBannerPosition = 0;

    @BindView(R.id.king_view_pager) ViewPager mViewPager;

    private List<ImageView> mImageViews = new ArrayList<>();
    private int[] mImageIds = new int[]{R.mipmap.intro_1_img, R.mipmap.intro_2_img, R.mipmap.intro_3_img};

    private BannerAdapter mBannerAdapter;

    // TODO: 2017/5/7 使用ScheduledThreadPoolExecutor
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            getActivity().runOnUiThread(mLoopBannerRun);
        }
    };

    /**
     * 切换到下一个banner图片的任务
     **/
    private Runnable mLoopBannerRun = new Runnable() {
        @Override
        public void run() {
            mBannerPosition = (mBannerPosition + 1) % FAKE_BANNER_SIZE;
            if (mBannerPosition == FAKE_BANNER_SIZE - 1) {
                mViewPager.setCurrentItem(DEFAULT_BANNER_SIZE - 1);
            } else {
                mViewPager.setCurrentItem(mBannerPosition);
            }
        }
    };

    @Override
    protected int getContentLayout() {
        return R.layout.activity_view_pager;
    }

    @Override
    protected void initData() {
        super.initData();

        for (int imgId : mImageIds) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgId);
//            SimpleDraweeView imageView = new SimpleDraweeView(mContext);
//            imageView.setImageURI(String.format(Locale.US,"res://%s/%d", AppUtil.getAppInfo().getPackageName(), imgId));
            mImageViews.add(imageView);
        }

        mBannerAdapter = new BannerAdapter(mContext);
        mViewPager.setAdapter(mBannerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                mBannerPosition = position;
            }
        });
        ViewPagerHelper.setDefaultPagerSwitchDuration(mViewPager);
//        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
//        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        startBannerLoop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopBannerLoop();
    }

    private class BannerAdapter extends PagerAdapter{

        private final Context mContext;

        public BannerAdapter(Context context) {
            mContext = context;
        }
        /**
         * 当ViewPager的内容有所变化时,进行调用。
         *
         * @param container ViewPager本身
         */
        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        /**
         * 为给定的位置创建相应的View。创建View之后,需要在该方法中自行添加到container中。
         *
         * @param container ViewPager本身
         * @param position  给定的位置
         * @return 提交给ViewPager进行保存的实例对象
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i("aaa", "instantiateItem");
            //IllegalStateException: The specified child already has a parent.
            // You must call removeView() on the child's parent first.
            final int pos = position % DEFAULT_BANNER_SIZE;
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_banner, container, false);
            final SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.banner_image);
            imageView.setImageURI(String.format(Locale.US,"res://%s/%d", AppUtil.getAppInfo().getPackageName(), mImageIds[pos]));
            imageView.setOnClickListener(v -> showToast(String.valueOf(pos)));

            container.addView(view);
            return view;
        }

        /**
         * 为给定的位置移除相应的View。
         *
         * @param container ViewPager本身
         * @param position  给定的位置
         * @param object    在instantiateItem中提交给ViewPager进行保存的实例对象
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        /**
         * 获取View的总数
         *
         * @return View总数
         */
        @Override
        public int getCount() {
            return FAKE_BANNER_SIZE;
        }

        /**
         * 确认View与实例对象是否相互对应。ViewPager内部用于获取View对应的ItemInfo。
         *
         * @param view   ViewPager显示的View内容
         * @param object 在instantiateItem中提交给ViewPager进行保存的实例对象
         * @return 是否相互对应
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            if (getCount() <= 1) {
                return;
            }
            //解决第一页不能向左滑，最后一页不能向右滑的问题.
            int position = mViewPager.getCurrentItem();
            if (position == 0) {//第一页
                position = DEFAULT_BANNER_SIZE;
                Log.i("aaa", "first page: "+position);
                //要设为false.第一页和最后一页切换时内容是一页的，不设置时间差.
                mViewPager.setCurrentItem(position, false);
            } else if (position == FAKE_BANNER_SIZE - 1) {//最后一页
                position = DEFAULT_BANNER_SIZE - 1;
                Log.i("aaa", "last page: "+position);
                mViewPager.setCurrentItem(position, false);
            }
        }
    }

    /**
     * 延迟切换下一个banner
     * 会清除已存在的延迟切换操作
     */
    private void startBannerLoop() {
        if (mBannerAdapter.getCount() > 1) {
            mTimer.schedule(mTimerTask, BANNER_SHOW_DURATION, BANNER_SHOW_DURATION);
        }
    }

    private void stopBannerLoop() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }
}
