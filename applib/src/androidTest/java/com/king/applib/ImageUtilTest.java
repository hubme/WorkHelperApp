package com.king.applib;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.test.AndroidTestCase;

import com.king.applib.log.Logger;
import com.king.applib.util.FileUtil;
import com.king.applib.util.ImageUtil;

import java.io.File;

import static com.king.applib.util.ImageUtil.compressByQuality;

/**
 * Created by HuoGuangxu on 2016/11/22.
 */

public class ImageUtilTest extends AndroidTestCase {

    private Context mContext;
    private String mImagePath;
    private String mSavedBitmapPath;

    @Override protected void setUp() throws Exception {
        super.setUp();

        mContext = getContext();
        Logger.init("aaa").methodCount(1).hideThreadInfo();

        mImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/compress_before.jpg";
        mSavedBitmapPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/compress_100k.jpg";
    }

    @Override protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCompressBySize() throws Exception{
//        Bitmap bitmap = ImageUtil.getBitmap(mContext, R.drawable.home);
        Bitmap bitmap = ImageUtil.getBitmap(FileUtil.getFileByPath(mImagePath));
        long t1 = System.currentTimeMillis();
        Bitmap compressedBitmap = ImageUtil.compressByQuality(bitmap, 100 * 1024L, false);
        if (compressedBitmap != null) {
            Logger.i("width: "+compressedBitmap.getWidth()+" ;height: "+compressedBitmap.getHeight());
        }

        Logger.i((System.currentTimeMillis() - t1) + " millis");

        File imageFile = ImageUtil.saveBitmap(compressedBitmap, mSavedBitmapPath, Bitmap.CompressFormat.PNG, 60);
        if (imageFile != null) {
            Logger.i("图片保存成功." + ImageUtil.getImageWidth(mContext, mSavedBitmapPath) + "-" + ImageUtil.getImageHeight(mContext, mSavedBitmapPath));
        } else {
            Logger.i("图片保存失败");
        }
    }

    /**
     * 10.6KB, 768 * 817
     * quality == 10;715 millis;1536*1634;88.2KB
     * quality == 01;889 millis;1536*1634;88.2KB
     */
    public void testBitmap2File() throws Exception {
        Bitmap bitmap = ImageUtil.getBitmap(mContext, R.drawable.home);
        long t1 = System.currentTimeMillis();
        File imageFile = ImageUtil.saveBitmap(bitmap, mSavedBitmapPath, Bitmap.CompressFormat.PNG, 1);
        Logger.i((System.currentTimeMillis() - t1) + " millis");
        if (imageFile != null) {
            Logger.i("图片保存成功." + ImageUtil.getImageWidth(mContext, mSavedBitmapPath) + "-" + ImageUtil.getImageHeight(mContext, mSavedBitmapPath));
        } else {
            Logger.i("图片保存失败");
        }
    }

    public void testCompressByQuality() throws Exception {
        Bitmap bitmap = ImageUtil.getBitmap(FileUtil.getFileByPath(mImagePath));
        compressByQuality(bitmap, 200 * 1024, true);
    }

    public void testGetBitmap() throws Exception {
        Bitmap bitmap = ImageUtil.getBitmap(FileUtil.getFileByPath(mImagePath));
        Bitmap compressedBitmap = compressByQuality(bitmap, 60);
    }

    public void testGetImageWidth() throws Exception {
        Logger.i("getImageWidth(): " + ImageUtil.getImageWidth(mContext, R.drawable.home));//768
        Logger.i("getImageHeight(): " + ImageUtil.getImageHeight(mContext, R.drawable.home));//817

        Logger.i("getImageWidth(): " + ImageUtil.getImageWidth(mContext, mImagePath));//2448
        Logger.i("getImageHeight(): " + ImageUtil.getImageHeight(mContext, mImagePath));//3264
    }
}
