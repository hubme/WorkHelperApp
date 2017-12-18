package com.king.app.workhelper.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.ui.customview.StartDrawableTextView;
import com.king.applib.util.ExtendUtil;

import butterknife.BindView;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.start_text_view) StartDrawableTextView textView;

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }
    
    @Override protected void initContentView() {
        super.initContentView();
        
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#3b7aff"), Color.parseColor("#b8d6ff")});
//        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.aaa);
        textView.setStartImage(drawable, ExtendUtil.dp2px(10), ExtendUtil.dp2px(10));
    }
}
