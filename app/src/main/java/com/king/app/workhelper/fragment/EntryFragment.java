package com.king.app.workhelper.fragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.king.app.workhelper.R;
import com.king.app.workhelper.activity.BroadcastReceiverActivity;
import com.king.app.workhelper.activity.DebugActivity;
import com.king.app.workhelper.activity.LifeCircleEntryActivity;
import com.king.app.workhelper.activity.MainActivity;
import com.king.app.workhelper.activity.RecyclerViewActivity;
import com.king.app.workhelper.activity.ServiceActivity;
import com.king.app.workhelper.activity.TabSwitchActivity;
import com.king.app.workhelper.activity.TestActivity;
import com.king.app.workhelper.activity.ThreadActivity;
import com.king.app.workhelper.activity.ToolbarActivity;
import com.king.app.workhelper.activity.ViewEventSampleActivity;
import com.king.app.workhelper.activity.WBShareActivity;
import com.king.app.workhelper.activity.WebActivity;
import com.king.app.workhelper.bluetooth.BluetoothActivity;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.databinding.FragmentEntryBinding;

/**
 * 测试页面入口Fragment
 *
 * @author VanceKing
 * @since 2018/1/7.
 */
public class EntryFragment extends AppBaseFragment {
    public final String TAG = "EntryFragment";
    private FragmentEntryBinding mBinding;
    private int mScrollY;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_entry;
    }

    @Override
    protected void initContentView(View rootView) {
        super.initContentView(rootView);
        mBinding = FragmentEntryBinding.bind(rootView);

        mBinding.scrollView.setScrollY(mScrollY);
        setViewOnClickListener(mBinding.tvActivityLifeCircle, mBinding.tvService,
                mBinding.tvContentProvider, mBinding.tvBroadcastReceiver,
                mBinding.tvTestActivity, mBinding.tvDebugActivity,
                mBinding.tvMainActivity, mBinding.tvPermission,
                mBinding.tvImageSample, mBinding.tvRxJava,
                mBinding.tvViewPager, mBinding.tvWebViewJs,
                mBinding.tvDownloadFile, mBinding.tvRetrofit,
                mBinding.tvCustomView, mBinding.tvUsage,
                mBinding.tvLeakCanary, mBinding.tvToolbar,
                mBinding.tvLog, mBinding.tvNotification,
                mBinding.tvOkhttpGet, mBinding.tvRoundImage,
                mBinding.tvCanvas, mBinding.tvFresco,
                mBinding.tvWebView, mBinding.tvFileProvider,
                mBinding.tvAnimation, mBinding.tvViewUsage,
                mBinding.tvMulProcess, mBinding.tvMenuShare,
                mBinding.tvRecyclerView, mBinding.tvEvent,
                mBinding.tvDrawable, mBinding.tvFragmentLifeCircle,
                mBinding.tvBannerSimple, mBinding.tvFlexboxLayout,
                mBinding.tvUpdatePhoto, mBinding.tvBottomSheets,
                mBinding.tvVectorSample, mBinding.tvViewSlide,
                mBinding.tvExpandableListView, mBinding.tvThreadPool,
                mBinding.tvThread, mBinding.tvDataBinding,
                mBinding.tvDagger2, mBinding.tvMotionEvent,
                mBinding.tvBluetooth);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mBinding.tvActivityLifeCircle) {
            openActivity(LifeCircleEntryActivity.class);
        } else if (v == mBinding.tvContentProvider) {
            ARouter.getInstance().build("/contentprovider/basic").navigation();
        } else if (v == mBinding.tvBroadcastReceiver) {
            openActivity(BroadcastReceiverActivity.class);
        } else if (v == mBinding.tvTestActivity) {
            openActivity(TestActivity.class);
        } else if (v == mBinding.tvDebugActivity) {
            openActivity(DebugActivity.class);
        } else if (v == mBinding.tvMainActivity) {
            openActivity(MainActivity.class);
        } else if (v == mBinding.tvPermission) {
            ARouter.getInstance().build("/permission/main").navigation();
        } else if (v == mBinding.tvImageSample) {
            clickedOn(new ImageSampleFragment());
        } else if (v == mBinding.tvRxJava) {
            clickedOn(RxJavaSampleFragment2.newInstance());
        } else if (v == mBinding.tvViewPager) {
            //clickedOn(new ViewPagerSampleFragment());
            openActivity(TabSwitchActivity.class);
        } else if (v == mBinding.tvWebViewJs) {
            clickedOn(new WebViewSampleFragment());
        } else if (v == mBinding.tvDownloadFile) {
            clickedOn(new DownloadFileSampleFragment());
        } else if (v == mBinding.tvRetrofit) {
            clickedOn(new RetrofitSampleFragment());
        } else if (v == mBinding.tvCustomView) {
            clickedOn(new CustomViewFragment());
            //clickedOn(new CustomViewFragment1());
        } else if (v == mBinding.tvUsage) {
            clickedOn(new UsageFragment());
        } else if (v == mBinding.tvLeakCanary) {
            clickedOn(new LeakCanaryFragment());
        } else if (v == mBinding.tvToolbar) {
            openActivity(ToolbarActivity.class);
        } else if (v == mBinding.tvLog) {
            clickedOn(new LogFragment());
        } else if (v == mBinding.tvNotification) {
            clickedOn(new NotificationFragment());
        } else if (v == mBinding.tvOkhttpGet) {
            clickedOn(new OkHttpFragment());
        } else if (v == mBinding.tvRoundImage) {
            clickedOn(new RoundDrawableFragment());
        } else if (v == mBinding.tvCanvas) {
            clickedOn(new CanvasSampleFragment());
        } else if (v == mBinding.tvFresco) {
            clickedOn(new FrescoSampleFragment());
        } else if (v == mBinding.tvWebView) {
            WebActivity.openActivity(getContext(), WebActivity.ASSET_JS);
        } else if (v == mBinding.tvFileProvider) {
            clickedOn(new FileProviderFragment());
        } else if (v == mBinding.tvAnimation) {
            clickedOn(new AnimationFragment());
        } else if (v == mBinding.tvViewUsage) {
            clickedOn(new ViewSampleFragment());
        } else if (v == mBinding.tvMulProcess) {
            clickedOn(new MulProcessFragment());
        } else if (v == mBinding.tvMenuShare) {
            openActivity(WBShareActivity.class);
        } else if (v == mBinding.tvRecyclerView) {
            openActivity(RecyclerViewActivity.class);
        } else if (v == mBinding.tvEvent) {
            openActivity(ViewEventSampleActivity.class);
        } else if (v == mBinding.tvDrawable) {
            clickedOn(new DrawableFragment());
        } else if (v == mBinding.tvFragmentLifeCircle) {
            clickedOn(new LifeCircleFragment());
        } else if (v == mBinding.tvBannerSimple) {
            clickedOn(new BannerSampleFragment());
        } else if (v == mBinding.tvFlexboxLayout) {
            clickedOn(new FlexBoxLayoutFragment());
        } else if (v == mBinding.tvUpdatePhoto) {
            clickedOn(new ChosePhotoFragment());
        } else if (v == mBinding.tvBottomSheets) {
            clickedOn(new BottomSheetsFragment());
        } else if (v == mBinding.tvVectorSample) {
            clickedOn(new VectorFragment());
        } else if (v == mBinding.tvViewSlide) {
            clickedOn(new ViewSlideFragment());
        } else if (v == mBinding.tvExpandableListView) {
            clickedOn(new ExpandableListViewFragment());
        } else if (v == mBinding.tvThreadPool) {
            clickedOn(new ThreadPoolFragment());
        } else if (v == mBinding.tvThread) {
            openActivity(ThreadActivity.class);
        } else if (v == mBinding.tvDataBinding) {

        } else if (v == mBinding.tvDagger2) {
            clickedOn(new Dagger2Fragment());
        } else if (v == mBinding.tvService) {
            openActivity(ServiceActivity.class);
        } else if (v == mBinding.tvMotionEvent) {
            clickedOn(new MotionEventFragment());
        } else if (v == mBinding.tvBluetooth) {
            openActivity(BluetoothActivity.class);
        }
    }

    private void clickedOn(@NonNull Fragment fragment) {
        mScrollY = mBinding.scrollView.getScrollY();
        final String tag = fragment.getClass().toString();
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(tag)
                    .replace(R.id.layout_container, fragment, tag)
                    .commit();
        }
    }

}
