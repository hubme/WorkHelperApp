package com.king.app.workhelper.activity;

import android.view.View;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

public class SingleInstanceActivity extends AppBaseActivity {

    @Override protected int getContentLayout() {
        return R.layout.activity_single_instance;
    }

    public void onTextViewClick(View view) {
        openActivity(StandardActivity.class);
    }
}
