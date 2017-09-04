package com.king.app.workhelper.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.king.app.workhelper.common.DownloadManager;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.app.workhelper.model.UpdateModel;
import com.king.applib.log.Logger;
import com.king.applib.util.SPUtil;

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
        SPUtil.putBoolean(GlobalConstant.SP_PARAMS_KEY.DOWNLOAD_SERVER_EXISTS, true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.i("onHandleIntent");
        if (intent == null) {
            return;
        }
        if (ACTION_DOWNLOAD_FILE.equals(intent.getAction())) {
            UpdateModel model = (UpdateModel) intent.getSerializableExtra(GlobalConstant.INTENT_PARAMS_KEY.FILE_DOWNLOAD_KEY);
            downloadUpdatedApk(this, model);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("DownloadFileService#onDestroy");
        SPUtil.putBoolean(GlobalConstant.SP_PARAMS_KEY.DOWNLOAD_SERVER_EXISTS, false);
    }

    public void downloadUpdatedApk(Context context, UpdateModel updateModel) {
        String path = context.getCacheDir().getPath();//Environment.getExternalStorageDirectory().getPath() + "/Download";
        DownloadManager.FileDownloadRequest request = new DownloadManager.FileDownloadRequest(updateModel.url, path, "update_app.apk");
        request.showNotification(true).setNotificationId(1024);
        DownloadManager.getInstance(context).downloadFile(request);

    }
}
