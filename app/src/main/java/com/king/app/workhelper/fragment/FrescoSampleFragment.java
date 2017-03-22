package com.king.app.workhelper.fragment;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Environment;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;
import com.king.applib.util.FileUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import javax.annotation.Nullable;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Fresco Sample
 * Created by HuoGuangxu on 2016/12/30.
 */

public class FrescoSampleFragment extends AppBaseFragment {
//    public static final String PIC_URL = "http://192.168.2.78:8080/appupdate/launch_background.jpg";
    public static final String PIC_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490190243757&di=3bd3e4eca13d247b62dad6e4dedaccc3&imgtype=0&src=http%3A%2F%2Ftupian.enterdesk.com%2F2016%2Fhxj%2F08%2F16%2F1602%2FChMkJlexsJmIIe8dAAghrgQhdMQAAUdNAJInfYACCHG699.jpg";
    @BindView(R.id.view_pic)
    SimpleDraweeView mSimpleDraweeView;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_fresco_sample;
    }

    @Override
    protected void initData() {
        super.initData();

        final AbstractDraweeController build = Fresco.newDraweeControllerBuilder()
                .setControllerListener(new ImageDownloadListener())
                .setUri(PIC_URL).build();

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
                    imagePipeline.evictFromDiskCache(uri);
                } else {
                    Logger.i("无缓存，立即下载图片");
                }
                mSimpleDraweeView.setController(build);
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

        File file = FileUtil.getFileByPath(Environment.getExternalStorageDirectory().getPath() + "/000test/launch.jpg");
        if (FileUtil.isFileExists(file)) {
            mSimpleDraweeView.setImageURI(Uri.fromFile(file));
        } else {
            OkHttpUtils.get().url(PIC_URL).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getPath() + "/000test", "launch.jpg") {
                @Override public void onError(Call call, Exception e, int id) {
                    Logger.i("onError");
                }

                @Override public void onResponse(File response, int id) {
                    Logger.i("onResponse");
                    mSimpleDraweeView.setImageURI(Uri.fromFile(response));
                }
            });
        }

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

}
