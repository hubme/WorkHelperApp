package com.king.app.workhelper.fragment;

import android.content.Intent;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.app.workhelper.service.DownloadFileService;
import com.king.applib.log.Logger;
import com.king.applib.util.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

/**
 * 大文件下载
 * Created by VanceKing on 2016/11/26 0026.
 */

public class DownloadFileSampleFragment extends AppBaseFragment {
    private static final String APK_DOWNLOAD_URL = "http://192.168.2.253:8080/appupdate/mobileqq_android.apk";


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
        boolean serviceExists = SPUtil.getBoolean(GlobalConstant.SP_PARAMS_KEY.DOWNLOAD_SERVER_EXISTS);
        if (serviceExists) {
            Logger.i("服务已存在");
            return;
        }
        Logger.i("服务不存在，重新下载");

        Intent intent = new Intent(mContext, DownloadFileService.class);
        intent.setAction(DownloadFileService.ACTION_DOWNLOAD_FILE);
        intent.putExtra(GlobalConstant.INTENT_PARAMS_KEY.FILE_DOWNLOAD_URL, APK_DOWNLOAD_URL);
        mContext.startService(intent);
    }
}
