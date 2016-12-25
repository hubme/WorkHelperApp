package com.king.app.workhelper.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.ui.customview.BadgeTextView;
import com.king.app.workhelper.ui.customview.CircleCenterTextView;
import com.king.app.workhelper.ui.customview.HorizontalTagView;
import com.king.app.workhelper.ui.customview.SimpleDrawable;
import com.king.applib.log.Logger;
import com.king.applib.ui.customview.BadgeView;
import com.king.applib.ui.customview.BadgeView2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 自定义View
 * Created by VanceKing on 2016/11/26 0026.
 */

public class CustomViewFragment extends AppBaseFragment {

    @BindView(R.id.tv_haha)
    public TextView mTextViewHaHa;

    @BindView(R.id.gradient_drawable)
    public ImageView mGradientDrawable;

    @BindView(R.id.my_badge)
    public BadgeTextView myBadgeTextView;

    private BadgeView mBadgeView;

    @BindView(R.id.htv_tag)
    public HorizontalTagView mTagView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_custom;
    }

    @Override
    protected void initContentView(View rootView) {
        super.initContentView(rootView);

        //颜色渐变
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{0xFFFF0000, 0xFF00FF00, 0xFF0000FF});
        mGradientDrawable.setImageDrawable(drawable);

    }

    @Override
    protected void initData() {
        super.initData();

        mBadgeView = new BadgeView(getContext(), mTextViewHaHa);
        mBadgeView.setText("2");
        mBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        mBadgeView.setBadgeMargin(-20, 0);
        mBadgeView.show();

        BadgeView2 badgeView2 = new BadgeView2(getContext());
        badgeView2.setBadgeCount(15);
        badgeView2.setTargetView(myBadgeTextView);

        mTagView.setOnTagCheckedListener(new HorizontalTagView.OnTagCheckedListener() {
            @Override
            public void onTagChecked(boolean isChecked, String tag) {
                Logger.i("isChecked: " + isChecked + "; tag: " + tag);
                Toast.makeText(getContext(), tag + "  " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });

    }


    public Drawable wrap(int icon) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), icon, getActivity().getTheme());
        ColorStateList mTint = ResourcesCompat.getColorStateList(getResources(), R.color.tab, getActivity().getTheme());
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable.mutate());
            DrawableCompat.setTintList(drawable, mTint);
        }
        return drawable;
    }

    @OnClick(R.id.tv_hehe)
    public void clickHeHe(TextView textView) {
//        ColorStateList csl = getResources().getColorStateList(R.color.button_text);
        ColorStateList csl = ResourcesCompat.getColorStateList(getResources(), R.color.button_text, getActivity().getTheme());
        textView.setTextColor(csl);
    }

    @OnClick(R.id.tv_haha)
    public void testGradientDrawable(TextView textView) {
        Drawable drawable = getShapeDrawable();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(drawable);
        } else {
            textView.setBackgroundDrawable(drawable);
        }
        if (!mTagView.isTagExists("北京")) {
            mTagView.addTag(true, "北京");
        } else {
            Toast.makeText(getContext(), "tag已存在", Toast.LENGTH_SHORT).show();
        }
    }

    private Drawable getGradientDrawable() {
        //渐变色
//        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{0xFFFF0000, 0xFF00FF00, 0xFF0000FF});
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(Color.BLUE);
        gd.setCornerRadius(15);
//        gd.setStroke(1, Color.parseColor("#FFFF0000"));//边框
        gd.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        return gd;
    }

    private Drawable getShapeDrawable() {
//        Drawable drawable = new ShapeDrawable(new OvalShape());
//        return drawable;
        return new SimpleDrawable.Builder(getContext())
                .setShape(SimpleDrawable.RADIAL)
                .setBackgroundColor(R.color.colorAccent)
                .setCornerRadius(45)
                .build();
    }

    @OnClick(R.id.tv_center)
    public void onCenterTextViewClick(CircleCenterTextView view) {
        view.setOrientation(CircleCenterTextView.Horizontal);
    }
}
