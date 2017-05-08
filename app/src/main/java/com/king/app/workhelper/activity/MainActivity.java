package com.king.app.workhelper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.model.entity.BannerModel;
import com.king.applib.banner.Banner;
import com.king.applib.banner.BannerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppBaseActivity {
    @BindView(R.id.banner) Banner mBanner;
    private List<BannerModel> mBannerData = new ArrayList<>();
    
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

        BannerModel model = new BannerModel();
        model.setImageUrl("https://gma.alicdn.com/simba/img/TB1FS.AJpXXXXc_XpXXSutbFXXX.jpg_q50.jpg");
        model.setTips("这是页面1");
        mBannerData.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gw.alicdn.com/tps/i3/TB1J9GqJXXXXXcZaXXXdIns_XXX-1125-352.jpg_q50.jpg");
        model.setTips("这是页面2");
        mBannerData.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gma.alicdn.com/simba/img/TB1txffHVXXXXayXVXXSutbFXXX.jpg_q50.jpg");
        model.setTips("这是页面3");
        mBannerData.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gw.alicdn.com/tps/TB1fW3ZJpXXXXb_XpXXXXXXXXXX-1125-352.jpg_q50.jpg");
        model.setTips("这是页面4");
        mBannerData.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gw.alicdn.com/tps/i2/TB1ku8oMFXXXXciXpXXdIns_XXX-1125-352.jpg_q50.jpg");
        model.setTips("这是页面5");
        mBannerData.add(model);

        BannerAdapter adapter = new BannerAdapter<BannerModel>(mBannerData) {
            @Override
            protected void bindTips(TextView tv, BannerModel bannerModel) {
                tv.setText(bannerModel.getTips());
            }

            @Override
            public void bindImage(ImageView imageView, BannerModel bannerModel) {
                Glide.with(MainActivity.this)
                        .load(bannerModel.getImageUrl())
                        .placeholder(R.mipmap.empty)
                        .error(R.mipmap.error)
                        .into(imageView);
            }

        };
        
        mBanner.setBannerAdapter(adapter);
        mBanner.notifyDataHasChanged();
        mBanner.goScroll();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mBanner.pauseScroll();
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
}
