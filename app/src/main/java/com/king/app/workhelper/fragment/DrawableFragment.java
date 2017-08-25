package com.king.app.workhelper.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.util.ExtendUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author huoguangxu
 * @since 2017/4/18.
 */

public class DrawableFragment extends AppBaseFragment {
    @BindView(R.id.tv_paint_drawable) TextView mPaintDrawable;
    @BindView(R.id.tv_drawable_bg) TextView mDrawableBgTv;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_drawable;
    }

    @Override
    protected void initData() {
        super.initData();

        initPaintDrawable();
    }

    @Override protected void initContentView(View rootView) {
        super.initContentView(rootView);
        mDrawableBgTv.setBackgroundDrawable(getDrawable());
    }

    private void initPaintDrawable() {
        PaintDrawable paintDrawable = new PaintDrawable(ContextCompat.getColor(mContext, R.color.chocolate));
        paintDrawable.setShape(new OvalShape());
//        paintDrawable.setCornerRadius(10);
        //[0, 6] 左上角;[10, 10]右上角;[0, 10]右下角;[10, 10]左下角
//        paintDrawable.setCornerRadii(new float[]{0, 6, 10, 10, 10, 0, 30, 30});
        mPaintDrawable.setBackgroundDrawable(paintDrawable);
    }

    private Drawable getDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(ContextCompat.getColor(mContext, R.color.white));
        int borderColor = ExtendUtil.parseColor("#D2691E", Color.parseColor("#b10600"));
        drawable.setStroke(ExtendUtil.dp2px(1), borderColor);
        drawable.setCornerRadius(ExtendUtil.dp2px(21));
        return drawable; 
    }

    @OnClick(R.id.tv_paint_drawable)
    public void onPaintDrawableClick(TextView textView) {

    }
}
