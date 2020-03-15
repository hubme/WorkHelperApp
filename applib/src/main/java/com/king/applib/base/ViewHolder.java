package com.king.applib.base;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.core.content.ContextCompat;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 缓存 ViewGroup 的子 View，避免每次都查找。
 *
 * @author VanceKing
 * @since 2016/9/30.
 */
public class ViewHolder<V extends View> {
    private final SparseArray<V> mViews;
    private final View mConvertView;

    private ViewHolder(View convertView) {
        this(convertView, 0);

    }

    private ViewHolder(View convertView, int childCount) {
        mConvertView = convertView;
        mConvertView.setTag(this);
        mViews = new SparseArray<>(Math.max(childCount, 6));
    }

    public static ViewHolder create(View convertView) {
        return new ViewHolder(convertView);
    }

    public static ViewHolder create(View convertView, int childCount) {
        return new ViewHolder(convertView, childCount);
    }

    public V getView(@IdRes int viewId) {
        V view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public ViewHolder setText(@IdRes int viewId, String text) {
        View tv = getView(viewId);
        if (tv instanceof TextView && text != null) {
            ((TextView) tv).setText(text);
        }
        return this;
    }

    public ViewHolder setImageResource(@IdRes int viewId, @DrawableRes int resId) {
        View view = getView(viewId);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(resId);

        }
        return this;
    }

    public ViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        View view = getView(viewId);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(bitmap);
        }
        return this;
    }

    public ViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
        View view = getView(viewId);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(drawable);
        }
        return this;
    }

    public ViewHolder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHolder setTextColor(@IdRes int viewId, @ColorInt int colorInt) {
        View view = getView(viewId);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(colorInt);
        }
        return this;
    }

    public ViewHolder setTextColorRes(@IdRes int viewId, @ColorRes int colorRes) {
        View view = getView(viewId);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(ContextCompat.getColor(mConvertView.getContext(), colorRes));
        }
        return this;
    }

    public ViewHolder setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }


    public ViewHolder setTag(@IdRes int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ViewHolder setTag(@IdRes int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ViewHolder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnTouchListener(@IdRes int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
}
