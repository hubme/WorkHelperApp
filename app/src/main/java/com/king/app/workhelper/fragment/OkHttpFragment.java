package com.king.app.workhelper.fragment;

import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp使用
 * Created by HuoGuangxu on 2016/12/8.
 */

public class OkHttpFragment extends AppBaseFragment {
    private final String URL = "http://www.baidu.com";
    private final String HTML_URL = "http://www.baidu.com";
    private final String IMAGE_URL = "http://192.168.1.106:8080/appupdate/pic.jpg";

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
        //给HttpClient设置参数
//        mOkHttpClient.newBuilder().readTimeout(30, TimeUnit.SECONDS).build();
    }

    @OnClick(R.id.tv_okhttp)
    public void onOkHttpClick() {
        Request request = new Request.Builder().get().url(URL).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.i("onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.i("onResponse");
                String content = response.body().string();
                Logger.i(content);
            }
        });
    }
}
