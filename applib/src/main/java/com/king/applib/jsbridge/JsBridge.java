package com.king.applib.jsbridge;

import android.webkit.WebView;

/**
 * 参考 https://github.com/Sunzxyong/JsBridge
 *
 * @author huoguangxu
 * @since 20-6-7.
 */
public class JsBridge {
    public static final String LOG_TAG = "JsBridge";
    public static final String JS_BRIDGE_SCHEME = "jsbridge";
    public static final String JS_BRIDGE_AUTHORITY = "app.king.com";

    /**
     * 在 android.webkit.WebChromeClient#onJsPrompt() 方法中调用此方法， Js 才能调用 Native 提供的方法。
     */
    public static void call(WebView webView, String message) {
        JsCallJava.call(webView, message);
    }

    /**
     * Native 向 H5 传递数据的回调方法。
     */
    public static void callback(WebView webView, String requestCode, String response) {
        JsCallback.call(webView, requestCode, response);
    }
}