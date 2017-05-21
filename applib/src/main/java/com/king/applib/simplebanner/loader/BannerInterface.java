package com.king.applib.simplebanner.loader;

import android.content.Context;
import android.view.View;

/**
 * 设置Banner的接口
 *
 * @author VanceKing
 * @since 2017/5/8.
 */
public interface BannerInterface<T> {
    void displayBanner(Context context, T bannerModel, View bannerView);

    View createBannerView(Context context);
}
