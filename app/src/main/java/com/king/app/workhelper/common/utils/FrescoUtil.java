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
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.StringUtil;

import java.io.File;

/**
 * @author VanceKing
 * @since 2017/7/20.
 */

public class FrescoUtil {
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
            @Override protected void onNewResultImpl(DataSource<Void> dataSource) {
                Logger.i("onSuccess.是否在主线程：" + ExtendUtil.isInMainThread());
                final File file = getFileFromDiskCache(context, url);
                if (listener != null && file != null) {
                    listener.onSuccess(file);
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
                Logger.i("progress: " + dataSource.getProgress());
                if (listener != null) {
                    listener.onProgress(dataSource.getProgress());
                }
            }

            @Override
            protected void onFailureImpl(DataSource<Void> dataSource) {
                Logger.i("onFailure");
                if (listener != null) {
                    listener.onFail(url);
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

        void onFail(String url);

        void onProgress(float progress);
    }
}
