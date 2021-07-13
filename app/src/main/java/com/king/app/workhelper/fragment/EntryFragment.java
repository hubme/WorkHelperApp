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
    private FragmentEntryBinding mViewBinding;
    private int mScrollY;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_entry;
    }

    @Override
    protected void initContentView(View rootView) {
        super.initContentView(rootView);
        mViewBinding = FragmentEntryBinding.bind(rootView);

        mViewBinding.scrollView.setScrollY(mScrollY);
        setViewOnClickListener(mViewBinding.tvActivityLifeCircle, mViewBinding.tvService,
                mViewBinding.tvContentProvider, mViewBinding.tvBroadcastReceiver,
                mViewBinding.tvTestActivity, mViewBinding.tvDebugActivity,
                mViewBinding.tvMainActivity, mViewBinding.tvPermission,
                mViewBinding.tvImageSample, mViewBinding.tvRxJava,
                mViewBinding.tvViewPager, mViewBinding.tvWebViewJs,
                mViewBinding.tvDownloadFile, mViewBinding.tvRetrofit,
                mViewBinding.tvCustomView, mViewBinding.tvUsage,
                mViewBinding.tvLeakCanary, mViewBinding.tvToolbar,
                mViewBinding.tvLog, mViewBinding.tvNotification,
                mViewBinding.tvOkhttpGet, mViewBinding.tvRoundImage,
                mViewBinding.tvCanvas, mViewBinding.tvFresco,
                mViewBinding.tvWebView, mViewBinding.tvFileProvider,
                mViewBinding.tvAnimation, mViewBinding.tvViewUsage,
                mViewBinding.tvMulProcess, mViewBinding.tvMenuShare,
                mViewBinding.tvRecyclerView, mViewBinding.tvEvent,
                mViewBinding.tvDrawable, mViewBinding.tvFragmentLifeCircle,
                mViewBinding.tvBannerSimple, mViewBinding.tvFlexboxLayout,
                mViewBinding.tvUpdatePhoto, mViewBinding.tvBottomSheets,
                mViewBinding.tvVectorSample, mViewBinding.tvViewSlide,
                mViewBinding.tvExpandableListView, mViewBinding.tvThreadPool,
                mViewBinding.tvThread, mViewBinding.tvDataBinding,
                mViewBinding.tvDagger2, mViewBinding.tvMotionEvent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewBinding = null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mViewBinding.tvActivityLifeCircle) {
            openActivity(LifeCircleEntryActivity.class);
        } else if (v == mViewBinding.tvContentProvider) {
            ARouter.getInstance().build("/contentprovider/basic").navigation();
        } else if (v == mViewBinding.tvBroadcastReceiver) {
            openActivity(BroadcastReceiverActivity.class);
        } else if (v == mViewBinding.tvTestActivity) {
            openActivity(TestActivity.class);
        } else if (v == mViewBinding.tvDebugActivity) {
            openActivity(DebugActivity.class);
        } else if (v == mViewBinding.tvMainActivity) {
            openActivity(MainActivity.class);
        } else if (v == mViewBinding.tvPermission) {
            ARouter.getInstance().build("/permission/main").navigation();
        } else if (v == mViewBinding.tvImageSample) {
            clickedOn(new ImageSampleFragment());
        } else if (v == mViewBinding.tvRxJava) {
            clickedOn(RxJavaSampleFragment2.newInstance());
        } else if (v == mViewBinding.tvViewPager) {
            //clickedOn(new ViewPagerSampleFragment());
            openActivity(TabSwitchActivity.class);
        } else if (v == mViewBinding.tvWebViewJs) {
            clickedOn(new WebViewSampleFragment());
        } else if (v == mViewBinding.tvDownloadFile) {
            clickedOn(new DownloadFileSampleFragment());
        } else if (v == mViewBinding.tvRetrofit) {
            clickedOn(new RetrofitSampleFragment());
        } else if (v == mViewBinding.tvCustomView) {
            clickedOn(new CustomViewFragment());
            //clickedOn(new CustomViewFragment1());
        } else if (v == mViewBinding.tvUsage) {
            clickedOn(new UsageFragment());
        } else if (v == mViewBinding.tvLeakCanary) {
            clickedOn(new LeakCanaryFragment());
        } else if (v == mViewBinding.tvToolbar) {
            openActivity(ToolbarActivity.class);
        } else if (v == mViewBinding.tvLog) {
            clickedOn(new LogFragment());
        } else if (v == mViewBinding.tvNotification) {
            clickedOn(new NotificationFragment());
        } else if (v == mViewBinding.tvOkhttpGet) {
            clickedOn(new OkHttpFragment());
        } else if (v == mViewBinding.tvRoundImage) {
            clickedOn(new RoundDrawableFragment());
        } else if (v == mViewBinding.tvCanvas) {
            clickedOn(new CanvasSampleFragment());
        } else if (v == mViewBinding.tvFresco) {
            clickedOn(new FrescoSampleFragment());
        } else if (v == mViewBinding.tvWebView) {
            WebActivity.openActivity(getContext(), WebActivity.ASSET_JS);
        } else if (v == mViewBinding.tvFileProvider) {
            clickedOn(new FileProviderFragment());
        } else if (v == mViewBinding.tvAnimation) {
            clickedOn(new AnimationFragment());
        } else if (v == mViewBinding.tvViewUsage) {
            clickedOn(new ViewSampleFragment());
        } else if (v == mViewBinding.tvMulProcess) {
            clickedOn(new MulProcessFragment());
        } else if (v == mViewBinding.tvMenuShare) {
            openActivity(WBShareActivity.class);
        } else if (v == mViewBinding.tvRecyclerView) {
            openActivity(RecyclerViewActivity.class);
        } else if (v == mViewBinding.tvEvent) {
            openActivity(ViewEventSampleActivity.class);
        } else if (v == mViewBinding.tvDrawable) {
            clickedOn(new DrawableFragment());
        } else if (v == mViewBinding.tvFragmentLifeCircle) {
            clickedOn(new LifeCircleFragment());
        } else if (v == mViewBinding.tvBannerSimple) {
            clickedOn(new BannerSampleFragment());
        } else if (v == mViewBinding.tvFlexboxLayout) {
            clickedOn(new FlexBoxLayoutFragment());
        } else if (v == mViewBinding.tvUpdatePhoto) {
            clickedOn(new ChosePhotoFragment());
        } else if (v == mViewBinding.tvBottomSheets) {
            clickedOn(new BottomSheetsFragment());
        } else if (v == mViewBinding.tvVectorSample) {
            clickedOn(new VectorFragment());
        } else if (v == mViewBinding.tvViewSlide) {
            clickedOn(new ViewSlideFragment());
        } else if (v == mViewBinding.tvExpandableListView) {
            clickedOn(new ExpandableListViewFragment());
        } else if (v == mViewBinding.tvThreadPool) {
            clickedOn(new ThreadPoolFragment());
        } else if (v == mViewBinding.tvThread) {
            openActivity(ThreadActivity.class);
        } else if (v == mViewBinding.tvDataBinding) {

        } else if (v == mViewBinding.tvDagger2) {
            clickedOn(new Dagger2Fragment());
        } else if (v == mViewBinding.tvService) {
            openActivity(ServiceActivity.class);
        }
    }

    private void clickedOn(@NonNull Fragment fragment) {
        mScrollY = mViewBinding.scrollView.getScrollY();
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
