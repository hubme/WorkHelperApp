package com.king.applib.jsbridge;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.Locale;

public class JsBridgeWebChromeClient extends WebChromeClient {
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (!TextUtils.isEmpty(message) && message.startsWith(JsBridge.JS_BRIDGE_SCHEME)) {
            result.confirm();
            Log.i(JsBridge.LOG_TAG, String.format(Locale.US, "onJsPrompt。url: %s message：%s", url, message));
            JsBridge.call(view, message);
            return true;
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
}
