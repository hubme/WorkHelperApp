package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.king.app.workhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VanceKing
 * @since 2017/10/13.
 */

public class TabLayout extends LinearLayout {
    private final List<TabItem> mTabItems = new ArrayList<>();
    private final SparseArray<TabView> mPositionMapper = new SparseArray<>();
    private int mCurrentTabPosition = -1;
    private OnTabClickListener mOnTabClickListener;

    private int mTitleSize;
    private int mSelectedTitleColor;
    private int mUnselectedTitleColor;
    private int mImageWidth;
    private int mImageHeight;
    private int mTextTopMargin;
    private int mBackgroundRes;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabLayout);
            mSelectedTitleColor = typedArray.getColor(R.styleable.TabLayout_tabSelectedTitleColor, Color.BLACK);
            mUnselectedTitleColor = typedArray.getColor(R.styleable.TabLayout_tabUnselectedTitleColor, Color.BLACK);
            mTitleSize = (int) typedArray.getDimension(R.styleable.TabLayout_tabTitleSize, dip2px(14));
            mImageWidth = typedArray.getDimensionPixelSize(R.styleable.TabLayout_tabImageWidth, -1);
            mImageHeight = typedArray.getDimensionPixelSize(R.styleable.TabLayout_tabImageHeight, -1);
            mTextTopMargin = typedArray.getDimensionPixelSize(R.styleable.TabLayout_tabTitleTopMargin, 0);
            mBackgroundRes = typedArray.getResourceId(R.styleable.TabLayout_tabBackgroundRes, -1);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void setupTabs(List<TabItem> tabs) {
        if (isTabsEmpty(tabs)) {
            return;
        }
        if (!mTabItems.isEmpty()) {
            mTabItems.clear();
        }
        mTabItems.addAll(tabs);
        for (int i = 0, size = mTabItems.size(); i < size; i++) {
            final TabItem tab = mTabItems.get(i);
            final TabView tabView = new TabView(getContext(), tab);
            int finalI = i;
            tabView.setOnClickListener(new OnClickListener() {
                @Override public void onClick(View v) {
                    if (mOnTabClickListener != null) {
                        mOnTabClickListener.onTabSelected(finalI, tab, tabView);
                    }
                }
            });
            addView(tabView);
            mPositionMapper.put(i, tabView);
        }
    }

    public void setSelectedTab(int position) {
        if (!validatePosition(position) || mCurrentTabPosition == position) {
            return;
        }
        int mLastSelectedTabPosition = mCurrentTabPosition;
        //取消上次选中的tab
        if (validatePosition(mLastSelectedTabPosition)) {
            TabView tabView = mPositionMapper.get(mLastSelectedTabPosition);
            tabView.setImageRes(mTabItems.get(mLastSelectedTabPosition).unselectedImageRes);
            tabView.setTitleTextColor(mUnselectedTitleColor);
        }
        //选中当前tab
        TabView tabView = mPositionMapper.get(position);
        tabView.setImageRes(mTabItems.get(position).selectedImageRes);
        tabView.setTitleTextColor(mSelectedTitleColor);

        mCurrentTabPosition = position;
    }

    public TabView getTab(int position) {
        return validatePosition(position) ? mPositionMapper.get(position) : null; 
    }

    public static class TabItem {
        public int id;
        public String title;
        @DrawableRes public int selectedImageRes;
        @DrawableRes public int unselectedImageRes;
        public String imageUrl;
        public int width;

        public TabItem() {
        }

        public TabItem(int id) {
            this.id = id;
        }

        public TabItem(int id, String title, int selectedImageRes) {
            this.id = id;
            this.title = title;
            this.selectedImageRes = selectedImageRes;
        }

        public TabItem(int id, String title, int selectedImageRes, int unselectedImageRes) {
            this.id = id;
            this.title = title;
            this.selectedImageRes = selectedImageRes;
            this.unselectedImageRes = unselectedImageRes;
        }
    }

    public class TabView extends LinearLayout {
        private TabItem tab;
        private SimpleDraweeView mImageView;
        private TextView mTextView;

        public TabView(Context context, TabItem tab) {
            this(context, null, tab);
        }

        public TabView(Context context, @Nullable AttributeSet attrs, TabItem tab) {
            this(context, attrs, 0, tab);
        }

        public TabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, TabItem tab) {
            super(context, attrs, defStyleAttr);
            this.tab = tab;
            init();
        }

        private void init() {
            LayoutParams itemParams;
            if (tab.width != 0) {
                itemParams = new LayoutParams(tab.width, LayoutParams.MATCH_PARENT);
            } else {
                itemParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
                itemParams.weight = 1;
            }

            setLayoutParams(itemParams);
            setOrientation(LinearLayout.VERTICAL);
            setGravity(Gravity.CENTER);
            if (mBackgroundRes > 0) {
                setBackgroundResource(mBackgroundRes);
            }

            mImageView = new SimpleDraweeView(getContext());
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mImageWidth, mImageHeight));
            mImageView.setBackgroundResource(tab.unselectedImageRes);
            mImageView.setScaleType(ImageView.ScaleType.CENTER);
            mImageView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
            addView(mImageView);

            LayoutParams titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            titleParams.setMargins(0, mTextTopMargin, 0, 0);
            mTextView = new TextView(getContext());
            mTextView.setLayoutParams(titleParams);
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            mTextView.setText(tab.title);
            mTextView.setTextColor(mUnselectedTitleColor);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
            addView(mTextView);
        }

        public void setImageRes(@DrawableRes int resId) {
            mImageView.setBackgroundResource(resId);
        }

        public void setTabTitle(String title) {
            mTextView.setText(title);
        }

        public void setTabTitle(@StringRes int resId) {
            mTextView.setText(resId);
        }

        public void setTitleTextSize(@DimenRes int resId) {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(resId));
        }
        
        public void setTitleTextColor(@ColorInt int colorInt) {
            mTextView.setTextColor(colorInt);
        }

        public void setBackgroundRes(@DrawableRes int resId) {
            setBackgroundResource(resId);
        }
    }

    private boolean isTabsEmpty(List<TabItem> tabs) {
        return tabs == null || tabs.isEmpty();
    }

    private boolean validatePosition(int position) {
        return position >= 0 && position < mTabItems.size();
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setOnTabClickListener(OnTabClickListener listener) {
        mOnTabClickListener = listener;
    }

    public interface OnTabClickListener {
        void onTabSelected(int position, TabItem tabItem, TabView tabView);
    }
}
