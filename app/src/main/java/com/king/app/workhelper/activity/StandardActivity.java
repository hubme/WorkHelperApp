package com.king.app.workhelper.activity;

import android.view.View;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

public class StandardActivity extends AppBaseActivity {

    @Override protected int getContentLayout() {
        return R.layout.activity_standard;
    }

    public void onTextViewClick(View view) {
        openActivity(StandardActivity.class);
    }
}
