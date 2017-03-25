package com.king.app.workhelper.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.king.app.workhelper.R;
import com.king.app.workhelper.activity.MainActivity;
import com.king.app.workhelper.activity.TabSwitchActivity;
import com.king.app.workhelper.activity.ToolbarActivity;
import com.king.app.workhelper.activity.WBShareActivity;
import com.king.app.workhelper.activity.WebActivity;
import com.king.app.workhelper.common.AppBaseFragment;

import butterknife.OnClick;

/**
 * 测试页面入口Fragment
 * Created by HuoGuangxu on 2016/11/10.
 */

public class EntryFragment extends AppBaseFragment {
    public final String TAG = "EntryFragment";

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_entry;
    }

    @OnClick(R.id.tv_main_activity)
    public void mainActivityClick() {
        openActivity(MainActivity.class);
    }

    @OnClick(R.id.tv_permission)
    public void permissionClick() {
        clickedOn(new PermissionFragment());
    }

    @OnClick(R.id.tv_image_sample)
    public void clickImageSample() {
        clickedOn(new ImageSampleFragment());
    }

    @OnClick(R.id.tv_rx_java)
    public void clickRxJava() {
        clickedOn(new RxJavaSampleFragment());
    }

    @OnClick(R.id.tv_view_pager)
    public void clickViewPager() {
//        clickedOn(new ViewPagerSampleFragment());
        openActivity(TabSwitchActivity.class);
    }

    @OnClick(R.id.tv_web_view_js)
    public void clickWebViewJs() {
        clickedOn(new WebViewSampleFragment());
    }

    @OnClick(R.id.tv_download_file)
    public void clickDownloadFile() {
        clickedOn(new DownloadFileSampleFragment());
    }

    @OnClick(R.id.tv_retrofit)
    public void clickRetrofit() {
        clickedOn(new RetrofitSampleFragment());
    }

    @OnClick(R.id.tv_custom_view)
    public void clickCustomView() {
        clickedOn(new CustomViewFragment());
    }

    @OnClick(R.id.tv_usage)
    public void clickWeakHandler() {
        clickedOn(new UsageFragment());
    }

    @OnClick(R.id.tv_leak_canary)
    public void clickLeakCanary() {
        clickedOn(new LeakCanaryFragment());
    }

    @OnClick(R.id.tv_toolbar)
    public void clickToolbar() {
        openActivity(ToolbarActivity.class);
    }

    @OnClick(R.id.tv_log)
    public void clickLog() {
        clickedOn(new LogFragment());
    }

    @OnClick(R.id.tv_notification)
    public void onNotificationClick() {
        clickedOn(new NotificationFragment());
    }

    @OnClick(R.id.tv_okhttp_get)
    public void onOkHttpClick() {
        clickedOn(new OkHttpFragment());
    }

    @OnClick(R.id.tv_round_image)
    public void onRoundImageClick() {
        clickedOn(new RoundDrawableFragment());
    }

    @OnClick(R.id.tv_flat_buffer)
    public void onFlatBufferClick() {
        clickedOn(new FlatBufferFragment());
    }

    @OnClick(R.id.tv_canvas)
    public void onCanvasClick() {
        clickedOn(new CanvasSampleFragment());
    }

    @OnClick(R.id.tv_fresco)
    public void onFrescoClick() {
        clickedOn(new FrescoSampleFragment());
    }

    @OnClick(R.id.tv_web_view)
    public void onWebViewClick() {
        WebActivity.openActivity(getContext(), WebActivity.ASSET_JS);
    }

    @OnClick(R.id.tv_file_provider)
    public void onFileProviderClick() {
        clickedOn(new FileProviderFragment());
    }

    @OnClick(R.id.tv_animation)
    public void onAnimation() {
        clickedOn(new AnimationFragment());
    }

    @OnClick(R.id.tv_view_usage)
    public void onViewUsage() {
        clickedOn(new ViewSampleFragment());
    }

    @OnClick(R.id.tv_mul_process)
    public void onMulProcess() {
        clickedOn(new MulProcessFragment());
    }

    @OnClick(R.id.tv_share)
    public void onShare() {
        openActivity(WBShareActivity.class);
    }

    private void clickedOn(@NonNull Fragment fragment) {
        final String tag = fragment.getClass().toString();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.layout_container, fragment, tag)
                .commit();
    }
}
