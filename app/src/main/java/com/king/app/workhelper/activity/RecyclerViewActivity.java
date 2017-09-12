package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.fragment.RecyclerEntryFragment;

/**
 * @author VanceKing
 * @since 2017/3/30.
 */

public class RecyclerViewActivity extends AppBaseActivity {
    
    @Override public int getContentLayout() {
        return R.layout.activity_recycler_view;
    }

    @Override protected void initData() {
        super.initData();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.layout_container, new RecyclerEntryFragment())
                .commit();

    }
}
