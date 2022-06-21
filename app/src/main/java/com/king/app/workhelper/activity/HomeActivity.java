package com.king.app.workhelper.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.common.RxBus;
import com.king.app.workhelper.receiver.ScreenOnOffReceiver;
import com.king.applib.log.Logger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 测试入口界面。
 *
 * @author VanceKing
 * @since 2016/11/10.
 */
public class HomeActivity extends AppBaseActivity {
    private long exitTime;
    private Toast toast;

    //解决Button无法使用Vector的问题
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void beforeCreateView() {
        super.beforeCreateView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerRxBus();

        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        ScreenOnOffReceiver mBootBroadcastReceiver = new ScreenOnOffReceiver();
        registerReceiver(mBootBroadcastReceiver, mIntentFilter);

        appInstallStateReceiver();
    }

    //包可见行：需要添加 QUERY_ALL_PACKAGES 权限或配置 query
    private void appInstallStateReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Logger.i("AppInstallStateReceiver: " + intent.getAction());
            }
        }, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterRxBus();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
        super.initData();

        toast = Toast.makeText(getApplicationContext(), "确定退出？", Toast.LENGTH_SHORT);
    }

    @Override
    protected String getActivityTitle() {
        return "测试";
    }

    @Override
    protected void onNavigationClicked() {
        moveTaskToBack(true);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            //isTaskRoot();//用来判断该Activity是否为任务栈中的根Activity，即启动应用的第一个Activity
            //moveTaskToBack(true);//将应用退到后台，不是finish
            quitToast();
        }
    }

    // TODO: 2017/7/16 Fragment返回
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 1500) {
                ToastUtil.showShort(R.string.one_more_exit);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    private void quitToast() {
        if (null == toast.getView().getParent()) {
            toast.show();
        } else {
            super.onBackPressed();
        }
    }

    public Disposable rxBusRegister;

    private void registerRxBus() {
        //在这里订阅消息
        rxBusRegister = RxBus.getInstance().register("1024", String.class)
                .observeOn(AndroidSchedulers.mainThread())//修改发布者，以异步方式在指定的调度程序上执行其排放和通知，并使用缓冲大小槽的有界缓冲区。
                .subscribe(new Consumer<String>() {//订阅一个发布者，并提供一个回调来处理它发出的条目。

                    @Override
                    public void accept(@NonNull String bundle) {
                        Logger.i("accept main");
                    }
                });
    }

    private void unregisterRxBus() {
        if (rxBusRegister != null)
            rxBusRegister.dispose();// 处理资源，操作应该是幂等的
    }
}
