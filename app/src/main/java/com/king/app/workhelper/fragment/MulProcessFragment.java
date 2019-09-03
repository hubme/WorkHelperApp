package com.king.app.workhelper.fragment;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.king.app.workhelper.IRemoteService;
import com.king.app.workhelper.R;
import com.king.app.workhelper.app.AppConfig;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.app.workhelper.model.AidlModel;
import com.king.app.workhelper.service.AidlService;
import com.king.app.workhelper.service.MessengerService;
import com.king.applib.log.Logger;

import java.util.List;

import butterknife.OnClick;

/**
 * https://developer.android.com/guide/components/aidl.html?hl=zh-cn
 *
 * @author VanceKing
 * @since 2017/1/31.
 */

public class MulProcessFragment extends AppBaseFragment {
    public static final int MSG_CODE_FROM_SERVER = 0;
    private int mIndex = 0;

    private MessengerServiceConnection mMessengerServiceConnection = new MessengerServiceConnection();
    Messenger mReplyMessenger = new Messenger(new MessengerHandler());
    private boolean mBindSuccess = false;

    private class MessengerHandler extends Handler {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CODE_FROM_SERVER:
                    Log.i(AppConfig.LOG_TAG, "receive msg from Service: " + msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private IRemoteService mRemoteService;

    // Binder死亡通知
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override public void binderDied() {
            Logger.i("binderDied");
            if (mRemoteService == null) {
                return;
            }
            //取消死亡监听
            mRemoteService.asBinder().unlinkToDeath(mDeathRecipient, 0);
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.i("onServiceConnected");
            mRemoteService = IRemoteService.Stub.asInterface(service);

            try {
                // 设置服务绑定死亡监听
                service.linkToDeath(mDeathRecipient, 0);
            } catch (Exception e) {

            }
        }

        @Override public void onServiceDisconnected(ComponentName name) {
            Logger.i("onServiceDisconnected");
            mRemoteService = null;
        }
    };

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_mul_process;
    }

    @OnClick(R.id.tv_messenger)
    public void onMessengerClick() {
        Intent intent = new Intent(getActivity(), MessengerService.class);
        mBindSuccess = getActivity().bindService(intent, mMessengerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.tv_bind_service)
    public void onBindServiceClick(TextView textView) {
        Intent service = new Intent(getContext(), AidlService.class);
        getActivity().bindService(service, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.tv_unbind_service)
    public void onUnBindServiceClick(TextView textView) {
        //重复解绑会出现IllegalArgumentException: Service not registered.
        getActivity().unbindService(mServiceConnection);
    }

    //调用远程耗时方法时，当前线程会阻塞，所以不要调用耗时的远程方法。
    @OnClick(R.id.tv_get_service)
    public void onGetServiceClick(TextView textView) {
        try {
            mRemoteService.add(new AidlModel("name_" + mIndex, 18 + mIndex));
            printList(mRemoteService.getModels());
        } catch (Exception e) {
            Logger.e(Log.getStackTraceString(e));
        }
        mIndex++;
    }

    private void printList(List<?> list) {
        Log.i(GlobalConstant.LOG_TAG, "size of list: " + list.size());
        for (Object object : list) {
            Log.i(GlobalConstant.LOG_TAG, object.toString());
        }
    }

    private class MessengerServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger mMessenger = new Messenger(service);
            Message msg = Message.obtain(null, MessengerService.MSG_CODE_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "hello, this is client");
            msg.setData(data);
            msg.replyTo = mReplyMessenger;//把接收服务端回复的Messenger通过Message的replyTo参数传递给服务端。
            try {
                mMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBindSuccess && mContext != null) {
            mContext.unbindService(mMessengerServiceConnection);
        }
    }
}
