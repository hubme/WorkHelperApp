package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.applib.util.StringUtil;

/**
 * 带蒙层的ImageView
 * Created by HuoGuangxu on 2016/11/15.
 */

public class LayerImageView extends RelativeLayout {

    private View mRootView;
    private ImageView mImageView;
    private TextView mLayerImageTv;

    public LayerImageView(Context context) {
        this(context, null);
    }

    public LayerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LayerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mRootView = View.inflate(getContext(), R.layout.layout_layer_image_view, this);

        mImageView = (ImageView) mRootView.findViewById(R.id.iv_simple_image);
        mLayerImageTv = (TextView) mRootView.findViewById(R.id.tv_image_layer);
    }

    public LayerImageView setImageResource(@DrawableRes int resId) {
        mImageView.setBackgroundResource(resId);
        return this;
    }

    public LayerImageView setImageBitmap(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
        return this;
    }

    public LayerImageView setImageDrawable(Drawable drawable) {
        mImageView.setImageDrawable(drawable);
        return this;
    }

    public LayerImageView setImageURI(Uri uri) {
        if (uri != null) {
            mImageView.setImageURI(uri);
        }
        return this;
    }

    public LayerImageView setLayerColor(@ColorRes int resId) {
        mLayerImageTv.setBackgroundColor(getResources().getColor(resId));
        return this;
    }

    public LayerImageView setLayerHint(String text) {
        if (!StringUtil.isNullOrEmpty(text)) {
            mLayerImageTv.setText(text);
        }
        return this;
    }

    public LayerImageView setLayerHint(@StringRes int resId) {
        mLayerImageTv.setText(resId);
        return this;
    }

    public LayerImageView setHintColor(@ColorRes int resId) {
        mLayerImageTv.setTextColor(getResources().getColor(resId));
        return this;
    }

    public LayerImageView setHintSize(@DimenRes int resId) {
        mLayerImageTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(resId));
        return this;
    }

    public LayerImageView showLayer() {
        mLayerImageTv.setVisibility(View.VISIBLE);
        return this;
    }

    public LayerImageView hideLayer() {
        mLayerImageTv.setVisibility(View.GONE);
        return this;
    }
}