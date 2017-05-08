package com.king.applib.simplebanner;

import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.king.applib.base.ViewHolder;
import com.king.applib.util.ContextUtil;

/**
 * @author huoguangxu
 * @since 2017/5/8.
 */

public class BannerViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mItemView;
    private int mLayoutId;

    private BannerViewHolder(View itemView, int position) {
        mItemView = itemView;
        mPosition = position;
        mViews = new SparseArray<>();
        mItemView.setTag(this);

    }

    public static BannerViewHolder getHolder(View itemView, ViewGroup parent, @LayoutRes int layoutId, int position) {
        BannerViewHolder holder;
        if (itemView == null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            holder = new BannerViewHolder(itemView, position);
            holder.mLayoutId = layoutId;
        } else {
            holder = (ViewHolder) itemView.getTag();
        }
        holder.mPosition = position;
        return holder;
    }
}
