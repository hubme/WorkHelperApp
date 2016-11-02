package com.king.applib.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.king.applib.log.Logger;

import java.util.List;
import java.util.Map;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * 扩展工具类
 * Created by HuoGuangxu on 2016/9/29.
 */

public class ExtendUtil {
    private ExtendUtil() {

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
     * dp转px
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
     * @param context 上下文
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

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
}
