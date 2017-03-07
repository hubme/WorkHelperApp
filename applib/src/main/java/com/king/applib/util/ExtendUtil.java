package com.king.applib.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.AnyRes;
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

    public static boolean isListNullOrEmpty(List list) {
        return list == null || list.isEmpty();
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
    public static void makePhoneCall(Context context, String telNumber) {
        if (context == null || StringUtil.isNullOrEmpty(telNumber)) {
            return;
        }
        if (!telNumber.toLowerCase().startsWith("tel:")) {
            telNumber = "tel:" + telNumber;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(telNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 使用浏览器下载文件
     */
    public static void downloadWithIntent(Context context, String fileUrl) {
        if (context == null || StringUtil.isNullOrEmpty(fileUrl)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 拷贝字符串
     */
    private void copyTextToClipboard(Context context, String label, final String text) {
        if (context == null || StringUtil.isNullOrEmpty(text)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText(label, text);
                clipboard.setPrimaryClip(clip);
            }
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
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
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
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
    public static byte[] readRawResource(Context context, int resId) {
        InputStream stream = null;
        try {
            stream = context.getResources().openRawResource(resId);
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
    public static String getResourceName(Context context, @AnyRes int resId) {
        return context.getResources().getResourceEntryName(resId);
    }

    /** 获取屏幕宽度 */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /** 获取屏幕高度 */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
