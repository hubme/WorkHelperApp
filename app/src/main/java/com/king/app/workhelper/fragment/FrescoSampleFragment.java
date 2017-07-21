package com.king.app.workhelper.fragment;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.common.utils.FrescoUtil;
import com.king.app.workhelper.okhttp.OkHttpProvider;
import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.FileUtil;
import com.king.applib.util.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Fresco Sample
 * Created by HuoGuangxu on 2016/12/30.
 */

public class FrescoSampleFragment extends AppBaseFragment {
    public static final String PIC_LOCAL_URL = "http://192.168.89.2:8080/pic/aaa.jpg";
    public static final String PIC_BAIDU_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490190243757&di=3bd3e4eca13d247b62dad6e4dedaccc3&imgtype=0&src=http%3A%2F%2Ftupian.enterdesk.com%2F2016%2Fhxj%2F08%2F16%2F1602%2FChMkJlexsJmIIe8dAAghrgQhdMQAAUdNAJInfYACCHG699.jpg";
    public static final String PIC_REMOTE_SNOW = "http://pic1.win4000.com/wallpaper/7/4fcec6cc47d34.jpg";
    public static final String LOCAL_PIC = Environment.getExternalStorageDirectory().getPath() + "/000test/gjj_splash.jpg";
    public static final String PIC_PATH = Environment.getExternalStorageDirectory().getPath() + "/000test/000aaa.jpg";

