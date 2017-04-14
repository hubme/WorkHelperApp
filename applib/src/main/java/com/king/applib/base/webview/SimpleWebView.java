package com.king.applib.base.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.king.applib.util.NetworkUtil;

/**
 * 基础BaseWebView
 * @author huoguangxu
 * @since 2017/4/14.
 */

public class SimpleWebView extends WebView {
    public SimpleWebView(Context context) {
        this(context, null);
    }

    public SimpleWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView();
    }

    private void initWebView() {
//        setWebChromeClient(new WebChromeClient());//不写这句,js的alert()无效
        setWebViewClient(new BaseWebViewClient());//不设置将跳转到系统浏览器
        initSettings();
    }

    private void initSettings() {
        WebSettings settings = getSettings();
//        settings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true); //启用应用缓存
        settings.setDomStorageEnabled(true); //启用或禁用DOM缓存。
        settings.setDatabaseEnabled(true); //启用或禁用DOM缓存。
        if (NetworkUtil.isNetworkAvailable()) { //判断是否联网
            settings.setCacheMode(WebSettings.LOAD_DEFAULT); //默认的使用模式
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY); //不从网络加载数据，只从缓存加载数据。
        }
    }
}
