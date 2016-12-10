package com.king.app.workhelper.fragment;

import android.content.Intent;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.common.DownloadManager;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.app.workhelper.service.DownloadFileService;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

/**
 * 大文件下载
 * Created by VanceKing on 2016/11/26 0026.
 */

public class DownloadFileSampleFragment extends AppBaseFragment {
    private static final String APK_DOWNLOAD_URL = "http://192.168.1.106:8080/appupdate/app_update.apk";


    @BindView(R.id.tv_download_file)
    public TextView mTextView;
    private OkHttpClient mHttpClient;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_download;
    }

    @Override
    protected void initData() {
        super.initData();
        mHttpClient = new OkHttpClient();
    }

    @OnClick(R.id.tv_download_file)
    public void download() {
        startDownloadService();
    }

    private void startDownloadService() {
        Intent intent = new Intent(mContext, DownloadFileService.class);
        intent.setAction(DownloadManager.ACTION_DOWNLOAD_FILE);
        intent.putExtra(GlobalConstant.INTENT_PARAMS_KEY.FILE_DOWNLOAD_URL, APK_DOWNLOAD_URL);
        mContext.startService(intent);
    }
}
