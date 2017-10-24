package com.king.applib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.king.applib.R;

/**
 * 顶部 SimpleDraweeView，底部 TextView 的包装View.
 *
 * @author huoguangxu
 * @since 2017/10/23.
 */

public class TitleImageView extends LinearLayout {
    private final SimpleDraweeView mImageView;
    private final TextView mTextView;

    private CharSequence mTitleText;
    private int mTitleColor;
    private float mTitleSize;
    private int mTitleTopMargin;
    private int mImageWidth;
    private int mImageHeight;
    
    public TitleImageView(Context context) {
        this(context, null);
    }

    public TitleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleImageView);

            mTitleText = typedArray.getText(R.styleable.TitleImageView_titleText);
            mTitleColor = typedArray.getColor(R.styleable.TitleImageView_titleColor, Color.BLACK);
            mTitleSize = typedArray.getDimension(R.styleable.TitleImageView_titleSize, dip2px(14));
            mTitleTopMargin = typedArray.getDimensionPixelSize(R.styleable.TitleImageView_titleTopMargin, 0);

            mImageWidth = typedArray.getDimensionPixelSize(R.styleable.TitleImageView_imageWidth, -1);
            mImageHeight = typedArray.getDimensionPixelSize(R.styleable.TitleImageView_imageHeight, -1);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        mImageView = new SimpleDraweeView(getContext());
        mTextView = new TextView(getContext());

        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mImageWidth, mImageHeight));
        addView(mImageView);

        LayoutParams titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleParams.setMargins(0, mTitleTopMargin, 0, 0);
        mTextView.setLayoutParams(titleParams);
        mTextView.setText(mTitleText);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
        mTextView.setTextColor(mTitleColor);

        addView(mTextView);
    }

    public TitleImageView setImageUrl(String url) {
        if (!isTextEmpty(url)) {
            mImageView.setImageURI(Uri.parse(url));
        }
        return this;
    }

    public TitleImageView setImageRes(@DrawableRes int resId) {
        mImageView.setImageURI(Uri.parse("res://" + getContext().getPackageName() + "/" + resId));
        return this;
    }

    public SimpleDraweeView getDraweeView() {
        return mImageView;
    }

    public TextView getTitleView() {
        return mTextView;
    }

    private float dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    private static boolean isTextEmpty(String text) {
        return text == null || TextUtils.isEmpty(text.trim());
    }
    
}
