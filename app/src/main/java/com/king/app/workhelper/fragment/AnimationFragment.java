package com.king.app.workhelper.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.animation.Rotate3dAnimation;
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
    @BindView(R.id.tv_haha) TextView mHaHa;
    @BindView(R.id.tv_expandable) TextView mExpandableTv;
    @BindView(R.id.vf_ad) ViewFlipper mAdVf;
    @BindView(R.id.progress_bar_flower) ProgressBar mFlowerLoading;
    @BindView(R.id.btn_vector) Button mVectorBtn;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_animation;
    }

    @Override
    protected void initContentView(View rootView) {
        super.initContentView(rootView);

        //解决无法使用app:srcCompact属性的情况
        VectorDrawableCompat drawableCompat = VectorDrawableCompat.create(getResources(), R.drawable.ic_location, null);
        mHaHa.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableCompat, null);

//        mVectorBtn.setBackgroundDrawable(VectorDrawableCompat.create(getResources(), R.drawable.ic_v_not, null));
    }

    @Override
    protected void initData() {
        super.initData();
        mAdVf.addView(LayoutInflater.from(mContext).inflate(R.layout.layout_notice_board, null));
        mAdVf.addView(LayoutInflater.from(mContext).inflate(R.layout.layout_notice_board, null));
        mAdVf.addView(LayoutInflater.from(mContext).inflate(R.layout.layout_notice_board, null));
        mAdVf.startFlipping();
    }

    @OnClick(R.id.btn_show_anim)
    public void onBtnShowAnim() {
        AnimationUtil.animHeightToView(mExpandableTv, 0, 200, 500);
    }

    @OnClick(R.id.tv_expandable)
    public void onExpandableClick(View view) {
        alpahAnimator(view);
    }

    //点击阴影效果
    @OnClick(R.id.iv_icon_edit)
    public void onEditImageClick() {

    }

    //    @OnClick(R.id.iv_animated_vector_line)
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


    @OnClick(R.id.image_3d)
    public void on3DImageClick(ImageView view) {
        applyRotation(view, 0, 180);
    }

    private void applyRotation(ImageView view, float start, float end) {
        // 计算中心点
        final float centerX = view.getWidth() / 2.0f;
        final float centerY = view.getHeight() / 2.0f;

        final Rotate3dAnimation rotation = new Rotate3dAnimation(mActivity, start, end, centerX, centerY, 1.0f, true);
        rotation.setDuration(1500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setImageResource(R.mipmap.banner2);
            }
        });
        view.startAnimation(rotation);
    }

    private void alpahAnimator(View view) {
        //第一个参数为 view对象，第二个参数为 动画改变的类型，第三，第四个参数依次是开始透明度和结束透明度。  
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        alpha.setDuration(2000);//设置动画时间  
        alpha.setInterpolator(new DecelerateInterpolator());//设置动画插入器，减速  
        alpha.setRepeatCount(-1);//设置动画重复次数，这里-1代表无限  
        alpha.setRepeatMode(ValueAnimator.REVERSE);//设置动画循环模式。  
        alpha.start();//启动动画。 
    }

    private void scaleAnimator(View view) {
        AnimatorSet animatorSet = new AnimatorSet();//组合动画  
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f);

        animatorSet.setDuration(2000);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(scaleX).with(scaleY);//两个动画同时开始  
        animatorSet.start();
    }

    //translationY
    private void translateAnimator(View view) {
        ObjectAnimator translationUp = ObjectAnimator.ofFloat(view, "Y", view.getY(), 0);
        translationUp.setInterpolator(new DecelerateInterpolator());
        translationUp.setDuration(1000);
        translationUp.start();
    }

    private void rotateAnimator(View view) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "rotationX", 0f, 180f);
        anim.setDuration(2000);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "rotationX", 180f, 0f);
        anim2.setDuration(2000);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(view, "rotationY", 0f, 180f);
        anim3.setDuration(2000);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(view, "rotationY", 180f, 0f);
        anim4.setDuration(2000);

        set.play(anim).before(anim2); //先执行anim动画之后在执行anim2  
        set.play(anim3).before(anim4);
        set.start();
    }
}