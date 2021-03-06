package com.king.app.workhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import androidx.core.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.king.app.workhelper.activity.CrashedActivity;
import com.king.app.workhelper.model.entity.Student;
import com.king.applib.builder.BundleBuilder;
import com.king.applib.log.Logger;
import com.king.applib.util.AppUtil;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.FileUtil;
import com.king.applib.util.StringUtil;

import org.junit.Before;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static android.os.Environment.DIRECTORY_DCIM;
import static com.king.applib.util.FileUtil.createFile;

/**
 * 测试
 * Created by VanceKing on 2016/9/29.
 */

public class AppTest extends BaseAndroidJUnit4Test {

    private Random random;

    @Before
    public void init() {
        random = new Random();
    }

    //计算SDCard的大小、可用空间等。
    public void testSdCardState() throws Exception {
        String path = Environment.getExternalStorageDirectory().getPath();
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSize();
        long blockCount = statFs.getBlockCount();
        long availCount = statFs.getAvailableBlocks();
        Logger.i("block大小: " + blockSize + ";block数目: " + blockCount + ";总大小: " + (blockSize * blockCount) / 1024 + "KB");
        Logger.i("可用的block数目：:" + availCount + ",剩余空间:" + availCount * blockSize / 1024 + "KB");
    }

    public void testMathMethod() throws Exception {
        Logger.i("Math.sinh(): " + Math.sinh(Math.PI / 6) + ";Math.sin(): " + Math.sin(Math.PI / 6));

        Logger.i(Math.cos(0.25 * Math.PI) + "    " + Math.cos(0.75 * Math.PI));
        Logger.i("Math.sqrt: " + Math.sqrt(3 * 3 + 4 * 4));
        Logger.i("Math.sqrt: " + Math.sqrt(-3 * 3 + -4 * 4));
    }

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

        Logger.i(String.valueOf(mContext.getResources().getDimension(R.dimen.dp_15)));
        Logger.i(String.valueOf(mContext.getResources().getDimensionPixelOffset(R.dimen.dp_15)));
        Logger.i(String.valueOf(mContext.getResources().getDimensionPixelSize(R.dimen.dp_15)));
    }

    public void testGetActivityProcessName() throws Exception {
        Logger.i("Activity: " + AppUtil.getActivityProcessName(Activity.class));
        Logger.i("CrashedActivity: " + AppUtil.getActivityProcessName(CrashedActivity.class));
    }

    public void testEnvironment() throws Exception {
        // "/storage/emulated/0/DCIM"
        Logger.i("DIRECTORY_DCIM: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
        // "/storage/emulated/0/Download"
        Logger.i("DIRECTORY_DOWNLOADS: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        // "/storage/emulated/0/Pictures"
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
        Logger.i(ExtendUtil.getResourceName(R.drawable.boy_running));//boy_running
        Logger.i(ExtendUtil.getResourceName(R.color.colorPrimary));//colorPrimary
        Logger.i(ExtendUtil.getResourceName(R.dimen.ts_25));//ts_25
        Logger.i(ExtendUtil.getResourceName(R.drawable.little_boy_10));//little_boy_10

//        Logger.i("getResourceName: " + mContext.getResources().getResourceName(R.drawable.boy_running));//com.king.app.workhelper:drawable/boy_running
//        Logger.i("getResourceEntryName: " + mContext.getResources().getResourceEntryName(R.drawable.boy_running));//boy_running
//        Logger.i("getResourcePackageName: " + mContext.getResources().getResourcePackageName(R.drawable.boy_running));//com.king.app.workhelper
//        Logger.i("getResourceTypeName: " + mContext.getResources().getResourceTypeName(R.drawable.boy_running));//drawable

//        Logger.i(mContext.getResources().getResourceEntryName(R.color.colorPrimary));
//        Logger.i(mContext.getResources().getResourceEntryName(R.dimen.ts_25));
//        Logger.i(mContext.getResources().getResourceEntryName(R.drawable.little_boy_10));
    }

    public void testRandom() throws Exception {
        int max=20;
        int min=10;
        for (int i = 0; i < 10; i++) {
            Log.i(TAG, random.nextInt((max - min) + 1) + min + "");
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
        Logger.i(ExtendUtil.dp2px(20) + "");
        Logger.i(ExtendUtil.px2dp(20) + "");
        Logger.i(ExtendUtil.sp2px(20) + "");
        Logger.i(ExtendUtil.px2sp(20) + "");
    }

    public void testBundle() throws Exception {
        Bundle bundle = BundleBuilder.create().put("aaa", "000").put("bbb", 111).build();
        Logger.i(bundle.getString("aaa") + "---" + bundle.getInt("bbb"));
        Bundle bundle1 = BundleBuilder.create(bundle).put("ccc", "VanceKing").build();
        Logger.i(bundle1.getString("aaa") + "---" + bundle1.getInt("bbb")+"---"+bundle1.getString("ccc"));
    }
    
    public void testIntentUri() throws Exception {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtra("aaa", "111");
        intent.putExtra("bbb", "222");
        Logger.i("result: " + intent.toURI());//#Intent;action=android.intent.action.MAIN;S.aaa=111;S.bbb=222;end 使用 Intent.parseUri()解析
        
    }
    
    public void testAAA() throws Exception {
        String text = "一卡在手|全球通行|全球货币0兑换费|全球支付|aaaa";//
        String[] desc = buildDesc(text);
        Logger.i(desc[0] + "===" + desc[1]);
    }

    private String[] buildDesc(String tags) {
        String[] tag = {"", ""};
        if (StringUtil.isNullOrEmpty(tags)) {
            return tag;
        }
        String[] split = tags.split("\\|");
        if (split.length <= 0) {
            return tag;
        } else if (split.length == 1) {
            tag[0] = split[0];
        } else if (split.length == 2) {
            tag[0] = split[0] + " " + split[1];
        } else {
            tag[0] = split[0] + " " + split[1];
            for (int i = 2, size = split.length; i < size; i++) {
                tag[1] += split[i] + " ";
            }
        }
        return tag;
    }
    
    public void testMathRound() throws Exception {
        Logger.i("" + Math.round(0));//0
        Logger.i("" + Math.round(0.4));//0
        Logger.i("" + Math.round(0.6));//1
        Logger.i("" + Math.round(-0.4));//0
        Logger.i("" + Math.round(-0.6));//-1

    }
    
    public void testHashSet() throws Exception {
        Set<String> stringSet = new HashSet<>();
        Logger.i(stringSet.add("aaa") + "");//不存在指定元素，返回true
        Logger.i(stringSet.add("aaa") + "");//已经存在指定元素，返回false
        Logger.i(stringSet.contains("aaa") + "");
    }

    public void testURLEncoder() {
        Logger.i("result: " + URLEncoder.encode("中国"));
        Logger.i("result2: " + URLEncoder.encode("aaa"));
    }
    
    public void testPair() {
        Pair<String, Integer> a1 = Pair.create("VanceKing", 29);
        Pair<String, Integer> a2 = Pair.create("aaa", 29);
    }
}
