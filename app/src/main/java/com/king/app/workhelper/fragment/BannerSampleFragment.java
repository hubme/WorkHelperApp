package com.king.app.workhelper.fragment;

import android.content.Context;
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
 * @author huoguangxu
 * @since 2017/5/9.
 */

public class BannerSampleFragment extends AppBaseFragment {
    @BindView(R.id.simple_banner) SimpleBanner mSimpleBanner;
    private int[] mImageIds = new int[]{R.mipmap.banner1, R.mipmap.banner2/*, R.mipmap.banner3*/};


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
        mSimpleBanner.updateBanner(fakeData());
        mSimpleBanner.setOnBannerClickListener(position -> showToast(String.valueOf(position)));
    }

    private List<BannerModel> fakeData() {
        List<BannerModel> banners = new ArrayList<>();
        for (@DrawableRes int id : mImageIds) {
            banners.add(new BannerModel(id));
        }
        return banners;
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
