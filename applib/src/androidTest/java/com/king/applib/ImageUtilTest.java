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
 * ImageUtil 测试类
 *
 * @author huoguangxu
 * @since 2016/11/22.
 */
public class ImageUtilTest extends AndroidTestCase {

    private Context mContext;
    private String mImagePath;
    private String mSavedBitmapPath;

    @Override protected void setUp() throws Exception {
        super.setUp();

        mContext = getContext();
        Logger.init("aaa").methodCount(1).hideThreadInfo();

        mImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/aaa.jpg";
        mSavedBitmapPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/compress_100k.jpg";
    }

    @Override protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCompressBySize() throws Exception {
//        Bitmap bitmap = ImageUtil.getBitmap(mContext, R.drawable.home);
        Bitmap bitmap = ImageUtil.getBitmap(FileUtil.getFileByPath(mImagePath));
        long t1 = System.currentTimeMillis();
        Bitmap compressedBitmap = ImageUtil.compressByQuality(bitmap, 100 * 1024L, false);
        if (compressedBitmap != null) {
            Logger.i("width: " + compressedBitmap.getWidth() + " ;height: " + compressedBitmap.getHeight());
        }

        Logger.i((System.currentTimeMillis() - t1) + " millis");

        File imageFile = ImageUtil.saveBitmap(compressedBitmap, mSavedBitmapPath, Bitmap.CompressFormat.PNG, 60);
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
    

    public void testGetBytes() throws Exception {
        byte[] bytes = ImageUtil.getBytes(mImagePath);
        Logger.i(bytes == null ? "bytes == null" : "图片大小:" + bytes.length);
        String encodeToString = FileUtil.encodeToString(bytes);
        Logger.i("encodeToString: " + encodeToString);
    }

    public void testReadPictureDegree() throws Exception{
        int degree = ImageUtil.readPictureDegree(mImagePath);
        Logger.i("degree: " + degree);
    }
}
