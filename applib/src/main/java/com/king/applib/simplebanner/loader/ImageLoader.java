package com.king.applib.simplebanner.loader;

import android.content.Context;
import android.view.View;

/**
 * @author VanceKing
 * @since 2017/5/8 0008.
 */

public abstract class ImageLoader implements ImageLoaderInterface{
    @Override public void displayImage(Context context, Object uri, View imageView) {
        
    }

    @Override public View createImageView(Context context) {
        return null;
    }
}
