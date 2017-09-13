package android.support.v4.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.View;


public class LoadingDrawable extends MaterialProgressDrawable {
    @ColorInt private static final int DEFAULT_COLOR = Color.parseColor("#D2691E");//chocolate

    public LoadingDrawable(Context context, View parent) {
        super(context, parent);

        setColorSchemeColors(DEFAULT_COLOR);
        setAlpha(255);   
    }
}
