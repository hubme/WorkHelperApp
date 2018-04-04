package com.king.app.workhelper.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.king.app.workhelper.IRemoteService;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.app.workhelper.model.AidlModel;
import com.king.applib.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VanceKing
 * @since 2018/3/27.
 */

public class AidlService extends Service {
    public static final String PERMISSION_NAME = "com.aidl.permission";
    private final List<AidlModel> mAidlModels = new ArrayList<>();

    @Override public void onCreate() {
        Log.i(GlobalConstant.LOG_TAG, "[RemoteService] onCreate()");
        super.onCreate();
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(GlobalConstant.LOG_TAG, "[RemoteService] onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable @Override public IBinder onBind(Intent intent) {
        //权限验证
        /*int check = checkCallingOrSelfPermission(PERMISSION_NAME);
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }*/
        Log.i(GlobalConstant.LOG_TAG, "[RemoteService] onBind()");
        return mRemoteService;
    }

    @Override public boolean onUnbind(Intent intent) {
        Log.i(GlobalConstant.LOG_TAG, "[RemoteService] onUnbind()");
        return super.onUnbind(intent);
    }

    @Override public void onDestroy() {
        Log.i(GlobalConstant.LOG_TAG, "[RemoteService] onDestroy()");
        super.onDestroy();
    }

    IRemoteService.Stub mRemoteService = new IRemoteService.Stub() {

        @Override public void add(AidlModel model) throws RemoteException {
//            SystemClock.sleep(5000);//会阻塞调用者线程
            mAidlModels.add(model);
        }

        @Override public List<AidlModel> getModels() throws RemoteException {
            return mAidlModels;
        }

        @Override public int getPid() throws RemoteException {
            return Process.myUid();
        }

        @Override public String getPName() throws RemoteException {
            return AppUtil.getProcessName(AidlService.this);
        }

        /**此处可用于权限拦截**/
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int check = checkCallingOrSelfPermission(PERMISSION_NAME);
            if (check == PackageManager.PERMISSION_DENIED) {
                return false;
            }
            String packageName = null;
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            if (packageName == null || !packageName.startsWith("com.king")) {
                return false;
            }
            Log.i(GlobalConstant.LOG_TAG, "服务权限验证成功。packageName："+packageName);
            return super.onTransact(code, data, reply, flags);
        }
    };
}
