package com.king.applib.builder;

import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Bundle构建器
 *
 * @author huoguangxu
 * @since 2017/3/8.
 */
public class BundleBuilder {

    private final Bundle bundle = new Bundle();

    public <T extends Serializable> BundleBuilder put(String key, T value) {
        bundle.putSerializable(key, value);
        return this;
    }

    public BundleBuilder put(String key, Parcelable value) {
        bundle.putParcelable(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) bundle.getSerializable(key);
    }

    public Bundle build() {
        return bundle;
    }
}