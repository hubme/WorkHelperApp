package com.king.app.workhelper.fragment;

import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.api.UserApiService;
import com.king.app.workhelper.app.AppConfig;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.model.EmptyModel;
import com.king.app.workhelper.okhttp.OkHttpProvider;
import com.king.app.workhelper.okhttp.SimpleOkHttp;
import com.king.app.workhelper.okhttp.interceptor.LogInterceptor;
import com.king.app.workhelper.retrofit.ApiServiceFactory;
import com.king.app.workhelper.retrofit.observer.HttpResultObserver;
import com.king.app.workhelper.rx.RxUtil;
import com.king.app.workhelper.rx.rxlife.event.FragmentLifeEvent;
import com.king.applib.log.Logger;
import com.king.applib.util.FileUtil;
import com.king.applib.util.IOUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttp使用
 * @author VanceKing
 * @since 2017/10/5
 */
public class OkHttpFragment extends AppBaseFragment {
    private static final String HELLO_WORLD = "http://www.publicobject.com/helloworld.txt";
    public static final String URL_BAIDU = "http://www.baidu.com";
    public static final String URL_SINA = "http://www.sina.com";
    public static final String IMAGE_URL = "http://192.168.1.106:8080/appupdate/pic.jpg";
    public static final String JSON_URL = "http://gjj.9188.com/app/loan/xiaoying_bank.json";
    public static final String JSON_URL2 = "http://gjj.9188.com/user/querySystemMessage.go";
    public static final String JSON_URL3 = "https://api.github.com/users/Guolei1130";
    public static final String PIC_URL = "http://192.168.56.1:8080/appupdate/pic.jpg";
    public static final String DOU_BAN_URL = "http://api.douban.com/v2/movie/top250?start=0&count=5";
    public static final String CITY_URL = "http://andgjj.youyuwo.com/gjj/getOrderedCitys.go?releaseVersion=2.4.0&source=10001&addressCode=021&appId=yuZALOE2017XR04LH2ZA10549EM5611D1&accessToken=%2BNEflO3uj02eOaWPdCVSbDiORgxuKQuyVLKkfCeHMvs1fBwYKw%2FBCF%2B2puc2f%2F6%2FUIWy%2F61%2FBdrnWaWkivUcyU71G%2FB7pOarJNNzFArRnG6rw683ZXw6i1P3IW%2FPLH4CCv9UegXMNZG7i%2BUvpXbE0bxZx5AnGb1yl2PnoYNN9N0vrov7o7DcJg%3D%3D";
    public static final String URL_CSDN = "http://blog.csdn.net/briblue";
    public static final String URL_PUBLIC = "http://publicobject.com/helloworld.txt";


