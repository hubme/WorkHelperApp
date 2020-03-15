package com.king.applib.ui.customview;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 多状态View。<br/>
 * 每个状态View添加到此ViewGroup下，同时只显示一个View，其它 GONE.
 *
 * @author VanceKing
 * @since 2017/7/10.
 */

public class MultiStatusView extends FrameLayout {
    private final SparseArray<View> mStatusViews = new SparseArray<>();
    private final List<View> mContentViews = new ArrayList<>();

    private static final FrameLayout.LayoutParams MATCH_PARENT_LAYOUT_PARAMS = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT);

    public MultiStatusView(Context context) {
        this(context, null);
    }

    public MultiStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0, size = getChildCount(); i < size; i++) {
            mContentViews.add(getChildAt(i));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public View showStatusView(@LayoutRes int layoutRes) {
        View statusView = mStatusViews.get(layoutRes);
        if (statusView == null) {
            statusView = generateView(layoutRes);
            addView(statusView, MATCH_PARENT_LAYOUT_PARAMS);
            mStatusViews.put(layoutRes, statusView);
        }

        hiddenContentView();
        for (int i = 0, size = mStatusViews.size(); i < size; i++) {
            final int key = mStatusViews.keyAt(i);
            final View view = mStatusViews.get(key);
            if (layoutRes == key) {
                setViewVisibility(view, View.VISIBLE);
            } else {
                setViewVisibility(view, View.GONE);
            }
        }
        return statusView;
    }

    public void showContentView() {
        hiddenAllStatusViews();
        for (View view : mContentViews) {
            setViewVisibility(view, View.VISIBLE);
        }
    }

    private void hiddenContentView() {
        for (View view : mContentViews) {
            setViewVisibility(view, View.INVISIBLE);
        }
    }

    private void hiddenAllStatusViews() {
        for (int i = 0, size = mStatusViews.size(); i < size; i++) {
            final View view = mStatusViews.get(mStatusViews.keyAt(i));
            setViewVisibility(view, View.INVISIBLE);
        }
    }

    private void setViewVisibility(View view, int visibility) {
        if (view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    private View generateView(@LayoutRes int layoutRes) {
        return LayoutInflater.from(getContext()).inflate(layoutRes, null);
    }
}
