package com.king.app.workhelper.fragment;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.king.app.workhelper.R;
import com.king.app.workhelper.activity.HomeActivity;
import com.king.app.workhelper.common.AppBaseFragment;

import butterknife.OnClick;

import static com.king.app.workhelper.activity.WebViewActivity.TAG;

/**
 * Notification.
 * https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html
 * http://www.jianshu.com/p/22e27a639787
 * Created by HuoGuangxu on 2016/12/7.
 */

public class NotificationFragment extends AppBaseFragment {
    public static final int NOTIFICATION_ID = 1024;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_notification;
    }

    @OnClick(R.id.tv_notification)
    public void pushNotification() {

        final PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, HomeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.downloading)
                .setContentTitle("公积金更新")
                .setContentText("下载中...")
//                .setContentIntent(contentIntent)
//                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(false);

        final NotificationManager mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for (int progress = 0; progress <= 100; progress += 1) {
                            mBuilder.setProgress(100, progress, false);
                            mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                            try {
                                // Sleep for 5 seconds
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "sleep failure");
                            }
                        }
                        mBuilder.setContentText("下载完成")
                                .setProgress(0, 0, false)
                                .setContentIntent(contentIntent)
                                .setAutoCancel(true);
                        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                    }
                }
        ).start();
    }
}
