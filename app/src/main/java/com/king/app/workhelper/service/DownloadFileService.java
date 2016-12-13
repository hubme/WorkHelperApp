package com.king.app.workhelper.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import com.king.app.workhelper.common.DownloadManager;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.applib.log.Logger;
import com.king.applib.util.SPUtil;
import com.king.applib.util.StringUtil;

/**
 * 下载大文件的service
 * Created by VanceKing on 2016/10/22 0022.
 */

public class DownloadFileService extends IntentService {
    public static final String ACTION_DOWNLOAD_FILE = "ACTION_DOWNLOAD_FILE";

    public DownloadFileService() {
        super("DownloadFileService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SPUtil.putBoolean(this, GlobalConstant.SP_PARAMS_KEY.DOWNLOAD_SERVER_EXISTS, true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.i("onHandleIntent");
        if (intent == null) {
            return;
        }
        if (ACTION_DOWNLOAD_FILE.equals(intent.getAction())) {
            String fileUrl = intent.getStringExtra(GlobalConstant.INTENT_PARAMS_KEY.FILE_DOWNLOAD_URL);
            if (!StringUtil.isNullOrEmpty(fileUrl)) {
                downloadFile(fileUrl);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("DownloadFileService#onDestroy");
        SPUtil.putBoolean(this, GlobalConstant.SP_PARAMS_KEY.DOWNLOAD_SERVER_EXISTS, false);
    }

    private void downloadFile(String url) {
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DownloadManager downloadManager = new DownloadManager(this);
        DownloadManager.FileDownloadRequest request = new DownloadManager.FileDownloadRequest(url,
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test", "app_update.apk")
                .showNotification(true)
                .setNotificationClickIntent(null);
        downloadManager.downloadFile(request);
    }
}
