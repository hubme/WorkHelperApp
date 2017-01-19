package com.king.app.workhelper.fragment;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.util.AnimationUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 动画
 *
 * @author huoguangxu
 * @since 2017/1/17.
 */

public class AnimationFragment extends AppBaseFragment {
    @BindView(R.id.tv_expandable)
    TextView mExpandableTv;

    @BindView(R.id.vf_ad)
    ViewFlipper mAdVf;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_animation;
    }

    @Override
    protected void initData() {
        super.initData();
        mAdVf.addView(LayoutInflater.from(mContext).inflate(R.layout.layout_notice_board, null));
        mAdVf.addView(LayoutInflater.from(mContext).inflate(R.layout.layout_notice_board, null));
        mAdVf.addView(LayoutInflater.from(mContext).inflate(R.layout.layout_notice_board, null));
//        mAdVf.startFlipping();
    }

    @OnClick(R.id.btn_show_anim)
    public void onBtnShowAnim() {
        AnimationUtil.animHeightToView(mExpandableTv, 0, 200, 500);
    }

    @OnClick(R.id.tv_expandable)
    public void onExpandableClick() {
        AnimationUtil.animHeightToView(mExpandableTv, 200, 0, 500);
    }


    @OnClick(R.id.iv_icon_edit)
    public void onEditImageClick() {

    }

    @OnClick(R.id.iv_animated_vector_line)
    public void onAnimatedLineClick(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    @OnClick(R.id.iv_animated_vector_heart)
    public void onAnimatedHeartClick(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //此时View已销毁,mAdVf == null;
//        mAdVf.stopFlipping();
    }
}