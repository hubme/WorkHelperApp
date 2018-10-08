package com.king.app.workhelper.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.service.NormalService;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2018/10/8.
 */
public class ServiceActivity extends AppBaseActivity {
    private Intent serviceIntent;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            NormalService.MyBind myBind = (NormalService.MyBind) iBinder;
            Log.i(TAG, "onServiceConnected。" + (myBind != null ? myBind.toString() : ""));
        }

        @Override public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "onServiceDisconnected ");
        }
    };
    
    @Override protected int getContentLayout() {
        return R.layout.activity_service;
    }

    @Override protected void initInitialData() {
        super.initInitialData();
        serviceIntent = new Intent(this, NormalService.class);
        serviceIntent.putExtra("key", "aaa");
    }

    @OnClick(R.id.tv_start_service)
    public void onStartServiceClick() {
        startService(serviceIntent);
    }

    @OnClick(R.id.tv_stop_service)
    public void onStopServiceClick() {
        stopService(serviceIntent);
    }

    @OnClick(R.id.tv_bind_service)
    public void onBindServiceClick() {
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    
    @OnClick(R.id.tv_unbind_service)
    public void onUnBindServiceClick() {
        //1. 只能解绑一次。多次会异常:IllegalArgumentException: Service not registered
        //2. 未注册服务不能解绑。IllegalArgumentException: Service not registered
        unbindService(serviceConnection);
    }

    @Override protected void onDestroy() {
        Log.i(TAG, "ServiceActivity#onDestroy");
        super.onDestroy();
    }
}
