package com.king.app.workhelper.common.utils;

import android.content.Context;
import android.net.Uri;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.king.applib.log.Logger;
import com.king.applib.util.FileUtil;
import com.king.applib.util.StringUtil;

import java.io.File;

/**
 * @author VanceKing
 * @since 2017/7/20.
 */

public class FrescoUtil {
    public static final String TAG = "FrescoUtil";
    
    private FrescoUtil() {

    }

    public static void downloadImage(Context context, String url, ImageDownloadListener listener) {
        if (context == null || StringUtil.isNullOrEmpty(url)) {
            return;
        }
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<Void> dataSource = imagePipeline.prefetchToDiskCache(imageRequest, context, Priority.HIGH);
        dataSource.subscribe(new BaseDataSubscriber<Void>() {
            //图片地址不正确,下载失败竟然也走这里.有缓存文件，但是文件大小为0，所以要判断图片文件的合法性
            @Override protected void onNewResultImpl(DataSource<Void> dataSource) {
                Logger.log(Logger.INFO, TAG, "hasFailed: " + dataSource.hasFailed() + ";isClosed: " + dataSource.isClosed()+";hasResult: "+dataSource.hasResult()+";isFinished: "+dataSource.isFinished());
                if (listener != null) {
                    final File file = getFileFromDiskCache(context, url);
                    if (FileUtil.isLegalFile(file)) {
                        listener.onSuccess(file);
                        Logger.log(Logger.INFO, TAG, "图片下载成功");
                    } else {
                        listener.onFail(new Throwable("缓存图片为null/length == 0"));
                    }
                }
                
                //当CallerThreadExecutor.getInstance()时
                /*getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        mSimpleDraweeView.setImageURI(Uri.fromFile(fileFromDiskCache));
                    }
                });*/
                
            }

            @Override
            public void onProgressUpdate(DataSource<Void> dataSource) {
                super.onProgressUpdate(dataSource);
                Logger.log(Logger.INFO, TAG, "progress: " + dataSource.getProgress());
                if (listener != null) {
                    listener.onProgress(dataSource.getProgress());
                }
            }

            @Override
            protected void onFailureImpl(DataSource<Void> dataSource) {
                Logger.log(Logger.INFO, TAG, "图片下载失败。url: " + url, dataSource.getFailureCause());
                if (listener != null) {
                    listener.onFail(dataSource.getFailureCause());
                }
            }
        }, UiThreadImmediateExecutorService.getInstance());//CallerThreadExecutor.getInstance()
    }

    /**
     * 从fresco的本地缓存拿到图片,注意文件的结束符并不是常见的.jpg,.png等，如果需要另存，可自行另存
     */
    public static File getFileFromDiskCache(Context context, String url) {
        if (context == null || StringUtil.isNullOrEmpty(url)) {
            return null;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(url), context);
        if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
            BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
            return ((FileBinaryResource) resource).getFile();
        } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
            BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
            return ((FileBinaryResource) resource).getFile();
        }
        return null;
    }

    public interface ImageDownloadListener {
        void onSuccess(File file);

        void onFail(Throwable tr);

        void onProgress(float progress);
    }
}
