package com.king.app.workhelper.model;

import android.widget.TextView;

/**
 * 内存泄漏测试
 * Created by VanceKing on 2016/12/3.
 */

public class LeakTestModel {
    private static LeakTestModel sInstance;
    private TextView mRetainedTextView;

    public static LeakTestModel getInstance() {
        if (sInstance == null) {
            sInstance = new LeakTestModel();
        }
        return sInstance;
    }

    public void setRetainedTextView(TextView textView) {
        mRetainedTextView = textView;
    }
}
