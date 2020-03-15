package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.View;

import com.king.applib.util.ExtendUtil;

/**
 * 标签View，是否选中。
 *
 * @author VanceKing
 * @since 2016/12/27
 */
public class TagTextView extends androidx.appcompat.widget.AppCompatCheckedTextView implements View.OnClickListener {
    private final int DEFAULT_TAG_RADIUS = 16;//dp

    private boolean mChangeBackground = true;
    private OnTagCheckedListener mOnTagCheckedListener;
    private GradientDrawable mBackgroundDrawable;

    private int mNormalBackgroundColor = Color.parseColor("#f0f0f0");

    private int mCheckedBackgroundColor = Color.parseColor("#78b7ff");

    public TagTextView(Context context) {
        this(context, null);
    }

    public TagTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBackgroundDrawable = new GradientDrawable();
        mBackgroundDrawable.setShape(GradientDrawable.RECTANGLE);
        mBackgroundDrawable.setCornerRadius(ExtendUtil.dp2px(DEFAULT_TAG_RADIUS));

        if (isChecked()) {
            setCheckedBackground();
        } else {
            setNormalBackground();
        }

        setOnClickListener(this);

        setPadding(ExtendUtil.dp2px(15), ExtendUtil.dp2px(3), ExtendUtil.dp2px(15), ExtendUtil.dp2px(3));
    }

    public void setChangedBackground(boolean isChangeBackground) {
        mChangeBackground = isChangeBackground;
    }

    public void setNormalBackgroundColor(@ColorRes int resId) {
        mNormalBackgroundColor = resId;
    }

    public void setCheckedBackgroundColor(@ColorRes int resId) {
        mCheckedBackgroundColor = resId;
    }

    public void setNormalBackground() {
        mBackgroundDrawable.setColor(mNormalBackgroundColor);
        setBackground();
    }

    public void setCheckedBackground() {
        mBackgroundDrawable.setColor(mCheckedBackgroundColor);
        setBackground();
    }

    private void setBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(mBackgroundDrawable);
        } else {
            setBackgroundDrawable(mBackgroundDrawable);
        }
    }

    @Override
    public void onClick(View v) {
        toggle();
        if (mChangeBackground) {
            if (isChecked()) {
                setCheckedBackground();
            } else {
                setNormalBackground();
            }
        }
        if (mOnTagCheckedListener != null) {
            mOnTagCheckedListener.onTagChecked(this);
        }
    }

    public void setOnTagCheckedListener(OnTagCheckedListener listener) {
        mOnTagCheckedListener = listener;
    }

    public interface OnTagCheckedListener {
        void onTagChecked(TagTextView tagView);
    }
}
