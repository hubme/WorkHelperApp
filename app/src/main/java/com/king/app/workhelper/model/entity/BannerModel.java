package com.king.app.workhelper.model.entity;

import android.support.annotation.DrawableRes;

/**
 * @author huoguangxu
 * @since 2017/5/8.
 */

public class BannerModel {
    @DrawableRes public int resId;
    public String imageUrl;
    public String mTips;

    public BannerModel() {
        
    }

    public BannerModel(String imageUrl, String mTips) {
        this.imageUrl = imageUrl;
        this.mTips = mTips;
    }
}
