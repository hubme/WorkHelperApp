package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.king.app.workhelper.R;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.StringUtil;

/**
 * 横向滚动的标签view
 * Created by HuoGuangxu on 2016/12/23.
 */

public class HorizontalTagView extends HorizontalScrollView {
    private static final int DEFAULT_TEXT_SIZE = 16;//sp
    private static final int DEFAULT_TAG_RADIUS = 16;//dp
    private static final int DEFAULT_TAG_MARGIN = 15;//dp
    private int mTagMargin = DEFAULT_TAG_MARGIN;

    private OnTagCheckedListener mOnTagCheckedListener;

    private LinearLayout mTagPanel;

    public HorizontalTagView(Context context) {
        this(context, null);
    }

    public HorizontalTagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setPadding(ExtendUtil.dp2px(DEFAULT_TAG_MARGIN), 0, ExtendUtil.dp2px(DEFAULT_TAG_MARGIN), 0);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTagPanel = new LinearLayout(getContext());
        mTagPanel.setOrientation(LinearLayout.HORIZONTAL);
//        mTagPanel.setPadding(ExtendUtil.dp2px(DEFAULT_TAG_MARGIN), 0, ExtendUtil.dp2px(DEFAULT_TAG_MARGIN), 0);
//        mTagPanel.setLayoutParams(params);

        mTagPanel.addView(buildTextView("上海", true));
        mTagPanel.addView(buildTextView("讨论葱姜范双方", false));
        mTagPanel.addView(buildTextView("出错个拖出", true));
        mTagPanel.addView(buildTextView("新建", false));
        mTagPanel.addView(buildTextView("哈哈哈", false));
        addView(mTagPanel);
    }

    public void addTag(boolean isChecked, String tag) {
        if (StringUtil.isNullOrEmpty(tag)) {
            return;
        }
        mTagPanel.addView(buildTextView(tag, isChecked));
        postInvalidate();
    }

    public boolean isTagExists(String tag) {
        if (StringUtil.isNullOrEmpty(tag) || mTagPanel.getChildCount() <= 0) {
            return false;
        }
        for (int i = 0; i < mTagPanel.getChildCount(); i++) {
            View tagView = mTagPanel.getChildAt(i);
            if (tagView != null && tagView instanceof CheckedTextView) {
                if (tag.equalsIgnoreCase(((CheckedTextView) tagView).getText().toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getTag() {

        return "";
    }

    public void setTagMargin(int dpValue) {
        if (dpValue > 0) {
            mTagMargin = dpValue;
        }
    }

    private CheckedTextView buildTextView(String text, boolean isChecked) {
        final CheckedTextView tagTextView = new CheckedTextView(getContext());
        tagTextView.setChecked(isChecked);
        tagTextView.setTextSize(DEFAULT_TEXT_SIZE);
        tagTextView.setPadding(ExtendUtil.dp2px(15), ExtendUtil.dp2px(3), ExtendUtil.dp2px(15), ExtendUtil.dp2px(3));
        setTagBackground(tagTextView);
        tagTextView.setText(text);
        tagTextView.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                tagTextView.toggle();
                setTagBackground(tagTextView);
                if (mOnTagCheckedListener != null) {
                    mOnTagCheckedListener.onTagChecked(tagTextView.isChecked(), tagTextView.getText().toString());
                }
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, ExtendUtil.dp2px(mTagMargin), 0);
        tagTextView.setLayoutParams(lp);
        return tagTextView;
    }

    private void setTagBackground(CheckedTextView textView) {
        textView.setTextColor(textView.isChecked() ? getResources().getColor(R.color.white) : getResources().getColor(R.color.gray_939393));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(createTagBackgroundDrawable(textView.isChecked()));
        } else {
            textView.setBackgroundDrawable(createTagBackgroundDrawable(textView.isChecked()));
        }
    }

    private GradientDrawable createTagBackgroundDrawable(boolean isChecked) {
        GradientDrawable drawable = new GradientDrawable();
//        drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(ExtendUtil.dp2px(DEFAULT_TAG_RADIUS));
        drawable.setColor(getContext().getResources().getColor(isChecked ? R.color.blue_78b7ff : R.color.gray_f0f0f0));
        return drawable;
    }

    public void setOnTagCheckedListener(OnTagCheckedListener listener) {
        mOnTagCheckedListener = listener;
    }

    public interface OnTagCheckedListener {
        void onTagChecked(boolean isChecked, String tag);
    }
}
