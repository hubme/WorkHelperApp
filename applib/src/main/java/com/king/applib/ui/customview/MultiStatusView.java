package com.king.applib.ui.customview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @author huoguangxu
 * @since 2017/7/10.
 */

public class MultiStatusView extends RelativeLayout{
    private final SparseArray<View> mStatusViews;
    private Context mContext;

    public MultiStatusView(Context context) {
        this(context, null);
    }

    public MultiStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mStatusViews = new SparseArray<>();
        mContext = context;
        init();        
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void init() {
    }
    
    public void showView(@LayoutRes int layoutRes) {
        hiddenChildren();
        View currentView = mStatusViews.get(layoutRes);
        if (currentView == null) {
            View view = generateView(layoutRes);
            addView(view);
            mStatusViews.append(layoutRes, view);
        } else {
            currentView.setVisibility(VISIBLE);
        }
    }

    private View generateView(@LayoutRes int layoutRes) {
        return LayoutInflater.from(mContext).inflate(layoutRes, null);
    }

    private void hiddenChildren() {
        for (int i = 0, size = getChildCount(); i < size; i++) {
            final View child = getChildAt(i);
            if (child.isShown()) {
                child.setVisibility(INVISIBLE);
            }
        }
    }

}
