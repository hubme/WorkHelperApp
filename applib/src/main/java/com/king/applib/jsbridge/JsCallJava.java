package com.king.applib.jsbridge;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JsCallJava {

    private JsCallJava() {

    }

    /**
     * @param webView WebView
     * @param message jsbridge://app.king.com:123/showToast?params={"message":"hello"}
     */
    public static void call(WebView webView, String message) {
        if (webView == null || TextUtils.isEmpty(message)) {
            return;
        }
        String[] parsedMessage = parseMessage(message);
        if (parsedMessage == null) {
            return;
        }
        invokeNativeMethod(webView, parsedMessage[0], parsedMessage[1], parsedMessage[2]);
    }

    private static void invokeNativeMethod(WebView webView, String method, String requestCode,
                                           String params) {
        if (TextUtils.isEmpty(method)) {
            return;
        }
        Method[] methods = NativeMethodForJs.class.getMethods();
        if (methods.length <= 0) {
            return;
        }

        try {
            Method m = NativeMethodForJs.class.getMethod(method, WebView.class, String.class,
                    String.class);
            m.setAccessible(true);
            m.invoke(null, webView, requestCode, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            NativeMethodForJs.nativeMethodNotFound(webView, method, requestCode, params);
        }
    }

    //jsbridge://app.king.com:123/showToast?params={"message":"hello"}
    private static String[] parseMessage(String params) {
        Uri uri = Uri.parse(params);
        String scheme = uri.getScheme();
        int requestCode = uri.getPort();
        //app.king.com:123
        String authority = uri.getAuthority();

        int index = 0;
        if (!TextUtils.isEmpty(authority)) {
            index = authority.indexOf(":");
        }

        String tmpAuthority = authority;
        if (index > 0) {
            tmpAuthority = authority.substring(0, index);
        }
        if (!JsBridge.JS_BRIDGE_SCHEME.equals(scheme) || !JsBridge.JS_BRIDGE_AUTHORITY.equals(
                tmpAuthority)) {
            return null;
        }
        String method = uri.getEncodedPath();
        if (TextUtils.isEmpty(method)) {
            return null;
        }
        String methodName = method.replace("/", "");
        String queryParameter = uri.getQueryParameter("params");
        return new String[]{methodName, String.valueOf(requestCode), queryParameter};
    }
}