package com.king.app.workhelper.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.model.entity.BannerModel;
import com.king.applib.simplebanner.SimpleBanner;
import com.king.applib.simplebanner.loader.BannerInterface;

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
    @BindView(R.id.simple_banner)
    SimpleBanner<BannerModel, View> mSimpleBanner;
    private int[] mImageIds = new int[]{R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};
    private String[] mImageUrls = new String[]{"http://ww1.sinaimg.cn/mw690/8031bb7bgy1fft1uplxf0j20kq071jvq.jpg",
            "http://ww1.sinaimg.cn/mw690/8031bb7bgy1fft1vl88hqj20sg08w76c.jpg",
            "http://ww1.sinaimg.cn/mw690/8031bb7bgy1fft1wffbppj20sg0g00vu.jpg",
            "http://ww1.sinaimg.cn/mw690/8031bb7bgy1fft1xan7owj20kt06ygpt.jpg"};

    private String[] newUrls = new String[]{"http://ww1.sinaimg.cn/mw690/8031bb7bgy1fft1xpvsraj20sg0dvgpe.jpg",
            "http://ww1.sinaimg.cn/mw690/8031bb7bgy1fft1y7yloyj20qo08ngqw.jpg",
            "http://ww1.sinaimg.cn/mw690/8031bb7bgy1fft1yiuup5j20rs0e3wgv.jpg"};
    private boolean change = false;

    @Override
    protected int getContentLayout() {
        return R.layout.layout_banner_sample;
    }

    @Override
    protected void initData() {
        super.initData();
        mSimpleBanner.setBannerLoader(new MyBannerInterface())
                .setSelectedIndicatorColor(Color.RED)
                .setUnSelectedIndicatorColor(Color.BLUE)
                .setIndicatorMargin(10)
                .setIndicatorSize(8)
                .setOnBannerClickListener(position -> showToast(String.valueOf(position)))
                .updateBanner(fakeRemoteData());
    }

    private static class MyBannerInterface implements BannerInterface<BannerModel, View> {
        @Override
        public void displayBanner(Context context, BannerModel bannerModel, View bannerView) {
            ((SimpleDraweeView) bannerView.findViewById(R.id.banner_image)).setImageURI(Uri.parse(bannerModel.imageUrl));
            ((TextView) bannerView.findViewById(R.id.tv_banner_desc)).setText(bannerModel.imageUrl);
        }

        @Override
        public View createBannerView(Context context) {
            return View.inflate(context, R.layout.layout_banner_item, null);
        }
    }

    private List<BannerModel> fakeRemoteData() {
        List<BannerModel> bannerModels = new ArrayList<>();
        for (String url : mImageUrls) {
            bannerModels.add(new BannerModel(url, "呵呵呵"));
        }
        return bannerModels;
    }

    private List<BannerModel> newData() {
        List<BannerModel> bannerModels = new ArrayList<>();
        for (String url : newUrls) {
            bannerModels.add(new BannerModel(url, "哈哈哈"));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSimpleBanner != null) {
            mSimpleBanner.stopLoop();
        }
    }
}
