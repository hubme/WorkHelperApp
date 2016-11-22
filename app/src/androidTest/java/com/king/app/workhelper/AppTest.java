package com.king.app.workhelper;

import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;

import com.king.app.workhelper.common.BusProvider;
import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.FileUtil;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.os.Environment.DIRECTORY_DCIM;
import static com.king.applib.util.FileUtil.createFile;

/**
 * Created by HuoGuangxu on 2016/9/29.
 */

public class AppTest extends AndroidTestCase {
    private static final String TAG = "aaa";
    private Context mContext;

    @Override protected void setUp() throws Exception {
        super.setUp();
        mContext = getContext();
    }

    public void testMathPowMethod() throws Exception {
        double aaa = Math.pow(2, 10);
        Logger.i("Math.pow(): " + aaa);
    }

    public void testTimeUnit() throws Exception {
        Logger.i(TimeUnit.MILLISECONDS.toSeconds(1000) + "");
        Logger.i(TimeUnit.SECONDS.toSeconds(1000) + "");
        Logger.i(TimeUnit.MINUTES.toSeconds(1000) + "");
    }

    public void testResources() throws Exception {
//        Logger.i("getResourceName: " + mContext.getResources().getResourceName(R.drawable.boy_running));//com.king.app.workhelper:drawable/boy_running
//        Logger.i("getResourceEntryName: " + mContext.getResources().getResourceEntryName(R.drawable.boy_running));//boy_running
//        Logger.i("getResourcePackageName: " + mContext.getResources().getResourcePackageName(R.drawable.boy_running));//com.king.app.workhelper
//        Logger.i("getResourceTypeName: " + mContext.getResources().getResourceTypeName(R.drawable.boy_running));//drawable

        Logger.i(mContext.getResources().getResourceEntryName(R.color.colorPrimary));
        Logger.i(mContext.getResources().getResourceEntryName(R.dimen.ts_25));
        Logger.i(mContext.getResources().getResourceEntryName(R.drawable.little_boy_10));
    }

    public void testRandom() throws Exception {
        Random random = new Random();
        Logger.i(random.nextInt(2) + "");
    }

    public void testBusProvider() throws Exception {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
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

    public void testSpDp() throws Exception {
        Logger.i(ExtendUtil.dp2px(mContext, 20) + "");
        Logger.i(ExtendUtil.px2dp(mContext, 20) + "");
        Logger.i(ExtendUtil.sp2px(mContext, 20) + "");
        Logger.i(ExtendUtil.px2sp(mContext, 20) + "");
    }
}
