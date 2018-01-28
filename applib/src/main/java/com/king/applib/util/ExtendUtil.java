package com.king.applib.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.storage.StorageManager;
import android.support.annotation.AnyRes;
import android.support.annotation.ColorInt;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.king.applib.log.Logger;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    /** 判断参数中是否有null值 */
    public static boolean isAnyNull(Object... objects) {
        if (objects == null) {
            return true;
        }
        for (Object object : objects) {
            if (object == null) {
                return true;
            }
        }
        return false;
    }

    /** 判断参数都不为null */
    public static boolean isNoneNull(Object... objects) {
        return !isAnyNull(objects);
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
        ClipboardManager clipboard = (ClipboardManager) ContextUtil.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(label, text);
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(View view) {
        if (null == view) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
        InputMethodManager imm = (InputMethodManager) view.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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

    /**
     * 根据drawable名称获取id
     */
    public static int getDrawableResId(String drawableResName) {
        return getResources().getIdentifier(drawableResName, "drawable", AppUtil.getAppInfo().getPackageName());
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

    /**
     * 判断通知是否可用.<br/>
     * from support library, to check if notifications are blocked on API 19+. The versions below API 19 will return true (notifications are enabled).
     *
     * @see <a href="https://stackoverflow.com/questions/11649151/android-4-1-how-to-check-notifications-are-disabled-for-the-application">stackoverflow</a>
     */
    public static boolean isNotificationEnable(Context context) {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        return managerCompat.areNotificationsEnabled();
    }

    /** 系统分享 */
    public static void share(Context context, String content) {
        if (content == null || StringUtil.isNullOrEmpty(content)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享到");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "分享到"));
    }

    /**
     * Compares two {@code boolean} values.
     * The value returned is identical to what would be returned by:
     * <pre>
     *    Boolean.valueOf(x).compareTo(Boolean.valueOf(y))
     * </pre>
     *
     * @param x the first {@code boolean} to compare
     * @param y the second {@code boolean} to compare
     * @return the value {@code 0} if {@code x == y};
     * a value less than {@code 0} if {@code !x && y}; and
     * a value greater than {@code 0} if {@code x && !y}
     * @since 1.7
     */
    public static int compareBoolean(boolean x, boolean y) {
        return (x == y) ? 0 : (x ? 1 : -1);
    }

    /**
     * 获取所有挂载的路径.<br/>
     * StorageManager#getVolumeList
     */
    @Deprecated
    public String[] getVolumnPaths(Context context) {
        StorageManager mStorageManager = (StorageManager) context.getApplicationContext().getSystemService(Context.STORAGE_SERVICE);
        String[] paths = null;
        try {
            Method mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            mMethodGetPaths.setAccessible(true);
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return paths;
    }

    /** 销毁WebView，避免内存泄露. */
    public static void destroyWebView(WebView webView) {
        destroyWebView(webView, false);
    }

    /**
     * 销毁WebView，避免内存泄露.<br/>
     *
     * @param clearCache 是否清空缓存.注意：所有WebView公用缓存，应用最后显示的WebView才可以使用.
     * @see <a href="https://stackoverflow.com/questions/17418503/destroy-webview-in-android">stackoverflow</a>
     */
    public static void destroyWebView(WebView webView, boolean clearCache) {
        if (webView != null) {
            final ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }

            webView.clearHistory();
            // NOTE: clears RAM cache, if you pass true, it will also clear the disk cache.
            // Probably not a great idea to pass true if you have other WebViews still alive.
            webView.clearCache(clearCache);
            // Loading a blank page is optional, but will ensure that the WebView isn't doing anything when you destroy it.
            //webView.loadUrl("about:blank");

            webView.onPause();
            webView.removeAllViews();
            webView.destroyDrawingCache();

            webView.destroy();
            webView = null;
        }
    }

    /**
     * 判断是否是手机uri(tel:)
     */
    public static boolean isTelUri(String uri) {
        return !StringUtil.isNullOrEmpty(uri) && uri.toLowerCase().startsWith("tel:");
    }

    /**
     * 拨打电话
     */
    public static void makeCall(Context context, String telNumber) {
        if (context == null || StringUtil.isNullOrEmpty(telNumber)) {
            return;
        }
        if (!telNumber.toLowerCase().startsWith("tel:")) {
            telNumber = "tel:" + telNumber;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(telNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivityInfo(context.getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "抱歉，未找到打电话的应用", Toast.LENGTH_SHORT).show();
        }
    }

    @ColorInt
    public static int parseColor(String color, @ColorInt int defaultColor) {
        if (StringUtil.isNullOrEmpty(color)) {
            return defaultColor;
        }
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            return defaultColor;
        }
    }

    /**
     * 截取整个WebView的内容。但是7.0+ 需要在Activity 的 setContentView()前添加如下代码。否则只会截取显示的部分
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     *      WebView.enableSlowWholeDocumentDraw();
     * }
     */
    private Bitmap captureWholeWebView(WebView webView){
        Picture snapShot = webView.capturePicture();
        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }

    /**
     * 判断 api 在当前版本是否可用
     */
    public static boolean isApiAvailable(int versionCode) {
        return Build.VERSION.SDK_INT >= versionCode;
    }
}
