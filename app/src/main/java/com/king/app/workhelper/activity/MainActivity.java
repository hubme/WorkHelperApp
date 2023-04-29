package com.king.app.workhelper.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.method.HideReturnsTransformationMethod;
import android.view.Gravity;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.common.PassTransformationMethod;
import com.king.applib.util.AppUtil;

import butterknife.OnClick;

public class MainActivity extends AppBaseActivity {
    @Override
    protected void beforeCreateView() {
        super.beforeCreateView();
        //因为LayoutInflaterCompat#setFactory()只能设置一次.see also: AppCompatDelegateImplV7#installViewFactory
        /*LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory() {
            @Override public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                int size = attrs.getAttributeCount();
                for (int i = 0; i < size; i++) {
                    Logger.i(attrs.getAttributeName(i) + "---" + attrs.getAttributeValue(i));
                }

                //统一把TextView替换成Button
                if (name.equals("TextView")) {
                    return new AppCompatButton(context, attrs);
                }

                //返回null将不能使用兼容包里的新特性。
                View view = getDelegate().createView(parent, name, context, attrs);
                if (view != null && (view instanceof TextView)) {
                    ((TextView) view).setTypeface(getTypeface());
                }
                return view;
            }
        });*/
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
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

//        startActivityWithAnimation();
//        AppUtil.openThirdApp(this, "com.speedsoftware.rootexplo");
        AppUtil.openAppByUri(this, "market://details?id=com.speedsoftware.rootexplorer");
    }

    private TextView buildTextView(String text) {
        TextView textView = new TextView(this);
        textView.setTextSize(15);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);//CheckedTextView不水平居中
        textView.setTextColor(ContextCompat.getColor(this, R.color.chocolate));
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        textView.setLayoutParams(params);
        return textView;
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
