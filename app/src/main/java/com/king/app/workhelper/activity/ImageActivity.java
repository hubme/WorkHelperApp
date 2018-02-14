package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.UltimateBar;

/**
 * @author VanceKing
 * @since 2017/8/22.
 */

public class ImageActivity extends AppBaseActivity {
    @Override protected int getContentLayout() {
        return R.layout.activity_image;
    }

    @Override protected void initContentView() {
        super.initContentView();

        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();
    }
}