    @BindView(R.id.tv_okhttp_get)
    public TextView mOkHttp;
    private OkHttpClient mOkHttpClient;
    private ExecutorService mExecutorService;
    private OkHttpClient myOkHttpClient;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_okhttp;
    }

    @Override
    protected void initData() {
        super.initData();
        myOkHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(new LogInterceptor()).build();
        mOkHttpClient = OkHttpProvider.getInstance().getOkHttpClient();
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    @OnClick(R.id.tv_url_conn_get)
    public void onHttpUrlConnectionGet() {
        mExecutorService.submit(() -> {
            String results = getResultWithHttpUrlConnection(URL_SINA);
            Logger.i(results);
        });
    }

    private String getResultWithHttpUrlConnection(String url) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        try {
            URL uri = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept", "*/*");
            int responseCode = connection.getResponseCode();
            Logger.i("responseCode: " + responseCode);
            inputStream = connection.getInputStream();
            InputStreamReader inReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inReader);
            String result;
            while ((result = bufferedReader.readLine()) != null) {
                sb.append(result).append("\n");
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(inputStream);
        }
        return sb.toString();
    }

    @OnClick(R.id.tv_okhttp_get)
    public void onOkHttpGetClick() {
        Request request = new Request.Builder().get().url(HELLO_WORLD).build();
        Call call = myOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Logger.i("onFailure");
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                Logger.i("results: " + response.body().string());
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

    @OnClick(R.id.tv_cache)
    public void onCacheClick() {
        mExecutorService.submit(this::doRequest);
    }

    private void doRequest() {
        Request request = new Request.Builder().url(CITY_URL)
//                .cacheControl(new CacheControl.Builder().maxAge(10, TimeUnit.SECONDS).build())
//                .cacheControl(new CacheControl.Builder().onlyIfCached().build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response response;
        try {
            response = call.execute();
            Log.i(AppConfig.LOG_TAG, "response: " + response.body().string());

            Response cacheResponse = response.cacheResponse();
            if (cacheResponse != null) {
                Log.i(AppConfig.LOG_TAG, "cache response: " + cacheResponse.toString());
            } else {
                Log.i(AppConfig.LOG_TAG, "cacheResponse == null");
            }

            Response netResponse = response.networkResponse();
            if (netResponse != null) {
                Log.i(AppConfig.LOG_TAG, "network response: " + netResponse.toString());
            } else {
                Log.i(AppConfig.LOG_TAG, "netResponse == null");
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }

//        Call newCall = mOkHttpClient.newCall(request);
    }

    @OnClick(R.id.tv_simple_okhttp)
    public void onSimpleOKHttpClick() {
        SimpleOkHttp.getInstance().get().url(URL_BAIDU).tag(this)
                .setConnectTimeout(10, TimeUnit.SECONDS).build().enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {

            }

            @Override public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    @OnClick(R.id.tv_download_image)
    public void onDownloadImageClick() {
        mExecutorService.submit(this::downloadImage);
    }

    private void downloadImage() {
        final String dir = Environment.getExternalStorageDirectory().getPath() + "/000test/000.jpg";
        Log.i("bbb", "dir: " + dir);
        final String URL = "http://10.0.10.168:8080/appupdate/launch_background.jpg";
        Request request = new Request.Builder().url(URL).cacheControl(CacheControl.FORCE_NETWORK).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.i("bbb", "啊啊啊啊啊");
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                Log.i("bbb", "000");
                if (body != null) {
                    Log.i("bbb", "111");
                    FileUtil.saveStream(FileUtil.getFileByPath(dir), body.byteStream(), true);
                }
            }
        });
    }

    @OnClick(R.id.tv_upload_file)
    public void onUploadFileClick() {
        MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
        File file = new File("");
        Request request = new Request.Builder().url("")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {

            }

            @Override public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    /** retrofit 上传图片 */
    private void uploadPhoto(final String filePath) {
        Map<String, String> params = new HashMap<>();//后端需要的参数(token等)
        final File file = new File(filePath);
        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("PhotoFile", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        UserApiService userApiService = ApiServiceFactory.getInstance().createService(UserApiService.class);
        userApiService.uploadPhoto(imageBodyPart, params)
                .compose(bindUntilEvent(FragmentLifeEvent.DESTROY_VIEW))
                .compose(RxUtil.defaultObservableSchedulers())
                .subscribe(new HttpResultObserver<EmptyModel>() {
                    @Override public void onSuccess(EmptyModel emptyModel, String msg) {

                    }
                });
    }

    /** retrofit 多图片上传 */
    private void uploadMultiPhoto(final String filePath) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("appId", "appId");

        List<String> filePaths = new ArrayList<>();
        for (String path : filePaths) {
            File file = new File(path);
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/png"), file);
            builder.addFormDataPart("imagefile" + file.getName(), file.getName(), imageBody);
        }

        UserApiService userApiService = ApiServiceFactory.getInstance().createService(UserApiService.class);
        userApiService.uploadMultiPhoto(builder.build().parts())
                .compose(bindUntilEvent(FragmentLifeEvent.DESTROY_VIEW))
                .compose(RxUtil.defaultObservableSchedulers())
                .subscribe(new HttpResultObserver<EmptyModel>() {
                    @Override public void onSuccess(EmptyModel emptyModel, String msg) {

                    }
                });
    }

    /** okhttp 上传图片 */
    private void uploadPhoto2(String filePath) {
        File file = new File(filePath);
        final MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("photo", file.getName(), okhttp3.RequestBody.create(okhttp3.MediaType.parse("image/png"), file))
                .addFormDataPart("appId", "appId")
                .addFormDataPart("token", "token")
                .build();
        final Request request = new Request.Builder()
                .url("")
                .tag("UploadPhoto")
                .post(requestBody)
                .build();
        OkHttpProvider.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Logger.e(Log.getStackTraceString(e));
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                Logger.i("上传成功。"+response.body().string());
            }
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        SimpleOkHttp.getInstance().cancel(this);
    }
}
