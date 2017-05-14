package com.king.app.workhelper.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;

import com.facebook.drawee.view.SimpleDraweeView;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.simplebanner.BannerModel;
import com.king.applib.simplebanner.SimpleBanner;
import com.king.applib.simplebanner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Banner使用Sample
 *
 * @author huoguangxu
 * @since 2017/5/9.
 */

public class BannerSampleFragment extends AppBaseFragment {
    @BindView(R.id.simple_banner) SimpleBanner mSimpleBanner;
    private int[] mImageIds = new int[]{R.mipmap.banner1, R.mipmap.banner2, R.mipmap.banner3};
    private String[] mImageUrls = new String[]{"http://192.168.1.11:8080/banners/banner_520_1.jpg",
            "http://192.168.1.11:8080/banners/banner_520_2.jpg",
            /*"http://192.168.1.11:8080/banners/banner_520_3.jpg",
            "http://192.168.1.11:8080/banners/banner_520_4.jpg"*/};

    private String[] newUrls = new String[]{"http://192.168.1.11:8080/banners/banner_e_business.jpg",
            "http://192.168.1.11:8080/banners/banner_mothers_day.jpg",
            /*"http://192.168.1.11:8080/banners/banner_one.jpg"*/};
    private boolean change = false;

    @Override protected int getContentLayout() {
        return R.layout.layout_banner_sample;
    }

    @Override protected void initData() {
        super.initData();
        mSimpleBanner.setImageLoader(new ImageLoaderInterface<SimpleDraweeView>() {
            @Override public void displayImage(Context context, Object uri, SimpleDraweeView imageView) {
                imageView.setImageURI((String) uri);
            }

            @Override public SimpleDraweeView createImageView(Context context) {
                return new SimpleDraweeView(context);
            }
        });
        mSimpleBanner.setSelectedIndicatorColor(Color.RED)
                .setUnSelectedIndicatorColor(Color.BLUE)
                .setIndicatorMargin(10)
                .setIndicatorSize(8)
                .updateBanner(fakeRemoteData());
        mSimpleBanner.setOnBannerClickListener(position -> showToast(String.valueOf(position)));
    }

    private List<BannerModel> fakeLocalData() {
        List<BannerModel> banners = new ArrayList<>();
        for (@DrawableRes int id : mImageIds) {
            banners.add(new BannerModel(id));
        }
        return banners;
    }

    private List<BannerModel> fakeRemoteData() {
        List<BannerModel> bannerModels = new ArrayList<>();
        for (String url : mImageUrls) {
            bannerModels.add(new BannerModel(url));
        }
        return bannerModels;
    }

    private List<BannerModel> newData() {
        List<BannerModel> bannerModels = new ArrayList<>();
        for (String url : newUrls) {
            bannerModels.add(new BannerModel(url));
        }
        return bannerModels;
    }

    @OnClick(R.id.tv_start_loop)
    public void onStartLoopClick() {
        mSimpleBanner.startLoop();
    }

    @OnClick(R.id.tv_stop_loop)
    public void onStopLoopClick() {
        mSimpleBanner.stopLoop();
    }

    @OnClick(R.id.tv_update_banner)
    public void onUpdateBannerClick() {
        if (!change) {
            mSimpleBanner.updateBanner(newData());
            change = true;
        } else {
            mSimpleBanner.updateBanner(fakeRemoteData());
            change = false;
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if (mSimpleBanner != null) {
            mSimpleBanner.stopLoop();
        }
    }
}
