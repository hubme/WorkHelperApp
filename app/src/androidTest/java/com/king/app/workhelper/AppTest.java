package com.king.app.workhelper;

import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;

import com.king.app.workhelper.common.BusProvider;
import com.king.applib.log.Logger;
import com.king.applib.util.FileUtil;

import java.io.File;

import static android.os.Environment.DIRECTORY_DCIM;
import static com.king.applib.util.FileUtil.createFile;

/**
 * Created by HuoGuangxu on 2016/9/29.
 */

public class AppTest extends AndroidTestCase {
    private static final String TAG = "aaa";

    public void testBusProvider() throws Exception {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override public void run() {
                    Log.i("aaa", BusProvider.getEventBus().toString());
                }
            }).start();
        }
    }

    public void testGetFilePath() throws Exception {
        Log.i(TAG, Environment.getDataDirectory().getAbsolutePath());
        Log.i(TAG, Environment.getDownloadCacheDirectory().getAbsolutePath());
        Log.i(TAG, Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.i(TAG, Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).getPath());
        Log.i(TAG, Environment.getRootDirectory().getAbsolutePath());
    }

    public void testCreateDir() throws Exception {
        File file = FileUtil.createDir(Environment.getExternalStorageDirectory() + "/000");
        Logger.i(file == null ? "创建失败" : "创建成功");
    }

    public void testCreateFile() throws Exception {
        File file = createFile(Environment.getExternalStorageDirectory().getAbsolutePath(), "000/123.txt");
        Logger.i(file == null ? "创建失败" : "创建成功");
    }

    public void testWriteFile() throws Exception {
//        File file = FileUtil.createFile(Environment.getExternalStorageDirectory().getAbsolutePath(), "000/123.txt");
//        FileUtil.writeToFile(file, "000111");

        File file = FileUtil.createDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "/123");
        File newFile = FileUtil.createFile(file, "哈哈哈");
        FileUtil.writeToFile(newFile, "987654321");
    }
}
