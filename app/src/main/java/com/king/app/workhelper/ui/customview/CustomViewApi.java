package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Build;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.TypedValue;

import com.king.app.workhelper.R;

/**
 * @author VanceKing
 * @since 2017/6/25 0025.
 */

class CustomViewApi {
    public CustomViewApi() {
        RoundedBitmapDrawableFactory.create(null, "");

        Paint paint = new Paint();
        paint.setPathEffect(new DashPathEffect(null, 0));
    }

    public static int colorPrimary(Context context, int defaultValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getColor(context, android.R.attr.colorPrimary, defaultValue);
        }
        return getColor(context, R.attr.colorPrimary, defaultValue);
    }

    private static int getColor(Context context, int id, int defaultValue) {
        TypedValue value = new TypedValue();

        Resources.Theme theme = context.getTheme();
        if (theme != null && theme.resolveAttribute(id, value, true)) {
            if (value.type >= TypedValue.TYPE_FIRST_INT
                    && value.type <= TypedValue.TYPE_LAST_INT) {
                return value.data;
            } else if (value.type == TypedValue.TYPE_STRING) {
                return context.getResources().getColor(value.resourceId);
            }
        }
        return defaultValue;
    }
}
