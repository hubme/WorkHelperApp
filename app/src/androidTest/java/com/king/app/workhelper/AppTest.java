package com.king.app.workhelper;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.king.app.workhelper.activity.CrashedActivity;
import com.king.app.workhelper.common.BusProvider;
import com.king.app.workhelper.model.entity.Student;
import com.king.applib.log.Logger;
import com.king.applib.util.AppUtil;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.FileUtil;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.os.Environment.DIRECTORY_DCIM;
import static com.king.applib.util.FileUtil.createFile;

/**
 * 测试
 * Created by HuoGuangxu on 2016/9/29.
 */

public class AppTest extends BaseTestCase {

    public void testGsonExposeAnnotation() throws Exception {
        Student student = new Student();
        student.id = 1;
        student.age = 24;
        student.name = "VanceKing";

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(student);
        Logger.i("json: " + json);

        final String jsonText = "{\"pId\":1024,\"age\":25,\"name\":\"VanceKing\"}";
        Student student1 = gson.fromJson(jsonText, Student.class);
        Logger.i("student1: " + student1.toString());
    }

    public void testSerializedNameAnnotation() throws Exception {
        final String jsonText = "{\"pId\":1024,\"name\":\"VanceKing\"}";
        Gson gson = new Gson();
        Student student = gson.fromJson(jsonText, Student.class);
        Logger.i("student: " + student.toString());
    }

    public void testDimension() throws Exception {

        Logger.i(String.valueOf(mContext.getResources().getDimension(R.dimen.icon_test)));
        Logger.i(String.valueOf(mContext.getResources().getDimensionPixelOffset(R.dimen.icon_test)));
        Logger.i(String.valueOf(mContext.getResources().getDimensionPixelSize(R.dimen.icon_test)));
    }

    public void testGetActivityProcessName() throws Exception {
        Logger.i("Activity: " + AppUtil.getActivityProcessName(mContext, Activity.class));
        Logger.i("CrashedActivity: " + AppUtil.getActivityProcessName(mContext, CrashedActivity.class));
    }

    public void testEnvironment() throws Exception {
        Logger.i("DIRECTORY_DCIM: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
        Logger.i("DIRECTORY_DOWNLOADS: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        Logger.i("DIRECTORY_PICTURES: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
    }

    public void testMathaaa() {
        Logger.i(1000 % 100 + "");//0
        Logger.i(99 % 100 + "");//99
        Logger.i(101 % 100 + "");//1
        Logger.i(200 % 100 + "");//0
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
        Logger.i(ExtendUtil.getResourceName(mContext, R.drawable.boy_running));//boy_running
        Logger.i(ExtendUtil.getResourceName(mContext, R.color.colorPrimary));//colorPrimary
        Logger.i(ExtendUtil.getResourceName(mContext, R.dimen.ts_25));//ts_25
        Logger.i(ExtendUtil.getResourceName(mContext, R.drawable.little_boy_10));//little_boy_10

//        Logger.i("getResourceName: " + mContext.getResources().getResourceName(R.drawable.boy_running));//com.king.app.workhelper:drawable/boy_running
//        Logger.i("getResourceEntryName: " + mContext.getResources().getResourceEntryName(R.drawable.boy_running));//boy_running
//        Logger.i("getResourcePackageName: " + mContext.getResources().getResourcePackageName(R.drawable.boy_running));//com.king.app.workhelper
//        Logger.i("getResourceTypeName: " + mContext.getResources().getResourceTypeName(R.drawable.boy_running));//drawable

//        Logger.i(mContext.getResources().getResourceEntryName(R.color.colorPrimary));
//        Logger.i(mContext.getResources().getResourceEntryName(R.dimen.ts_25));
//        Logger.i(mContext.getResources().getResourceEntryName(R.drawable.little_boy_10));
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
        Logger.i(Environment.getDataDirectory().getAbsolutePath());
        Logger.i(Environment.getDownloadCacheDirectory().getAbsolutePath());
        Logger.i(Environment.getExternalStorageDirectory().getAbsolutePath());
        Logger.i(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).getPath());
        Logger.i(Environment.getRootDirectory().getAbsolutePath());
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
