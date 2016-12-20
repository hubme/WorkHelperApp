package com.king.app.workhelper.fragment;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.ui.customview.BadgeTextView;
import com.king.applib.ui.customview.BadgeView;

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

        BadgeView badgeView = new BadgeView(getContext(), mTextViewHaHa);
        badgeView.setText("2");
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        badgeView.setBadgeMargin(-20, 0);
        badgeView.show();

        myBadgeTextView.drawBadge();
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

    @OnClick(R.id.image2)
    public void testGradientDrawable(ImageView imageView) {

    }
}
