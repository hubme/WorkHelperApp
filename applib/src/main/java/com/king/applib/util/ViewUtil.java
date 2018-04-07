package com.king.applib.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * View 相关工具类.
 *
 * @author VanceKing
 * @since 2018/1/17.
 */
public class ViewUtil {
    private ViewUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /** 如果 TextView 为 null 或 TextView 的内容为“”/null,则返回 defaultText. */
    public static String getText(TextView textView, String defaultText) {
        if (textView == null) {
            return defaultText;
        }
        String string = textView.getText().toString();
        if (string.trim().isEmpty()) {
            return defaultText;
        }
        return string;
    }

    /** 如果 TextView 为 null,或 TextView 的内容为“”/null,则返回空字符串 "". */
    public static String getText(TextView textView) {
        return getText(textView, "");
    }

    /** View 设置背景的兼容方法 */
    public static void setViewBackground(View view, Drawable drawable) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
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

}
