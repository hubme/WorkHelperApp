package com.king.app.workhelper.ui.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.king.app.workhelper.R;
import com.king.applib.log.Logger;
import com.king.applib.util.ImageUtil;
import com.king.applib.util.ScreenUtil;

import static android.view.View.MeasureSpec.getMode;

/**
 * Created by HuoGuangxu on 2016/10/27.
 */

public class LoadingView extends RelativeLayout {
    private static final int DEFAULT_TOTAL_TIME = 60;

    private Context mContext;
    private ImageView mBoyIv;
    private AnimationDrawable mBoyRunAnim;
    private ImageView mCoinImage;
    private AnimatorSet mAnimatorSet;
    private int mTotalTime = DEFAULT_TOTAL_TIME;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Logger.i("onMeasure()");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else {
            //如果宽高都是包裹内容
            if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
                //我们将高度设置为所有子View的高度相加，宽度设为子View中最大的宽度
                int height = getTotleHeight();
                int width = getMaxChildWidth();
                setMeasuredDimension(width, height);
            } else if (heightMode == MeasureSpec.AT_MOST) {
                //宽度设置为ViewGroup自己的测量宽度，高度设置为所有子View的高度总和
                setMeasuredDimension(widthSize, getTotleHeight());
            }else if (widthMode == MeasureSpec.AT_MOST) {//如果只有宽度是包裹内容
                //宽度设置为子View中宽度最大的值，高度设置为ViewGroup自己的测量值
                setMeasuredDimension(getMaxChildWidth(), heightSize);
            }
        }
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_loading, this, true);
    }

    private void initView() {
        Logger.i("initView()");

        mCoinImage = (ImageView) findViewById(R.id.iv_coin);
        mBoyIv = (ImageView) findViewById(R.id.iv_boy);
        mBoyRunAnim = (AnimationDrawable) mBoyIv.getBackground();
        mBoyRunAnim.setOneShot(true);

        ObjectAnimator coinRollingAnimator = ObjectAnimator.ofFloat(mCoinImage, "rotation", 0f, 360f);

        float translation = ScreenUtil.getScreenWidth(mContext) - 2 * ImageUtil.getImageWidth(mContext, R.drawable.little_boy_01);
        Logger.i("屏幕宽度：" + ScreenUtil.getScreenWidth(mContext) + ";图片宽度：" + ImageUtil.getImageWidth(mContext, R.drawable.little_boy_01) + ";translation: " + translation);
        ObjectAnimator coinTranslateAnimator = ObjectAnimator.ofFloat(mCoinImage, "translationX", 0, translation);

        ObjectAnimator boyTranslateAnimator = ObjectAnimator.ofFloat(mBoyIv, "translationX", 0, translation);
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(2000);
        mAnimatorSet.setInterpolator(new LinearInterpolator());
        mAnimatorSet.playTogether(boyTranslateAnimator, coinTranslateAnimator, coinRollingAnimator);
    }

    private void playBoyRunningAnim() {
        mBoyRunAnim.start();
    }

    private void pauseBoyRunningAnim() {
        mBoyRunAnim.stop();
    }

    public void start() {
        playBoyRunningAnim();
        mAnimatorSet.start();
    }

    public void pause() {
        pauseBoyRunningAnim();
        mAnimatorSet.cancel();
    }

    public void setTotalTime(int totalTime) {
        if (totalTime > 0) {
            mTotalTime = totalTime;
        }
    }

    public void setStep(int step) {

    }

    /***
     * 获取子View中宽度最大的值
     */
    private int getMaxChildWidth() {
        int childCount = getChildCount();
        int maxWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getMeasuredWidth() > maxWidth)
                maxWidth = childView.getMeasuredWidth();

        }

        return maxWidth;
    }

    /***
     * 将所有子View的高度相加
     **/
    private int getTotleHeight() {
        int childCount = getChildCount();
        int height = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            height += childView.getMeasuredHeight();

        }

        return height;
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }
}
