package com.king.app.workhelper.fragment;

import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author huoguangxu
 * @since 2017/4/18.
 */

public class DrawableFragment extends AppBaseFragment {
    @BindView(R.id.tv_paint_drawable) TextView mPaintDrawable;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_drawable;
    }

    @Override
    protected void initData() {
        super.initData();

        initPaintDrawable();
    }

    private void initPaintDrawable() {
        PaintDrawable paintDrawable = new PaintDrawable(ContextCompat.getColor(mContext, R.color.chocolate));
        paintDrawable.setShape(new OvalShape());
//        paintDrawable.setCornerRadius(10);
        //[0, 6] 左上角;[10, 10]右上角;[0, 10]右下角;[10, 10]左下角
//        paintDrawable.setCornerRadii(new float[]{0, 6, 10, 10, 10, 0, 30, 30});
        mPaintDrawable.setBackgroundDrawable(paintDrawable);
    }

    
    @OnClick(R.id.tv_paint_drawable)
    public void onPaintDrawableClick(TextView textView) {

    }
}
