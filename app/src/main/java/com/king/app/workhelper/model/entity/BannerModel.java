package com.king.app.workhelper.model.entity;

import android.support.annotation.DrawableRes;

/**
 * @author huoguangxu
 * @since 2017/5/8.
 */

public class BannerModel {
    @DrawableRes public int resId;
    private String imageUrl;
    private String mTips;

    public String getTips() {
        return mTips;
    }

    public void setTips(String tips) {
        mTips = tips;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BannerModel() {
        
    }

    public BannerModel(@DrawableRes int resId) {
        this.resId = resId;
    }
}
