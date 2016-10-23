package com.king.app.workhelper.service;

import android.app.IntentService;
import android.content.Intent;

import com.king.app.workhelper.common.DownloadManager;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.applib.log.Logger;
import com.king.applib.util.StringUtil;

/**
 * 下载大文件的service
 * Created by VanceKing on 2016/10/22 0022.
 */

public class DownloadFileService extends IntentService {
    public DownloadFileService() {
        super("DownloadFileService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.i("onHandleIntent");
        if (intent == null) {
            return;
        }
        if (DownloadManager.ACTION_DOWNLOAD_FILE.equals(intent.getAction())) {
            String fileUrl = intent.getStringExtra(GlobalConstant.PARAMS_KEY.APP_DOWNLOAD_URL);
            if (!StringUtil.isNullOrEmpty(fileUrl)) {
                downloadFile(fileUrl);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("DownloadFileService#onDestroy");
    }

    private void downloadFile(String url) {
        DownloadManager downloadManager = new DownloadManager(this);
        downloadManager.downloadFile(new DownloadManager.Request(url));
    }
}
