package com.king.app.workhelper.model.entity;

import androidx.annotation.DrawableRes;

/**
 * @author VanceKing
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
