package com.king.app.workhelper.ui.customview.filter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;

import androidx.core.util.Pools;

public class FilterFlowLayout extends FlowLayout {
    private final Pools.SimplePool<CheckedTextView> mPools = new Pools.SimplePool<>(10);

    public FilterFlowLayout(Context context) {
        super(context);
    }

    public FilterFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);

        //throw new IllegalStateException("Already in the pool!")
        try {
            if (child instanceof CheckedTextView) {
                mPools.release((CheckedTextView) child);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CheckedTextView getRecyclerChild() {
        CheckedTextView acquire = mPools.acquire();
        if (acquire != null) {
            acquire.setChecked(false);
            acquire.setOnClickListener(null);
            return acquire;
        }
        return new CheckedTextView(getContext());
    }
}
