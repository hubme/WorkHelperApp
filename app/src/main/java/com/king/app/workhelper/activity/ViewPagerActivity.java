package com.king.app.workhelper.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.common.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ViewPagerActivity extends AppBaseActivity {
    @BindView(R.id.king_view_pager)
    ViewPager mViewPager;

    private List<ImageView> mImageViews = new ArrayList<>();
    private int[] mImageIds = new int[]{R.mipmap.intro_1_img, R.mipmap.intro_2_img, R.mipmap.intro_3_img};

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

        mViewPager.setAdapter(new PagerAdapter() {
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
        });
//        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
    }
}