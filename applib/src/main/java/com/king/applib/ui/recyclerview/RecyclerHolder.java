package com.king.applib.ui.recyclerview;

import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 适配一切RecyclerView.ViewHolder
 *
 * @author kymjs (http://www.kymjs.com/) on 8/27/15.
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> mViews;
    private final View mItemView;
    
    public RecyclerHolder(View itemView) {
        super(itemView);
        this.mItemView = itemView;
        this.mViews = new SparseArray<>();
    }

    public View getItemView() {
        return mItemView;
    }
    
    public SparseArray<View> getAllView() {
        return mViews;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, view);
            }
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     */
    public RecyclerHolder setText(@IdRes int viewId, String text) {
        TextView view = getView(viewId);
        if (view != null) {
            view.setText(text);
        }
        return this;
    }

    /**
     * 为ImageView设置图片
     */
    public RecyclerHolder setImageResource(@IdRes int viewId, int drawableId) {
        ImageView view = getView(viewId);
        if (view != null) {
            view.setImageResource(drawableId);
        }
        return this;
    }

    /**
     * 为ImageView设置图片
     */
    public RecyclerHolder setImageBitmap(@IdRes int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        if (view != null) {
            view.setImageBitmap(bm);
        }
        return this;
    }

    public RecyclerHolder setViewVisibility(@IdRes int viewId, int visibility) {
        View view = getView(viewId);
        if (view != null) {
            view.setVisibility(visibility);
        }
        return this;
    }

    public RecyclerHolder setOnClickListener(@IdRes int viewId, View.OnClickListener l) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(l);
        }
        return this;
    }
}
