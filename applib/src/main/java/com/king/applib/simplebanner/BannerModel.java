package com.king.applib.simplebanner;

import android.support.annotation.DrawableRes;

/**
 * @author huoguangxu
 * @since 2017/5/10.
 */

public class BannerModel {
    @DrawableRes int imageId;
    public String imageUrl;

    public BannerModel() {
    }

    public BannerModel(@DrawableRes int imageId) {
        this.imageId = imageId;
    }

    public BannerModel(String url) {
        this.imageUrl = url;
    }
}
