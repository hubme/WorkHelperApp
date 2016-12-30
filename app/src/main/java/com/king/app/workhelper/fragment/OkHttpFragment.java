package com.king.app.workhelper.fragment;

import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.okhttp.LoggingInterceptor;
import com.king.applib.log.Logger;

import java.io.IOException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttp使用
 * Created by HuoGuangxu on 2016/12/8.
 */

public class OkHttpFragment extends AppBaseFragment {
    private final String URL = "http://www.baidu.com";
    private final String HTML_URL = "http://www.baidu.com";
    private final String IMAGE_URL = "http://192.168.1.106:8080/appupdate/pic.jpg";
    private final String JSON_URL = "http://gjj.9188.com/app/loan/xiaoying_bank.json";
    private final String JSON_URL2 = "http://gjj.9188.com/user/querySystemMessage.go";

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
        mOkHttpClient = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();
        //给HttpClient设置参数
//        mOkHttpClient.newBuilder().readTimeout(30, TimeUnit.SECONDS).build();
    }

    @OnClick(R.id.tv_okhttp)
    public void onOkHttpClick() {
        RequestBody formBody = new FormBody.Builder().add("pn", "1").add("ps", "10").build();
        Request request = new Request.Builder().get().url(JSON_URL).post(formBody).build();
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

    private class LogInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Logger.i("request: " + request.toString());
            long t1 = System.nanoTime();
            Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
            Logger.i(String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            Logger.i("content: " + content);
            return response.newBuilder().body(ResponseBody.create(mediaType, content)).build();
        }
    }
}
