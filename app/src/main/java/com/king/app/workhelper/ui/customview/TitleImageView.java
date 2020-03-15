package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.king.app.workhelper.R;
import com.king.applib.util.StringUtil;

/**
 * 包含一个TextView 和一个 SimpleDraweeView的包装View.
 *
 * @author VanceKing
 * @since 2017/10/23.
 */

public class TitleImageView extends LinearLayout {
    private final SimpleDraweeView mImageView;
    private final TextView mTextView;

    private CharSequence mTitleText;
    private int mTitleColor;
    private float mTitleSize;
    private int mTitleMargin;
    private int mImageWidth;
    private int mImageHeight;
    private int mImagePosition;

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
            mTitleMargin = typedArray.getDimensionPixelSize(R.styleable.TitleImageView_titleMargin, 0);

            mImageWidth = typedArray.getDimensionPixelSize(R.styleable.TitleImageView_imageWidth, -1);
            mImageHeight = typedArray.getDimensionPixelSize(R.styleable.TitleImageView_imageHeight, -1);
            mImagePosition = typedArray.getInt(R.styleable.TitleImageView_imagePosition, 1);
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
        setGravity(Gravity.CENTER);

        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mImageWidth, mImageHeight));

        LayoutParams titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleParams.setMargins(mImagePosition == 0 ? mTitleMargin : 0,
                mImagePosition == 1 ? mTitleMargin : 0,
                mImagePosition == 2 ? mTitleMargin : 0,
                mImagePosition == 3 ? mTitleMargin : 0);
        mTextView.setLayoutParams(titleParams);
        mTextView.setText(mTitleText);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
        mTextView.setTextColor(mTitleColor);

        switch (mImagePosition) {
            case 0: //left
                setOrientation(LinearLayout.HORIZONTAL);
                addView(mImageView);
                addView(mTextView);
                break;
            case 2: //right
                setOrientation(LinearLayout.HORIZONTAL);
                addView(mTextView);
                addView(mImageView);
                break;
            case 3: //bottom
                setOrientation(LinearLayout.VERTICAL);
                addView(mTextView);
                addView(mImageView);
                break;
            case 1: //top
            default:
                setOrientation(LinearLayout.VERTICAL);
                addView(mImageView);
                addView(mTextView);
                break;
        }
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

    public TitleImageView setTitle(String title) {
        if (StringUtil.isNotNullOrEmpty(title)) {
            mTextView.setText(title);
        }
        return this;
    }

    public TitleImageView setTitleColor(int colorInt) {
        mTextView.setTextColor(colorInt);
        return this;
    }

    public TitleImageView setTitleColorRes(@ColorRes int colorRes) {
        mTextView.setTextColor(ContextCompat.getColor(getContext(), colorRes));
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
