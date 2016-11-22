package com.king.applib;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.test.AndroidTestCase;

import com.king.applib.log.Logger;
import com.king.applib.util.FileUtil;
import com.king.applib.util.ImageUtil;

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

        mImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/CAMERA/20161126_140744.jpg";
        mSavedBitmapPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000.png";
    }

    @Override protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testBitmap2File() throws Exception {
        Bitmap bitmap = ImageUtil.getBitmap(mContext, R.drawable.home);
        ImageUtil.saveBitmap(bitmap, mSavedBitmapPath, Bitmap.CompressFormat.PNG, 100);
    }

    public void testCompressByQuality() throws Exception {
        Bitmap bitmap = ImageUtil.getBitmap(FileUtil.getFileByPath(mImagePath));
        ImageUtil.compressByQuality(bitmap, 200 * 1024, true);
    }

    public void testGetBitmap() throws Exception {
        Bitmap bitmap = ImageUtil.getBitmap(FileUtil.getFileByPath(mImagePath));
        Bitmap compressedBitmap = ImageUtil.compressByQuality(bitmap, 60);
    }

    public void testGetImageWidth() throws Exception {
        Logger.i("getImageWidth(): " + ImageUtil.getImageWidth(mContext, R.drawable.home));//768
        Logger.i("getImageHeight(): " + ImageUtil.getImageHeight(mContext, R.drawable.home));//817

        Logger.i("getImageWidth(): " + ImageUtil.getImageWidth(mContext, mImagePath));//2448
        Logger.i("getImageHeight(): " + ImageUtil.getImageHeight(mContext, mImagePath));//3264
    }
}
