package com.king.app.workhelper.fragment;

import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.okhttp.LoggingInterceptor;
import com.king.applib.log.Logger;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
import okio.Buffer;

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
    private final String PIC_URL = "http://192.168.1.102:8080/appupdate/pic.jpg";

    @BindView(R.id.tv_okhttp_get)
    public TextView mOkHttp;
    private OkHttpClient mOkHttpClient;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_okhttp;
    }

    @Override
    protected void initData() {
        super.initData();
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @OnClick(R.id.tv_okhttp_get)
    public void onOkHttpGetClick() {
        Request request = new Request.Builder().get().url(URL).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Logger.i("onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
//                Logger.i("onResponse" + content);
            }
        });
    }

    @OnClick(R.id.tv_okhttp_post)
    public void onOkHttpPostClick() {
        RequestBody formBody = new FormBody.Builder().add("pn", "1").add("ps", "10").build();
        Request request = new Request.Builder().url(JSON_URL2).post(formBody).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Logger.i("onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
//                Logger.i("onResponse" + content);
            }
        });
    }

    /**
     * notice: response.body()使用后流就关闭了.see:{@link ResponseBody#bytes() 源码}
     */
    private class LogInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Logger.i(String.format(Locale.getDefault(), "send request %s %s%n%s%n%s", request.method(), request.url(), stringifyRequestBody(request), request.headers()));
            long t1 = System.nanoTime();
            Response response = chain.proceed(chain.request());
            long time = System.nanoTime() - t1;

            MediaType mediaType = response.body().contentType();
            String content = response.body().string();

            Logger.i(String.format(Locale.getDefault(), "Received response for %s in %.1fms%s%n%s%n%s",
                    response.request().url(), time / 1e6d, request.headers(), response.headers(), content));
            return response.newBuilder().body(ResponseBody.create(mediaType, content)).build();
        }

        private String stringifyRequestBody(Request request) {
            if (request.body() == null) {
                return "request body is empty";
            }
            try {
                final Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            } catch (final IOException e) {
                return "IOException";
            }
        }
    }
}
