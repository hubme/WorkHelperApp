package com.king.app.workhelper.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.service.NormalService;
import com.king.applib.util.ViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.iv_image) ImageView imageView;
    @BindView(R.id.tv_open_qq) TextView textView;

    @Override protected void initData() {
        ViewUtil.setViewBackground(imageView, buildStateListDrawable(this, R.drawable.tab_home_unselect, R.drawable.tab_home_select));
        textView.setTextColor(buildTabTitleSelector(
                ContextCompat.getColor(this, R.color.gray_666666),
                ContextCompat.getColor(this, R.color.chocolate)));
    }

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();
    }

    @OnClick(R.id.iv_image)
    public void onImageClick(ImageView textView) {

    }

    @OnClick(R.id.tv_open_qq)
    public void onQqClick(TextView textView) {
        startService(new Intent(this, NormalService.class));
    }

    private StateListDrawable buildStateListDrawable(Context context, int idNormal, int idPressed) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_selected}, context.getResources().getDrawable(idPressed));
        drawable.addState(new int[]{android.R.attr.state_pressed}, context.getResources().getDrawable(idPressed));
        drawable.addState(new int[]{android.R.attr.state_enabled}, context.getResources().getDrawable(idNormal));
        drawable.addState(new int[]{}, context.getResources().getDrawable(idNormal));
        return drawable;
    }

    private ColorStateList buildTabTitleSelector(int normal, int pressed) {
        int[] colors = new int[]{pressed, pressed, normal};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_focused, android.R.attr.state_selected};
        states[2] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

}
