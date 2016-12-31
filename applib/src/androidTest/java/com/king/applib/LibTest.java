package com.king.applib;

import android.os.Environment;
import android.text.format.Formatter;
import android.util.SparseArray;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

/**
 * Lib测试类
 * eated by HuoGuangxu on 2016/9/30.
 */

public class LibTest extends BaseTestCase {

    public void testDoubleArray() throws Exception {
        String[][] array = {{"11", "12"}, {"21", "22"}};
        Logger.i(array[1][0]);
    }

    public void testStringSwitch() throws Exception {
        String aaa = "aaa";
        switch (aaa) {
            case "aaa":
                Logger.i("aaa");
                break;
            case "AAA":
                Logger.i("AAA");
                break;
            case "aaA":
                Logger.i("aaA");
                break;
            case "bb":
                Logger.i("bb");
                break;
        }
    }

    public void testSDf() {
        Logger.i(Formatter.formatFileSize(mContext, 250));//250 B
        Logger.i(Formatter.formatFileSize(mContext, 1024));//1.00 KB
        Logger.i(Formatter.formatFileSize(mContext, 1024 * 1024));//1.00 MB
        Logger.i(Formatter.formatFileSize(mContext, 1024 * 1024 * 1024));//1.00 GB
        Logger.i(Formatter.formatFileSize(mContext, 1024 * 1024 * 1024 * 1024));//
        Logger.i(Formatter.formatFileSize(mContext, 1024 * 1024 * 1024));//
    }

    public void testSparseArray() throws Exception {
        SparseArray<SparseArray<String>> sparseArray = new SparseArray<>();
        SparseArray<String> values0 = new SparseArray<>();
        values0.append(0, "000");
        values0.append(1, "111");

        SparseArray<String> values1 = new SparseArray<>();
        values1.append(0, "000");
        values1.append(1, "111");
        sparseArray.append(0, values0);
        sparseArray.append(0, values1);

        android.support.v4.util.ArrayMap<String, String> arrayMap = new android.support.v4.util.ArrayMap<>();
        arrayMap.keySet();

    }

    public void testJson() throws Exception {
        String jsonStr = "{\"山西省\":[\"长治市\",\"大同市\",\"晋城市\",\"晋中市\",\"临汾市\",\"吕梁市\",\"朔州市\",\"太原市\",\"忻州市\",\"阳泉市\",\"运城市\"],\"宁夏自治区\":[\"固原市\",\"石嘴山市\",\"吴忠市\",\"银川市\",\"中卫市\"]}";
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray jsonArray = jsonObject.optJSONArray("山西省");
        String str = jsonArray.getString(0);
        Logger.i(str);
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
        Logger.i(StringUtil.isAnyEmpty("aaa", null) + "");
        Logger.i(StringUtil.isAnyEmpty("aaa", "") + "");
        Logger.i(StringUtil.isAnyEmpty("aaa", "bbb") + "");
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
