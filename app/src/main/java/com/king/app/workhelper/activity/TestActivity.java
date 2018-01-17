package com.king.app.workhelper.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.ViewUtil;

import butterknife.BindView;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.image_view) ImageView mImageView;
    @BindView(R.id.tv_haha) TextView mTextView;

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Log.i("aaa", "ImageView");
            }
        });
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Log.i("aaa", "TextView");
            }
        });

        if (ExtendUtil.isApiAvailable(Build.VERSION_CODES.LOLLIPOP)) {
            int normalColor = ContextCompat.getColor(this, R.color.gray);
            RippleDrawable drawable = buildRippleDrawable(normalColor, ContextCompat.getColor(this, R.color.mediumvioletred));
            drawable.setHotspot(100, 100);
            ViewUtil.setViewBackground(mTextView, drawable);
            ViewUtil.setViewBackground(mImageView, buildRippleDrawable(normalColor, ContextCompat.getColor(this, R.color.goldenrod)));

        }

    }

    private RippleDrawable buildRippleDrawable(int normalColor, int pressedColor) {
        if (!ExtendUtil.isApiAvailable(Build.VERSION_CODES.LOLLIPOP)) {
            return null;
        }
        ColorStateList stateList = createColorStateList(normalColor, pressedColor, pressedColor, normalColor);
        RippleDrawable rippleDrawable = new RippleDrawable(stateList, null, null);
        rippleDrawable.mutate();
//        StateListDrawable selector = new StateListDrawable();
//        ripple.setDrawableByLayerId(android.R.id.background, selector);
        return rippleDrawable;
    }

    /**
     * 设置不同状态时其文字颜色
     *
     * @see ""
     */
    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        return new ColorStateList(states, colors);
    }

    /**
     * Make a dark color to press effect
     * 自动计算得到按下的颜色，如果不满足需求可重写
     */
    protected int getPressedColor(int normalColor) {
        int alpha = 255;
        int r = (normalColor >> 16) & 0xFF;
        int g = (normalColor >> 8) & 0xFF;
        int b = (normalColor >> 1) & 0xFF;
        r = (r - 50 < 0) ? 0 : r - 50;
        g = (g - 50 < 0) ? 0 : g - 50;
        b = (b - 50 < 0) ? 0 : b - 50;
        return Color.argb(alpha, r, g, b);
    }

    /**
     * 设置按下后的样式（颜色，描边）
     */
    /*private void setPressedDrawable(StateListDrawable selector) {
        if (pressedColor == DEFAULT_COLOR) {
            pressedColor = isSmart ? getPressedColor(normalColor) : pressedColor;
        }
        setColorAndStroke(pressed, pressedColor, pressedStrokeColor, pressedStrokeWidth, false);
        // 给selector设置pressed的状态
        selector.addState(new int[]{android.R.attr.state_pressed}, pressed);
        selector.addState(new int[]{android.R.attr.state_focused}, pressed);
        pressed.mutate();
    }*/
}
