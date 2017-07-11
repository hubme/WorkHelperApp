package com.king.applib.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.king.applib.util.ViewIdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VanceKing
 * @since 2017/7/10.
 */

public class MultiStatusView extends FrameLayout {
    private final SparseArray<View> mStatusViews = new SparseArray<>();
    private final List<View> mContentViews = new ArrayList<>();

    private static final RelativeLayout.LayoutParams MATCH_PARENT_LAYOUT_PARAMS =
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);

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

    public void showStatusView(View content) {
        final int contentId = content.getId();
        if (contentId <= 0) {
            content.setId(ViewIdGenerator.generateViewId());
        }
        View currentView = mStatusViews.get(content.getId());
        if (currentView == null) {
            addView(content, 0, MATCH_PARENT_LAYOUT_PARAMS);
            mStatusViews.append(content.getId(), content);
        }

        hiddenContentView();
        for (int i = 0, size = mStatusViews.size(); i < size; i++) {
            int key = mStatusViews.keyAt(i);
            final View view = mStatusViews.get(key);
            if (key == content.getId()) {
                setViewVisibility(view, View.VISIBLE);
            } else {
                setViewVisibility(view, View.INVISIBLE);
            }
        }
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
}
