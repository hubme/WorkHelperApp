package com.king.app.workhelper.fragment;

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

import com.king.app.workhelper.R;
import com.king.app.workhelper.app.AppConfig;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.service.MessengerService;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/1/31 0031.
 */

public class MulProcessFragment extends AppBaseFragment {
    public static final int MSG_CODE_FROM_SERVER = 0;

    private MessengerServiceConnection mMessengerServiceConnection = new MessengerServiceConnection();
    Messenger mReplyMessenger = new Messenger(new MessengerHandler());

    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CODE_FROM_SERVER:
                    Log.i(AppConfig.LOG_TAG, "receive msg from Service: " + msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_mul_process;
    }

    @OnClick(R.id.tv_messenger)
    public void onMessengerClick() {
        Intent intent = new Intent(mActivity, MessengerService.class);
        mActivity.bindService(intent, mMessengerServiceConnection, Context.BIND_AUTO_CREATE);
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
        mContext.unbindService(mMessengerServiceConnection);
    }
}
