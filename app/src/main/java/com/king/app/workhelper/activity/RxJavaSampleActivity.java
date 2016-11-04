package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.fragment.RxJavaSampleFragment;

/**
 * RxJava.http://blog.csdn.net/lzyzsd/article/details/41833541
 * Created by HuoGuangxu on 2016/11/1.
 */

public class RxJavaSampleActivity extends AppBaseActivity {


    @Override public int getContentLayout() {
        return R.layout.activity_sample_rxjava;
    }

    @Override protected void initData() {
        super.initData();
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, new RxJavaSampleFragment()).commit();
    }
}
