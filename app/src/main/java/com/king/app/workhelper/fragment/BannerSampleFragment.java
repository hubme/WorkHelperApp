package com.king.app.workhelper.fragment;

import android.widget.ImageView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.simplebanner.SimpleBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author huoguangxu
 * @since 2017/5/9.
 */

public class BannerSampleFragment extends AppBaseFragment {
    @BindView(R.id.simple_banner) SimpleBanner mSimpleBanner;
    private int[] mImageIds = new int[]{R.mipmap.banner1, R.mipmap.banner2, R.mipmap.banner3};


    @Override protected int getContentLayout() {
        return R.layout.layout_banner_sample;
    }

    @Override protected void initData() {
        super.initData();
        mSimpleBanner.updateBanner(fakeData());
    }

    private List<ImageView> fakeData() {
        List<ImageView> images = new ArrayList<>();
        for (int id : mImageIds) {
            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(id);
            images.add(imageView);
        }
        return images;
    }

    @OnClick(R.id.tv_start_loop)
    public void onStartLoopClick() {
        mSimpleBanner.startLoop();
    }

    @OnClick(R.id.tv_stop_loop)
    public void onStopLoopClick() {
        mSimpleBanner.stopLoop();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if (mSimpleBanner != null) {
            mSimpleBanner.stopLoop();
        }
    }
}
