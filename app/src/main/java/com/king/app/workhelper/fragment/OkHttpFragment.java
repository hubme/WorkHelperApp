package com.king.app.workhelper.fragment;

import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * OkHttp使用
 * Created by HuoGuangxu on 2016/12/8.
 */

public class OkHttpFragment extends AppBaseFragment {
    private final String URL = "http://www.baidu.com";
    @BindView(R.id.tv_okhttp)
    public TextView mOkHttp;
    private OkHttpClient mOkHttpClient;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_okhttp;
    }

    @Override
    protected void initData() {
        super.initData();
        mOkHttpClient = new OkHttpClient();
    }

    @OnClick(R.id.tv_okhttp)
    public void onOkHttpClick() {
        Request request = new Request.Builder().get().url(URL).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Request request, IOException e) {
                Logger.i("onFailure");
            }

            @Override public void onResponse(Response response) throws IOException {
                Logger.i("onResponse");
                String content = response.body().string();
                Logger.i(content);
            }
        });
    }
}
