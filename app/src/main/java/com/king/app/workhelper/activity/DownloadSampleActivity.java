package com.king.app.workhelper.activity;

import android.content.Intent;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.app.workhelper.service.DownloadFileService;
import com.king.applib.log.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HuoGuangxu on 2016/10/20.
 */

public class DownloadSampleActivity extends AppBaseActivity {
    private static final String APK_DOWNLOAD_URL = "http://192.168.9.102:8080/appupdate/app_update.apk";
    //    private static final String APK_DOWNLOAD_URL = "http://www.baidu.com";
    OkHttpClient mHttpClient;

    @Override public int getContentLayout() {
        return R.layout.activity_download;
    }

    @Override protected void initData() {
        super.initData();
        mHttpClient = new OkHttpClient();
    }

    @OnClick(R.id.btn_download)
    public void download() {
        downApk();
    }

    private void downApk() {
        Intent intent = new Intent(this, DownloadFileService.class);
        intent.putExtra(GlobalConstant.PARAMS_KEY.APP_DOWNLOAD_URL, APK_DOWNLOAD_URL);
    }


    private void sdfsdf() {
        OkHttpUtils.get().url(APK_DOWNLOAD_URL).build()
                .execute(new StringCallback() {
                    @Override public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        Logger.i("出错：");
                    }

                    @Override public void onResponse(String response, int id) {
                        Logger.i("result：" + response);
                    }
                });
    }

    private void adfaf() {
        Request request = new Request.Builder().url(APK_DOWNLOAD_URL).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Logger.i("结果出错");
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
//                String aaa = response.body().string();
                Logger.i("result : ");
            }
        });
    }

}
