package com.king.applib;

import android.os.Environment;

import com.king.applib.log.Logger;
import com.king.applib.util.FileUtil;

import org.junit.Before;

import java.io.File;
import java.util.Locale;

import static android.os.Environment.getDataDirectory;

/**
 * 文件工具测试类
 * Created by VanceKing on 2016/11/7.
 */

public class FileUtilTest extends BaseTestCase {

    private String sdCardPath;
    private String testPath;

    @Before
    public void init() {
        sdCardPath = Environment.getExternalStorageDirectory().getPath();
        testPath = sdCardPath + "/000test";
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
        Logger.i("getDataDirectory(): " + getDataDirectory());
        Logger.i("getDownloadCacheDirectory(): " + Environment.getDownloadCacheDirectory());
        Logger.i("getExternalStoragePublicDirectory(DIRECTORY_DCIM): " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
    }

    public void testFileDir() throws Exception {
        Logger.i("getPackageName(): " + mContext.getPackageName());//  com.king.applib.test

        Logger.i("getFilesDir(): " + mContext.getFilesDir());//  /data/data/com.king.applib.test/files
        Logger.i("getCacheDir(): " + mContext.getCacheDir());//  /data/data/com.king.applib.test/cache

        Logger.i("getObbDir(): " + mContext.getObbDir());// /storage/emulated/0/Android/obb/com.king.applib.test
        Logger.i("getExternalCacheDir(): " + mContext.getExternalCacheDir());// /storage/sdcard0/Android/data/com.king.applib.test/cache
        Logger.i("getExternalStorageDirectory(): " + Environment.getExternalStorageDirectory().getPath());// /storage/emulated/0
        Logger.i("getExternalStorageDirectory(): " + mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES));// /storage/emulated/0/Android/data/com.king.applib.test/files/Pictures
    }

    public void testIsLegalFile() throws Exception {
        Logger.i(FileUtil.isLegalFile(FileUtil.getFileByPath(testPath + "/000test/000aaa.jpg")) + "");
        Logger.i("" + FileUtil.isLegalFile(FileUtil.getFileByPath(testPath + "/000test/cache")));
        Logger.i("" + FileUtil.isLegalFile(FileUtil.getFileByPath(testPath + "/000test/123")));
        Logger.i("" + FileUtil.isLegalFile(FileUtil.getFileByPath(testPath + "/000test/empty")));
    }

    public void testIsLegalDir() throws Exception {
        Logger.i(FileUtil.isLegalDir(FileUtil.getFileByPath(testPath + "/000test/000aaa.jpg")) + "");
        Logger.i("" + FileUtil.isLegalDir(FileUtil.getFileByPath(testPath + "/000test/cache")));
        Logger.i("" + FileUtil.isLegalDir(FileUtil.getFileByPath(testPath + "/000test/123")));
        Logger.i("" + FileUtil.isLegalDir(FileUtil.getFileByPath(testPath + "/000test/empty")));
    }

    public void testCreateFile() throws Exception {
        Logger.i("" + FileUtil.createFile(testPath, "123.txt"));
        Logger.i("" + FileUtil.createFile(testPath + "/000", "123.txt"));
    }

    public void testCreateDir() throws Exception {
        Logger.i("" + FileUtil.createDir(testPath + "/000test"));
        Logger.i("" + FileUtil.createDir(testPath + "/000test/0"));
    }

    public void testDeleteDir() throws Exception {
        Logger.i("" + FileUtil.deleteDir(testPath + "/000"));
    }

    public void testDeleteFile() throws Exception {
        Logger.i("" + FileUtil.deleteFile(testPath + "/000"));
        Logger.i("" + FileUtil.deleteFile(testPath + "/cache"));
    }

    public void testIsFileExists() throws Exception {
        Logger.i("" + FileUtil.isFileExists(testPath + "/123"));
        Logger.i("" + FileUtil.isFileExists(testPath + "/cache"));
        Logger.i("" + FileUtil.isFileExists(testPath + "/empty"));
    }
}