    @BindView(R.id.view_pic)
    SimpleDraweeView mSimpleDraweeView;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_fresco_sample;
    }

    @Override
    protected void initData() {
        super.initData();
//        mSimpleDraweeView.setImageURI(Uri.fromFile(FileUtil.getFileByPath(LOCAL_PIC)));
//        mSimpleDraweeView.setImageURI(Uri.parse("res://"+ ContextUtil.getAppContext().getPackageName()+"/"+R.mipmap.splash_screen));
        mSimpleDraweeView.setImageURI(Uri.parse(PIC_BAIDU_URL));

        final AbstractDraweeController build = Fresco.newDraweeControllerBuilder()
                .setControllerListener(new ImageDownloadListener())
                .setUri(PIC_LOCAL_URL).build();

        //一。官网示例,https://www.fresco-cn.org/docs/caching.html.
        // 问题：1.首次进入判断没有缓存，但是无响应，没有回调。返回或查看最近应用再返回时就正常了。
        //2.判断有缓存，删除缓存，图片下载成功后图片没有缓存起来，要等杀死进程后才会缓存。
        /*final ImagePipeline imagePipeline = Fresco.getImagePipeline();
        final Uri uri = Uri.parse(PIC_URL);
        DataSource<Boolean> inDiskCache = imagePipeline.isInDiskCache(uri);
        inDiskCache.subscribe(new BaseDataSubscriber<Boolean>() {
            @Override protected void onNewResultImpl(DataSource<Boolean> dataSource) {
                if (!dataSource.isFinished()) {
                    Logger.i("还没有查询成功");
                    return;
                }
                Logger.i(ExtendUtil.isInMainThread() ? "主线程" : "子线程");//首次进入在子线程，返回再进入就是主线程.why???
                boolean isInCache = dataSource.getResult();
                if (isInCache) {
                    Logger.i("有缓存，移除.重新下载图片");
//                    imagePipeline.evictFromDiskCache(uri);
                } else {
                    Logger.i("无缓存，立即下载图片");
                }
                
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        mSimpleDraweeView.setController(build);
                    }
                });

            }

            @Override protected void onFailureImpl(DataSource<Boolean> dataSource) {

            }
        }, CallerThreadExecutor.getInstance());*/

        //二.其他方法。解决第一次下载不成功的问题。但是问题2依然存在。
        /*CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(PIC_URL), null);
        FileCache fileCache = Fresco.getImagePipelineFactory().getMainFileCache();
        if (fileCache.hasKey(cacheKey)) {
            BinaryResource resource = fileCache.getResource(cacheKey);
            File file = ((FileBinaryResource) resource).getFile();
            // /data/data/com.king.app.workhelper/cache/image_cache/v2.ols100.1/19/kXs62kiZKbqSDo--0Xqu0Tyo13U.cnt
            Logger.i("图片缓存路径： " + file.getAbsolutePath());
            Logger.i("移除图片");
            fileCache.remove(new SimpleCacheKey(PIC_URL));
            Logger.i(fileCache.hasKey(cacheKey) ? "还有缓存" : "缓存已清除");
            Logger.i("重新加载图片");
            mSimpleDraweeView.setController(build);


        } else {
            Logger.i("没有缓存");
            mSimpleDraweeView.setController(build);
        }*/

        /*File file = FileUtil.getFileByPath(Environment.getExternalStorageDirectory().getPath() + "/000test/launch.jpg");
        if (FileUtil.isFileExists(file)) {
            mSimpleDraweeView.setImageURI(Uri.fromFile(file));
        } else {
            //封装的下载图片有问题
            OkHttpUtils.get().url(PIC_URL).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getPath() + "/000test", "launch.jpg") {
                @Override public void onError(Call call, Exception e, int id) {
                    Logger.i("onError");
                }

                @Override public void onResponse(File response, int id) {
                    Logger.i("onResponse");
                    mSimpleDraweeView.setImageURI(Uri.fromFile(response));
                }
            });
        }*/
//        downloadLaunchImage();
//        requestLaunchUrls();
    }

    private void requestLaunchUrls() {
        Request request = new Request.Builder().url("http://www.mocky.io/v2/58d36f5d100000b016b16690").build();
        OkHttpUtils.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {

            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(body.string());
                    JSONObject object = JsonUtil.getJsonObject(jsonObject, "results", "config", "android");
                    int version = JsonUtil.getInt(object, "version");
                    String hdpi = JsonUtil.getString(object, "hdpi");
                    Logger.i("hdpi: " + hdpi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void downloadLaunchImage() {
        /*DownloadManager downloadManager = new DownloadManager(getContext());
        DownloadManager.FileDownloadRequest request = new DownloadManager.FileDownloadRequest(PIC_URL,
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test", "launchImage.jpg");
        downloadManager.downloadFile(request);*/

        Request request = new Request.Builder().url(PIC_LOCAL_URL).build();
        OkHttpUtils.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Logger.i("onFailure");
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null) {
                    return;
                }
                Logger.i("文件大小：" + body.contentLength() + "; 在主线程吗？ " + ExtendUtil.isInMainThread());
                boolean saveSuccess = FileUtil.saveStream(FileUtil.getFileByPath(Environment.getExternalStorageDirectory() + "/000test/test.jpg"), body.byteStream(), true);
                Logger.i(saveSuccess ? "保存成功" : "保存失败");
            }
        });
    }

    //图片下载监听
    private class ImageDownloadListener extends BaseControllerListener<ImageInfo> {
        @Override public void onSubmit(String id, Object callerContext) {
            Logger.i("onSubmit");
        }

        @Override public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
            Logger.i("onFinalImageSet");
        }

        @Override public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            Logger.i("onIntermediateImageSet");
        }

        @Override public void onIntermediateImageFailed(String id, Throwable throwable) {
            Logger.i("onIntermediateImageFailed");
        }

        @Override public void onFailure(String id, Throwable throwable) {
            Logger.i("onFailure" + throwable.toString());
        }

        @Override public void onRelease(String id) {
            Logger.i("onRelease");
        }
    }

    @OnClick(R.id.tv_download_image)
    public void downloadImage() {
        FrescoUtil.downloadImage(mContext, PIC_REMOTE_SNOW, new FrescoUtil.ImageDownloadListener() {
            @Override
            public void onSuccess(File file) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        mSimpleDraweeView.setImageURI(Uri.fromFile(file));
                    }
                });
            }

            @Override
            public void onFail(Throwable tr) {
                Logger.log(Logger.ERROR, FrescoUtil.TAG, "失败：", tr);
            }

            @Override
            public void onProgress(float progress) {

            }
        });
    }

    /**
     * 拷贝到某一个目录中,自动命名
     */
    public File copyCacheFileToDir(Context context, String url, File dir) {

        if (dir == null) {
            return null;
        }
        if (!dir.isDirectory()) {
            throw new RuntimeException(dir + "is not a directory,you should call copyCacheFile(String url,File path)");
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = URLUtil.guessFileName(url, "", "");//android SDK 提供的方法.
        // 注意不能直接采用file的getName拿到文件名,因为缓存文件是用cacheKey命名的
        if (TextUtils.isEmpty(fileName)) {
            fileName = UUID.randomUUID().toString();
        }
        File newFile = new File(dir, fileName);

        boolean isSuccess = copyCacheFile(context, url, newFile);
        if (isSuccess) {
            return newFile;
        } else {
            return null;
        }

    }

    /**
     * 拷贝到某一个文件,已指定文件名
     *
     * @param url  图片的完整url
     * @param path 目标文件路径
     */
    public boolean copyCacheFile(Context context, String url, File path) {
        if (path == null) {
            return false;
        }
        File file = FrescoUtil.getFileFromDiskCache(context, url);
        if (file == null) {
            return false;
        }

        if (path.isDirectory()) {
            throw new RuntimeException(path + "is a directory,you should call copyCacheFileToDir(String url,File dir)");
        }
        boolean isSuccess = file.renameTo(path);

        return isSuccess;
    }

    @OnClick(R.id.tv_update_image)
    public void onImageUpdateClick() {
        long a1 = System.currentTimeMillis();
        Request request = new Request.Builder().url(PIC_LOCAL_URL).build();
        OkHttpProvider.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.i("time: "+(System.currentTimeMillis() -a1));
                FileUtil.saveStream(FileUtil.createFile(PIC_PATH), response.body().byteStream(), true);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSimpleDraweeView.setImageURI(Uri.fromFile(FileUtil.getFileByPath(PIC_PATH)));
                    }
                });
            }
        });
    }
}
