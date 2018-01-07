package com.king.app.workhelper.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.king.app.workhelper.R;
import com.king.app.workhelper.activity.HomeActivity;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.common.DownloadManager;
import com.king.app.workhelper.model.UpdateModel;

import butterknife.OnClick;

/**
 * Notification.
 * https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html
 * http://www.jianshu.com/p/22e27a639787
 * Created by VanceKing on 2016/12/7.
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
                        for (int progress = 0; progress <= 100; progress += 5) {
                            mBuilder.setProgress(100, progress, false);
                            mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                            SystemClock.sleep(1000);
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

    @OnClick(R.id.tv_remote_view)
    public void onRemoteViewsClick() {
        initDownloadProgress(1024);
    }

    private void initDownloadProgress(long fileSize) {
        NotificationManagerCompat mNoticeManager = NotificationManagerCompat.from(mContext);
        if (!mNoticeManager.areNotificationsEnabled()) {
            return;
        }

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.view_download_notification);
        remoteViews.setTextViewText(R.id.file_name, "哈哈哈");
        remoteViews.setTextViewText(R.id.file_size, Formatter.formatFileSize(mContext, fileSize));
        remoteViews.setTextViewText(R.id.status, "下载中...");

        Notification notification = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.downloading)
                .setContentIntent(null)
                .setContent(remoteViews)
                .build();

        mNoticeManager.notify(1024, notification);
    }

    @OnClick(R.id.tv_notice_download)
    public void onNoticeDownloadClick() {
        UpdateModel model = new UpdateModel(0, "update", "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk", "3.0.0");
        downloadUpdatedApk(mContext, model);
    }

    public void downloadUpdatedApk(Context context, UpdateModel updateModel) {
        String path = mContext.getCacheDir().getPath();//Environment.getExternalStorageDirectory().getPath() + "/Download";
        DownloadManager downloadManager = DownloadManager.getInstance(context);
        DownloadManager.FileDownloadRequest request = new DownloadManager.FileDownloadRequest(updateModel.url, path, "update_app.apk");
        request.showNotification(true).setNotificationId(1024);
        downloadManager.downloadFile(request);

    }
}
