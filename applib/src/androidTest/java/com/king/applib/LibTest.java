package com.king.applib;

import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;

import com.king.applib.log.Logger;
import com.king.applib.util.AppUtil;
import com.king.applib.util.DateTimeUtil;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.FileUtil;
import com.king.applib.util.ImageUtil;
import com.king.applib.util.NetworkUtil;
import com.king.applib.util.NumberUtil;
import com.king.applib.util.SPUtil;
import com.king.applib.util.ScreenUtil;
import com.king.applib.util.StringUtil;

import java.io.File;

/**
 * Lib测试类
 * Created by HuoGuangxu on 2016/9/30.
 */

public class LibTest extends AndroidTestCase {

    private Context mContext;
    private static final String SP_KEY = "aaa";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Logger.init("aaa").methodCount(1).hideThreadInfo();
        mContext = getContext();
    }

    public void testImageWidth() throws Exception {
        Logger.i(ImageUtil.getImageWidth(mContext, R.drawable.home) + " == " + ImageUtil.getImageHeight(mContext, R.drawable.home));
    }

    public void testScreen() throws Exception {
        Logger.i("屏幕宽度：" + ScreenUtil.getScreenWidth(mContext) + "; 屏幕高度: " + ScreenUtil.getScreenHeight(mContext));

    }

    public void testBetweenDays() throws Exception {
        long day = DateTimeUtil.betweenDays("yyyy-MM-dd", "2011-10-23", "2011-10-23")/* / DateUtils.DAY_IN_MILLIS*/;
        Logger.i(day + "");
    }

    public void testEmpty() throws Exception {
        Logger.i(StringUtil.containsNullOrEmpty("aaa", null) + "");
        Logger.i(StringUtil.containsNullOrEmpty("aaa", "") + "");
        Logger.i(StringUtil.containsNullOrEmpty("aaa", "bbb") + "");
    }

    public void testNetwork() throws Exception {
        Logger.i(NetworkUtil.isNetworkAvailable(mContext) + "");
        Logger.i(NetworkUtil.isWifi(getContext()) + "");
        Logger.i(getContext().getExternalCacheDir().getAbsolutePath());//"/storage/sdcard0/Android/data/com.king.applib.test/cache"
        Logger.i(getContext().getCacheDir().getAbsolutePath());//"/data/data/com.king.applib.test/cache"
    }

    public void testPrintArray() throws Exception {
        ExtendUtil.printArray(new String[]{"我", "也", "不", "知", "道"});
        ExtendUtil.printArray(new Integer[]{1, 1, 5, 2, 0});
    }

    public void testGetFileExtension() throws Exception {
        String fileExtension = FileUtil.getFileExtension(Environment.getExternalStorageDirectory() + "/DCIM/aaa.apk");
        Logger.i(fileExtension);
    }

    public void testCreateFile() throws Exception {
        String path = Environment.getExternalStorageDirectory() + "/000/000.txt";
        File file = FileUtil.createFile(path);
        Logger.i(file != null ? "创建文件成功:" + path : "创建文件失败");
        Logger.i(FileUtil.getFileExtension(path));
    }

    public void testAppUtils() throws Exception {
        AppUtil.AppInfo appInfo = AppUtil.getAppInfo(getContext());
        Logger.i(appInfo.toString());
    }

    public void testNumberMethod() throws Exception {
        Logger.i("result：" + NumberUtil.getInt(""));
    }

    public void testSPUtils() throws Exception {
        SPUtil.putBoolean(mContext, "aaaaaaaaa", true);
//        SPUtil.putBoolean(mContext, "boolean", false);
//        SPUtil.putFloat(mContext, "float", 5.20f);
//        SPUtil.putInt(mContext, "int", 100);
//        SPUtil.putLong(mContext, "long", 321651);
//        SPUtil.putString(mContext, "string", "双方而上方的");
//
//        Set<String> texts = new HashSet<>();
//        texts.add("董藩");
//        texts.add("带看佛陈");
//        texts.add("董双管");
//        texts.add("；困啊");
//        SPUtil.getSP(mContext).edit().putStringSet("list_set", texts).apply();
//
//        List<Integer> intList = new ArrayList<>();
//        intList.add(1);
//        intList.add(2);
//        intList.add(3);
//        SPUtil.putIntList(mContext, "int_list", intList);
//
//        List<String> stringList = new ArrayList<>();
//        stringList.add("我##fdf");
//        stringList.add("是");
//        stringList.add("谁");
//        SPUtil.putStringList(mContext, "string_list", stringList);


//        SPUtil.clear(mContext);

//        SPUtil.remove(mContext, "string_list");
//        SPUtil.putInt(mContext, "int", 999);

//        ExtendUtil.printList(SPUtil.getStringList(mContext, "string_list"));
//        ExtendUtil.printList(SPUtil.getIntList(mContext, "int_list"));
//        Logger.i(SPUtil.getInt(mContext, "sdjfo")+"");


//        SPUtil.getSP(mContext).edit().putBoolean("aaa", true).putString("bbb", "龙胆啊")
//                .putLong("ccc", 7).putInt("ddd", 678).putFloat("eee", 20.222f).apply();
    }
}
