package com.king.app.workhelper.ui.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.app.workhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huoguangxu
 * @since 2017/12/22.
 */

public class ExpandableTextViewGroup extends LinearLayout {
    private static final String TAG = "aaa";

    private int mTitleColor = Color.parseColor("#333333");
    private int mCheckedTitleColor = Color.parseColor("#ffb022");
    private int mTitleTextSize = 16;//sp

    private int mContentColor = Color.parseColor("#878787");
    private int mContentTextSize = 15;//sp

    private int mPaddingLeft = dp2px(15);
    private int mPaddingRight = dp2px(15);

    private final List<ExpandModel> mDataList = new ArrayList<>();
    private OnTitleClickListener mOnTitleClickListener;
    private OnContentClickListener mOnSubTitleClickListener;

    /** 注意：前一个动画结束一会才会执行下一个动画 */
    private boolean mUseAnimation = true;
    private ValueAnimator mAnimator;
    private int mAnimDuration = 300;
    private TextView mCurrAnimView;

    public ExpandableTextViewGroup(Context context) {
        this(context, null);
    }

    public ExpandableTextViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);

        if (mUseAnimation) {
            mAnimator = ValueAnimator.ofInt();
            mAnimator.setDuration(mAnimDuration);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override public void onAnimationUpdate(ValueAnimator animation) {
                    if (mCurrAnimView == null) {
                        return;
                    }
                    final ViewGroup.LayoutParams layoutParams = mCurrAnimView.getLayoutParams();
                    layoutParams.height = (int) animation.getAnimatedValue();

                    Log.i(TAG, "anim height: " + layoutParams.height);
                    mCurrAnimView.setLayoutParams(layoutParams);
                    mCurrAnimView.requestLayout();
                }
            });

        }

        
    }

    public void setExpandData(List<ExpandModel> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        if (!mDataList.isEmpty()) {
            mDataList.clear();
        }
        mDataList.addAll(data);
        populateData();
    }

    private List<ExpandModel> getExpandItems() {
        return mDataList;
    }

    private ExpandModel getExpandItem(int position) {
        if (position < 0 || position >= mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    private void populateData() {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        for (int i = 0, size = mDataList.size(); i < size; i++) {
            final ExpandModel item = mDataList.get(i);
            final ExpandableTextView expandableTextView = new ExpandableTextView(getContext(), item);
            CheckedTextView title = expandableTextView.getTitleView();
            TextView content = expandableTextView.getContentView();
            int finalI = i;
            title.setOnClickListener(new OnClickListener() {
                @Override public void onClick(View v) {
                    if (mAnimator != null && mAnimator.isRunning()) {
                        return;
                    }
                    Log.i(TAG, "点击了" + finalI);
                    title.toggle();
                    doTitleClick(title, content);
                    if (mOnTitleClickListener != null) {
                        mOnTitleClickListener.onTitleClick(finalI, title, content);
                    }
                }
            });

            content.setOnClickListener(new OnClickListener() {
                @Override public void onClick(View v) {
                    if (mOnSubTitleClickListener != null) {
                        mOnSubTitleClickListener.onSubTitleClick(finalI, title, content);
                    }
                }
            });
            addView(expandableTextView);
        }
    }

    private void doTitleClick(CheckedTextView title, TextView content) {
        if (title.isChecked()) {
            title.setTextColor(mCheckedTitleColor);
//            title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.anim.round, 0);
            if (mUseAnimation) {
                animHeightToView(title, content);
            } else {
                content.setVisibility(View.VISIBLE);
            }
        } else {
            title.setTextColor(mTitleColor);
//            title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
            if (mUseAnimation) {
                animHeightToView(title, content);
            } else {
                content.setVisibility(View.GONE);
            }
        }
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private void animHeightToView(CheckedTextView title, TextView content) {
        if (mAnimator.isRunning()) {
            return;
        }
        content.setVisibility(View.VISIBLE);
        mCurrAnimView = content;
        int height = (int) content.getTag();
        if (title.isChecked()) {
            mAnimator.setIntValues(0, height);
        } else {
            mAnimator.setIntValues(height, 0);
        }
        mAnimator.start();
    }

    public static class ExpandModel {
        private String title;
        private String content;
        private String h5Link;
        private boolean isExpand;

        public ExpandModel() {
        }

        public ExpandModel(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public ExpandModel(String title, String content, boolean isExpand) {
            this.title = title;
            this.content = content;
            this.isExpand = isExpand;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getH5Link() {
            return h5Link;
        }

        public void setH5Link(String h5Link) {
            this.h5Link = h5Link;
        }

        public boolean isExpand() {
            return isExpand;
        }

        public void setExpand(boolean expand) {
            isExpand = expand;
        }
    }

    public void setOnTitleClickListener(OnTitleClickListener listener) {
        mOnTitleClickListener = listener;
    }

    public interface OnTitleClickListener {
        void onTitleClick(int position, CheckedTextView parent, TextView content);
    }

    public void setOnContentClickListener(OnContentClickListener listener) {
        mOnSubTitleClickListener = listener;
    }

    public interface OnContentClickListener {
        void onSubTitleClick(int position, CheckedTextView parent, TextView content);
    }

    //垂直包含两个TextView。点击标题TextView能显示隐藏Content TextView。Content TextView 可以扩展成ViewGroup更通用，但是要避免dView嵌套太深。
    private class ExpandableTextView extends LinearLayout {
        private ExpandModel mItem;
        private CheckedTextView mTitle;
        private TextView mContent;

        public ExpandableTextView(Context context, ExpandModel item) {
            this(context, null, item);
        }

        public ExpandableTextView(Context context, @Nullable AttributeSet attrs, ExpandModel item) {
            this(context, attrs, 0, item);
        }

        public ExpandableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, ExpandModel item) {
            super(context, attrs, defStyleAttr);
            init();
            setExpandModel(item);
        }

        private void init() {
            setOrientation(LinearLayout.VERTICAL);
            setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            setDividerDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ll_h_divider));
        }

        public void setExpandModel(ExpandModel item) {
            mItem = item;
            populateData();
        }

        public ExpandModel getExpandModel() {
            return mItem;
        }

        private void populateData() {
            if (getChildCount() > 0) {
                removeAllViews();
            }
            mTitle = buildTitle(mItem.getTitle());
            mContent = buildSubTitle(mItem.getContent());
            addView(mTitle);
            addView(mContent);
        }

        private CheckedTextView buildTitle(String title) {
            CheckedTextView textView = new CheckedTextView(getContext());
            textView.setChecked(mItem.isExpand());
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(45)));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextColor(textView.isChecked() ? mCheckedTitleColor : mTitleColor);
            textView.setTextSize(mTitleTextSize);
            textView.setText(title);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, textView.isChecked() ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down, 0);
            textView.setPadding(mPaddingLeft, dp2px(10), mPaddingRight, dp2px(10));
            return textView;
        }

        private TextView buildSubTitle(String subTitle) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(subTitle);
            textView.setTextColor(mContentColor);
            textView.setTextSize(mContentTextSize);
            textView.setPadding(mPaddingLeft, dp2px(10), mPaddingRight, dp2px(10));
            textView.setVisibility(mTitle.isChecked() ? View.VISIBLE : View.GONE);
            textView.measure(MeasureSpec.makeMeasureSpec(getResources().getDisplayMetrics().widthPixels, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int measuredHeight = textView.getMeasuredHeight();
            Log.i(TAG, textView.getText() + " measuredHeight is " + measuredHeight);
            textView.setTag(textView.getMeasuredHeight());
            return textView;
        }

        public CheckedTextView getTitleView() {
            return mTitle;
        }

        public void setmTitleView(CheckedTextView titleView) {
            this.mTitle = titleView;
        }

        public TextView getContentView() {
            return mContent;
        }

        public void setContentView(TextView contentView) {
            this.mContent = contentView;
        }

    }
}
