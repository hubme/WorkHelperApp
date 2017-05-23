package com.king.app.workhelper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.text.method.HideReturnsTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.common.PassTransformationMethod;

import butterknife.OnClick;

public class MainActivity extends AppBaseActivity {
    @Override
    protected void beforeCreateView() {
        super.beforeCreateView();
        //因为LayoutInflaterCompat#setFactory()只能设置一次.see also: AppCompatDelegateImplV7#installViewFactory
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory() {
            @Override public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
//                Logger.i("name: " + name);//look log
                /*int size = attrs.getAttributeCount();
                for (int i = 0; i < size; i++) {
                    Logger.i(attrs.getAttributeName(i) + "---" + attrs.getAttributeValue(i));
                }*/

                //统一把TextView替换成Button
                /*if (name.equals("TextView")) {
                    return new AppCompatButton(context, attrs);
                }*/

                //返回null将不能使用兼容包里的新特性。
                View view = getDelegate().createView(parent, name, context, attrs);
                if (view != null && (view instanceof TextView)) {
                    ((TextView) view).setTypeface(getTypeface());
                }
                return view;
            }
        });
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAfterTransition(this);
    }

    @OnClick(R.id.tv_main)
    public void onMainClick() {
//        openActivity(TabSwitchActivity.class);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ActivityManager activityManager = (ActivityManager) getApplication().getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.clearApplicationUserData();
        }*/

        startActivity();
    }

    private void startActivityWithAnimation() {
        Intent intent = new Intent(this, DebugActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_from_bottom, R.anim.alpha_disappear);
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
    }

    private void startActivity() {
        Intent intent = new Intent(this, DebugActivity.class);
        startActivity(intent);
        //第一个参数作(enterAnim)用于target activity，第二个参数(exitAnim)作用于当前activity。
        //如果第二个参数不设置或时间小于target activity的动画时间，会出现背景黑屏的现象。
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.alpha_disappear);
    }

    private Typeface getTypeface() {
        return Typeface.createFromAsset(getAssets(), "fonts/my_font.ttf");
    }

    @OnClick(R.id.ctv_pass)
    public void onPassClick(CheckedTextView textView) {
        textView.toggle();
        if (textView.isChecked()) {
//            textView.getPaint().getFontMetrics()
            textView.setPadding(0, 50, 0, 0);
            textView.setTransformationMethod(new PassTransformationMethod());
        } else {
            textView.setPadding(0, 0, 0, 0);
            textView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }
}
