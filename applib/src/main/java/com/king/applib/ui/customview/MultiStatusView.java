package com.king.applib.ui.customview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.king.applib.util.ViewIdGenerator;

/**
 * @author huoguangxu
 * @since 2017/7/10.
 */

public class MultiStatusView extends FrameLayout {
    private final SparseArray<View> mStatusViews = new SparseArray<>();
    private static final RelativeLayout.LayoutParams MATCH_PARENT_LAYOUT_PARAMS =
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
    private Context mContext;
    private int mCurrentLayoutId;

    public MultiStatusView(Context context) {
        this(context, null);
    }

    public MultiStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void init() {
    }

    public void showContentView(@LayoutRes int layoutRes) {
        if (mCurrentLayoutId == layoutRes) {
            return;
        }
        View currentView = mStatusViews.get(layoutRes);
        if (currentView == null) {
            View view = generateView(layoutRes);
            addView(view, MATCH_PARENT_LAYOUT_PARAMS);
            mStatusViews.append(layoutRes, view);
        }

        for (int i = 0, size = mStatusViews.size(); i < size; i++) {
            int key = mStatusViews.keyAt(i);
            final View view = mStatusViews.get(key);
            if (key == layoutRes) {
                view.setVisibility(VISIBLE);
            } else {
                view.setVisibility(INVISIBLE);
            }
        }
        mCurrentLayoutId = layoutRes;
    }

    private View generateView(@LayoutRes int layoutRes) {
        return LayoutInflater.from(mContext).inflate(layoutRes, null);
    }

    public void showContentView(View view) {
        if (view == null) {
            throw new RuntimeException("content view can not be null");
        }
        if (view.getId() < 0) {
            view.setId(ViewIdGenerator.generateViewId());
        }
        // TODO: 2017/7/11 0011
    }
}
