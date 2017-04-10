package com.king.app.workhelper.fragment;

import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.okhttp.SimpleOkHttp;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttp使用
 * Created by HuoGuangxu on 2016/12/8.
 */

public class OkHttpFragment extends AppBaseFragment {
    private final String URL_BAIDU = "http://www.baidu.com";
    private final String HTML_URL = "http://www.baidu.com";
    private final String IMAGE_URL = "http://192.168.1.106:8080/appupdate/pic.jpg";
    private final String JSON_URL = "http://gjj.9188.com/app/loan/xiaoying_bank.json";
    private final String JSON_URL2 = "http://gjj.9188.com/user/querySystemMessage.go";
    private final String JSON_URL3 = "https://api.github.com/users/Guolei1130";
    private final String PIC_URL = "http://192.168.1.102:8080/appupdate/pic.jpg";
    private final String DOU_BAN_URL = "https://api.douban.com/v2/movie/top250?start=0&count=10";

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
        mOkHttpClient = OkHttpUtils.getInstance().getOkHttpClient();
    }

    @OnClick(R.id.tv_okhttp_get)
    public void onOkHttpGetClick() {
        /*Request request = new Request.Builder().get().url(URL_BAIDU).build();
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
        });*/

        Request request = new Request.Builder().get().get().url(URL_BAIDU).build();
        SimpleOkHttp.getInstance().get().url(URL_BAIDU).tag(this)
                .setConnectTimeout(10, TimeUnit.SECONDS).build().enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                
            }

            @Override public void onResponse(Call call, Response response) throws IOException {

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

    @Override public void onDestroyView() {
        super.onDestroyView();
        SimpleOkHttp.getInstance().cancel(this);
    }
}
