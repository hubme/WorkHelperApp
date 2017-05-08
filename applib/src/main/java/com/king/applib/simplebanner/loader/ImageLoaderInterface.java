package com.king.applib.simplebanner.loader;

import android.content.Context;
import android.view.View;

/**
 * 加载图片的方式各种各样,提供一个接口.
 * https://github.com/youth5201314/banner/blob/master/banner/src/main/java/com/youth/banner/loader/ImageLoaderInterface.java
 *
 * @author VanceKing
 * @since 2017/5/8 0008.
 */
public interface ImageLoaderInterface<T extends View> {
    void displayImage(Context context, Object uri, T imageView);

    T createImageView(Context context);
}
