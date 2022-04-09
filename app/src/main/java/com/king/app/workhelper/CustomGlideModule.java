package com.king.app.workhelper;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * @author VanceKing
 * @since 19-8-27.
 */
@GlideModule
public class CustomGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, Glide glide, @NonNull Registry registry) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();

        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory((Call.Factory) client);
        glide.getRegistry().replace(GlideUrl.class, InputStream.class, factory);
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);

        //在Glide内部，会读取原始图片的EXIF头信息，获取当前图片的格式。若当前格式的图片支持alpha通道，则还是会设置为ARGB_8888的格式。
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
