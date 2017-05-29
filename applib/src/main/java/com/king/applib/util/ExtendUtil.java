package com.king.applib.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.AnyRes;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.king.applib.log.Logger;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * 扩展工具类
 * Created by HuoGuangxu on 2016/9/29.
 */

public class ExtendUtil {
    private ExtendUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /**
     * 判断List集合是否为null/empty
     */
    public static boolean isListNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 判断List集合不为null/empty
     */
    public static boolean isListNotNullOrEmpty(List list) {
        return !isListNullOrEmpty(list);
    }

    public static <E> boolean isArrayNullOrEmpty(E[] array) {
        return array == null || array.length == 0;
    }

    public static <E> void printArray(E[] array) {
        if (ExtendUtil.isArrayNullOrEmpty(array)) {
            return;
        }
        for (E element : array) {
            if (element != null) {
                Logger.i(element.toString());
            }
        }

    }

    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return {@code true}: 为空<br>{@code false}: 不为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 打印List
     */
    public static <T> void printList(List<T> list) {
        if (ExtendUtil.isListNullOrEmpty(list)) {
            return;
        }
        for (T t : list) {
            if (t != null) {
                Logger.i(t.toString());
            }
        }
    }

    /**
     * 检查是否挂载SD卡
     */
    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 检查是否安装外置的SD卡
     */
    public static boolean checkExternalSDExists() {
        Map<String, String> evn = System.getenv();
        return evn.containsKey("SECONDARY_STORAGE");
    }

    /**
     * 拨打电话
     */
    public static void makePhoneCall(String telNumber) {
        if (StringUtil.isNullOrEmpty(telNumber)) {
            return;
        }
        if (!telNumber.toLowerCase().startsWith("tel:")) {
            telNumber = "tel:" + telNumber;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(telNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(ContextUtil.getAppContext().getPackageManager()) != null) {
            ContextUtil.getAppContext().startActivity(intent);
        }
    }

    /**
     * 使用浏览器下载文件
     */
    public static void downloadWithIntent(String fileUrl) {
        if (StringUtil.isNullOrEmpty(fileUrl)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (intent.resolveActivity(ContextUtil.getAppContext().getPackageManager()) != null) {
            ContextUtil.getAppContext().startActivity(intent);
        }
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(float dpValue) {
        final float scale = ContextUtil.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(float pxValue) {
        final float scale = ContextUtil.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(float spValue) {
        final float fontScale = ContextUtil.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(float pxValue) {
        final float fontScale = ContextUtil.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 拷贝字符串
     */
    private void copyTextToClipboard(String label, final String text) {
        if (StringUtil.isNullOrEmpty(text)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) ContextUtil.getAppContext().getSystemService(CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText(label, text);
                clipboard.setPrimaryClip(clip);
            }
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) ContextUtil.getAppContext().getSystemService(CLIPBOARD_SERVICE);
            if (clipboard != null) {
                clipboard.setText(text);
            }
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(View view) {
        if (null == view) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);//InputMethodManager.HIDE_NOT_ALWAYS等只能隐藏一次
        }
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInput(View view) {
        if (null == view) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    public static ColorFilter getColorFilter(int color) {
        ColorMatrixColorFilter colorFilter;
        int red = (color & 0xFF0000) / 0xFFFF;
        int green = (color & 0xFF00) / 0xFF;
        int blue = color & 0xFF;

        float[] matrix = {0, 0, 0, 0, red
                , 0, 0, 0, 0, green
                , 0, 0, 0, 0, blue
                , 0, 0, 0, 1, 0};

        colorFilter = new ColorMatrixColorFilter(matrix);

        return colorFilter;
    }

    /**
     * 获取状态栏的高度
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = ContextUtil.getAppContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ContextUtil.getAppContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * SparseArray转List
     */
    public static <E> List<E> asList(SparseArray<E> sparseArray) {
        if (sparseArray == null) {
            return null;
        }
        List<E> arrayList = new ArrayList<>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }
        return arrayList;
    }

    /** 读取Raw资源 */
    public static byte[] readRawResource(int resId) {
        InputStream stream = null;
        try {
            stream = getResources().openRawResource(resId);
            return new byte[stream.available()];
        } catch (Exception e) {
            return null;
        } finally {
            IOUtil.close(stream);
        }
    }

    /**
     * 根据资源id获取资源文件简单名称.
     */
    public static String getResourceName(@AnyRes int resId) {
        return getResources().getResourceEntryName(resId);
    }

    /** 获取屏幕宽度 */
    public static int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    /** 获取屏幕高度 */
    public static int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    /** 判断是否是主线程 */
    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean isLdpi() {
        return getResources().getDisplayMetrics().densityDpi <= 120;
    }

    public static boolean isMdpi() {
        return (getResources().getDisplayMetrics().densityDpi > 120) && (getResources().getDisplayMetrics().densityDpi <= 160);
    }

    public static boolean isHdpi() {
        return (getResources().getDisplayMetrics().densityDpi > 160) && (getResources().getDisplayMetrics().densityDpi <= 240);
    }

    public static boolean isXhdpi() {
        return (getResources().getDisplayMetrics().densityDpi > 240) && (getResources().getDisplayMetrics().densityDpi <= 320);
    }

    public static boolean isXxhdpi() {
        return (getResources().getDisplayMetrics().densityDpi > 320) && (getResources().getDisplayMetrics().densityDpi <= 480);
    }

    public static Resources getResources() {
        return ContextUtil.getAppContext().getResources();
    }

    /** 是否能正常唤醒intent */
    public static boolean canResolveActivity(Intent intent) {
        return intent != null && intent.resolveActivity(ContextUtil.getAppContext().getPackageManager()) != null;
    }

    /**
     * 根据类全名启动Activity
     *
     * @param context       上下文
     * @param fullClassPath Activity全名
     */
    public static boolean openActivity(Context context, String fullClassPath) {
        if (context == null || StringUtil.isNullOrEmpty(fullClassPath)) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        final String packageName = AppUtil.getAppInfo().getPackageName();
        try {
            ComponentName cn = new ComponentName(packageName, fullClassPath);
            intent.setComponent(cn);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** 打印Exception信息 */
    public static void logException(Exception e) {
        Logger.e(Log.getStackTraceString(e));
    }

    public static void setViewBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
