package com.king.app.workhelper.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.ui.customview.FormInputView;
import com.king.app.workhelper.ui.dialog.SimpleDialog;
import com.king.app.workhelper.ui.dialog.SimpleDialogFragment;
import com.king.applib.ui.customview.BarrageView;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.ViewUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.form_input_view) FormInputView mFormInputView;
    @BindView(R.id.tv_open_qq) TextView mTestTv;
    @BindView(R.id.barrage_view) BarrageView mBarrageView;
    @BindView(R.id.panel_root) LinearLayout mBulletRoot;
    private Animation animation;
    private SimpleDialog simpleDialog;
    private AlertDialog alertDialog;
    private boolean yes;

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();
        mFormInputView.setInputType(FormInputView.EDIT_INPUT_TYPE)
                .setTitle("哈哈哈")
                .setContentHint("请输入内容")
                .setContentText("呵呵呵");

        GradientDrawable drawable1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.parseColor("#d3a574"), Color.parseColor("#b08f64")});
        drawable1.setCornerRadius(ExtendUtil.dp2px(20));

        GradientDrawable drawable2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.parseColor("#c471f5"), Color.parseColor("#fa71cd")});
        drawable2.setCornerRadius(ExtendUtil.dp2px(20));


        GradientDrawable drawable3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.parseColor("#6e7ff3"), Color.parseColor("#5753c9"), Color.parseColor("#3d4e81")});
        drawable3.setCornerRadius(ExtendUtil.dp2px(20));

        TextView textView1 = buildTextView("小枫叶刚刚完成认证，获得30万贷款额度");
        ViewUtil.setViewBackground(textView1, drawable1);
        mBulletRoot.addView(textView1);

        TextView textView2 = buildTextView("白玫瑰刚刚完成认证，获得2万贷款额度");
        ViewUtil.setViewBackground(textView2, drawable2);
        mBulletRoot.addView(textView2);

        TextView textView3 = buildTextView("白玫瑰刚刚完成认证，获得5万贷款额度");
        ViewUtil.setViewBackground(textView3, drawable3);
        mBulletRoot.addView(textView3);

        animation = new TranslateAnimation(0, 1000, 0, 0);
        animation.setDuration(2000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatMode(Animation.RESTART);//必须设置 mRepeatCount > 0 或 Animation.INFINITE(效果同mRepeatCount < 0)才有效
        animation.setRepeatCount(Animation.INFINITE);//重复repeatCount次，一共执行repeatCount+1次
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
//        mTestTv.setAnimation(animation);//会执行动画

        ArrayList<Drawable> drawableList = new ArrayList<Drawable>() {
            {
                add(drawable1);
                add(drawable2);
                add(drawable3);
            }
        };

        mBarrageView.setBackgroundDrawables(drawableList);
//        mBarrageView.setShowRange(100, 200);
        mBarrageView.addBarrage(new BarrageView.Barrage("小枫叶刚"));
        mBarrageView.addBarrage(new BarrageView.Barrage("白玫瑰刚刚完成认证额度"));
        mBarrageView.addBarrage(new BarrageView.Barrage("红玫瑰刚刚完成认证，获得10万贷款额度"));
        mBarrageView.addBarrage(new BarrageView.Barrage("哈哈哈哈航啊哈哈哈哈哈哈哈航啊哈哈哈哈哈哈哈航啊哈哈哈哈哈哈哈航啊哈哈哈AAA"));

    }

    private TextView buildTextView(String text) {
        TextView textView = new TextView(this);
        textView.setPadding(ExtendUtil.dp2px(10), ExtendUtil.dp2px(6), ExtendUtil.dp2px(10), ExtendUtil.dp2px(6));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setText(text);
        return textView;
    }

    @OnClick(R.id.tv_open_qq)
    public void onTestViewClick(TextView textView) {
//        mTestTv.startAnimation(animation);
//        textView.animate().translationX(500).setDuration(2000).start();
        yes = !yes;
        showAlertDialog(yes);
    }

    private void showDialog() {
        if (simpleDialog == null) {
            simpleDialog = new SimpleDialog.Builder()
                    .setMessage("哈哈哈")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            showToast("取消");
                        }
                    })
                    .build();
        }
        simpleDialog.showDialog(getSupportFragmentManager());
    }

    private void showDialog2() {
        SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        View view = View.inflate(this, R.layout.layout_dialog_sample_content, null);
        dialogFragment.setView(view);
        dialogFragment.show(getSupportFragmentManager(), "aaa");
    }

    private void showAlertDialog(boolean yes) {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            showToast("确定");
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            showToast("取消");
                        }
                    })
                    .setNeutralButton("不确定?", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            showToast("不确定");
                        }
                    })
                    .create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override public void onShow(DialogInterface dialog) {
                    //必须在show()之后还能获取到Button
                    Button negativeBtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    negativeBtn.setTextSize(15);
                    negativeBtn.setTextColor(ContextCompat.getColor(TestActivity.this, R.color.chocolate));
                    Button positiveBtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    positiveBtn.setTextSize(15);
                    positiveBtn.setTextColor(ContextCompat.getColor(TestActivity.this, R.color.gold));
                }
            });
        }
        if (yes) {
            alertDialog.setView(buildAlertDialogView("哈哈哈"));
        } else {
            alertDialog.setView(buildAlertDialogView("呵呵呵"));
        }
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    private View buildAlertDialogView(String message) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(this);
        textView.setTextSize(15);
        textView.setLayoutParams(params);
        textView.setLineSpacing(0, 1.4f);
        textView.setText(message);
        return textView;
    }
}
