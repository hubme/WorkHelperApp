package com.king.app.workhelper.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.receiver.CustomReceiver;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2018/11/15.
 */
public class BroadcastReceiverActivity extends AppBaseActivity {
    private CustomReceiver receiver;

    @Override protected int getContentLayout() {
        return R.layout.activity_broadcast_receiver;
    }

    @Override protected void initData() {
        super.initData();
        receiver = new CustomReceiver();
        registerReceiver(receiver, new IntentFilter(CustomReceiver.CUSTOM_RECEIVER_ACTION));
    }

    @OnClick(R.id.tv_send_receiver)
    public void onSendBroadcastClick(TextView textView) {
        Intent intent = new Intent(CustomReceiver.CUSTOM_RECEIVER_ACTION);
        intent.putExtra("key", "aaa");
        sendBroadcast(intent);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
