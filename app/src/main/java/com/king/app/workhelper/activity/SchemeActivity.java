package com.king.app.workhelper.activity;

import android.content.Intent;
import android.net.Uri;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.log.Logger;

/**
 * h5跳转到native
 * Created by VanceKing on 2017/1/1 0001.
 */

public class SchemeActivity extends AppBaseActivity {
    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void getIntentData(Intent intent) {
        super.getIntentData(intent);
        Uri uri = intent.getData();
        StringBuilder sb = new StringBuilder();
        sb.append("string: ").append(intent.getDataString())
                .append("scheme: ").append(uri.getScheme()).append("\n")
                .append("host: ").append(uri.getHost()).append("\n")
                .append("port : ").append(uri.getHost()).append("\n")
                .append("path  : ").append(uri.getHost()).append("\n")
                .append("page   : ").append(uri.getHost()).append("\n")
                .append("name   : ").append(uri.getHost()).append("\n");
        Logger.i("result: " + sb.toString());
    }
}
