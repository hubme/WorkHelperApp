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

    private final Bundle mBundle;

    public static BundleBuilder create() {
        return new BundleBuilder();
    }

    public static BundleBuilder create(Bundle bundle) {
        return new BundleBuilder(bundle);
    }

    private BundleBuilder() {
        mBundle = new Bundle();
    }

    private BundleBuilder(Bundle bundle) {
        mBundle = new Bundle(bundle);
    }

    public <T extends Serializable> BundleBuilder put(String key, T value) {
        mBundle.putSerializable(key, value);
        return this;
    }

    public BundleBuilder put(String key, Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) mBundle.getSerializable(key);
    }

    public Bundle build() {
        return mBundle;
    }
}