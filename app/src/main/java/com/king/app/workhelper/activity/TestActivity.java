package com.king.app.workhelper.activity;

import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.BindView;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.view1) View view1;
    @BindView(R.id.view2) ScrollView view2;

    @Override protected void initInitialData() {
        super.initInitialData();
    }

    @Override protected void initData() {
    }

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();
        view1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Log.i("aaa", "view1");
            }
        });


    }

}
