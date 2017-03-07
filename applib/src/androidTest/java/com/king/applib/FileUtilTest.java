package com.king.applib;

import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;

import com.king.applib.log.Logger;
import com.king.applib.util.FileUtil;

import java.io.File;
import java.util.Locale;

/**
 * 文件工具测试类
 * Created by HuoGuangxu on 2016/11/7.
 */

public class FileUtilTest extends AndroidTestCase {

    private Context mContext;

    @Override protected void setUp() throws Exception {
        super.setUp();
        mContext = getContext();
        Logger.init("aaa").methodCount(1).hideThreadInfo();
    }

    @Override protected void tearDown() throws Exception {
        super.tearDown();
    }

    private String getNetSpeed(double speed) {
        if (speed < FileUtil.KB_IN_BYTES) {
            return "bytes/s";
        } else if (speed < FileUtil.MB_IN_BYTES) {
            return String.format(Locale.getDefault(), "%.2f K/s", speed / FileUtil.KB_IN_BYTES);
        } else if (speed < FileUtil.GB_IN_BYTES) {
            return String.format(Locale.getDefault(), "%.2f M/s", speed / FileUtil.MB_IN_BYTES);
        } else {
            return String.format(Locale.getDefault(), "%.2f G/s", speed / FileUtil.GB_IN_BYTES);
        }
    }

    public void testFormatFileSize() throws Exception {
        Logger.i(getNetSpeed(1024d));
        Logger.i(getNetSpeed(1024 * 1024d));
        Logger.i(getNetSpeed(1024 * 1024 * 1024d));
        Logger.i(getNetSpeed(1024 * 1024 * 1024 * 1024d));
    }

    public void testDeleteOnExit() throws Exception {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000/000.jpg";
        Logger.i(FileUtil.isFileExists(filePath) ? "存在" : "不存在");
        File file = FileUtil.createFile(filePath);
        Logger.i(FileUtil.isFileExists(file) ? "存在" : "不存在");
        Logger.i(file == null ? "null" : "not null");
    }

    public void testEnvironment() throws Exception {
        Logger.i("getDownloadCacheDirectory(): " + Environment.getDownloadCacheDirectory().getAbsolutePath());
        Logger.i("getExternalStorageState(): " + Environment.getExternalStorageState());
        Logger.i("getRootDirectory(): " + Environment.getRootDirectory());
        Logger.i("getExternalStorageDirectory(): " + Environment.getExternalStorageDirectory());
        Logger.i("getDataDirectory(): " + Environment.getDataDirectory());
        Logger.i("getDownloadCacheDirectory(): " + Environment.getDownloadCacheDirectory());
        Logger.i("getExternalStoragePublicDirectory(DIRECTORY_DCIM): " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
    }

    public void testFileDir() throws Exception {
        Logger.i("getPackageName(): " + mContext.getPackageName());//  com.king.applib.test
        Logger.i("getFilesDir(): " + mContext.getFilesDir());//  /data/data/com.king.applib.test/files
        Logger.i("getExternalCacheDir(): " + mContext.getExternalCacheDir());// /storage/sdcard0/Android/data/com.king.applib.test/cache
        Logger.i("getCacheDir(): " + mContext.getCacheDir());//  /data/data/com.king.applib.test/cache
        Logger.i("getObbDir(): " + mContext.getObbDir());
    }

    public void testDeleteDir() throws Exception {
//        Logger.i(FileUtil.deleteDir(mContext.getCacheDir().getParentFile()) ? "删除成功" : "删除失败");
        Logger.i(FileUtil.deleteDir(Environment.getExternalStorageDirectory() + "/000test") ? "删除成功" : "删除失败");
    }
}
