package com.king.applib.jsbridge;

import android.os.Looper;
import android.util.Log;
import android.webkit.WebView;

import java.util.Locale;

class JsCallback {
    private static final String CALLBACK_JS_FORMAT = "javascript:JsBridge.onComplete(%s,%s);";

    public static void call(WebView webView, String requestCode, String response) {
        Log.i(JsBridge.LOG_TAG, " requestCode:" + requestCode + " response:" + response);
        final String callbackJs = String.format(Locale.US, CALLBACK_JS_FORMAT, requestCode, response);
        if (isMainThread()) {
            webView.evaluateJavascript(callbackJs, null);
        } else {
            webView.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    webView.evaluateJavascript(callbackJs, null);
                }
            });
        }
    }

    private static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
