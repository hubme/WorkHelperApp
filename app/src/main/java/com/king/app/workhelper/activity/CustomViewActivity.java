package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.ui.customview.SparkView;
import com.king.applib.base.BaseActivity;

import butterknife.BindView;

/**
 * 自定义View
 * Created by HuoGuangxu on 2016/10/17.
 */

public class CustomViewActivity extends BaseActivity {
    @BindView(R.id.spark_view)
    SparkView mSparkView;

    @Override public int getContentLayout() {
        return R.layout.activity_custom;
    }

    @Override protected void initData() {
        super.initData();
        mSparkView.show(this);
    }
}
