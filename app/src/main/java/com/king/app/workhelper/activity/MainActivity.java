package com.king.app.workhelper.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

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

    @OnClick(R.id.tv_main)
    public void onMainClick() {
//        openActivity(TabSwitchActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ActivityManager activityManager = (ActivityManager) getApplication().getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.clearApplicationUserData();
        }
    }

    private Typeface getTypeface() {
        return Typeface.createFromAsset(getAssets(), "fonts/my_font.ttf");
    }
}
