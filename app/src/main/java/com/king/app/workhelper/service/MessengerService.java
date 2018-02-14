package com.king.app.workhelper.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.king.app.workhelper.app.AppConfig;
import com.king.app.workhelper.fragment.MulProcessFragment;

/**
 * 使用 Messenger 实现跨进程通信
 *
 * @author VanceKing
 * @since 2017/1/31.
 */

public class MessengerService extends Service {
    public static final int MSG_CODE_FROM_CLIENT = 0;

    private Messenger mMessenger = new Messenger(new MessengerHandler());

    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CODE_FROM_CLIENT:
                    Log.i(AppConfig.LOG_TAG, "receive msg from Client: " + msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;

                    Message replayMsg = Message.obtain(null, MulProcessFragment.MSG_CODE_FROM_SERVER);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply", "yes,my dear client, i had receive your message.");
                    replayMsg.setData(bundle);
                    try {
                        client.send(replayMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
