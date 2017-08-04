package com.king.applib.base.webview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.king.applib.util.ContextUtil;

/**
 * 基础BaseWebView
 *
 * @author VanceKing
 * @since 2017/4/14.
 */
public class SimpleWebView extends WebView {
    public static final String URL_BLANK = "about:blank";

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
        setWebChromeClient(new BaseWebChromeClient());//不写这句,js的alert()无效
        setWebViewClient(new BaseWebViewClient());//不设置将跳转到系统浏览器
        initSettings();
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroySelf(false);
    }

    // TODO: 2017/8/4 WebSettings 使用Builder模式，提供给外部使用
    private void initSettings() {
        WebSettings settings = getSettings();
//        settings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
        settings.setJavaScriptEnabled(true); // 默认false，设置true后我们才能在WebView里与我们的JS代码进行交互
        settings.setJavaScriptCanOpenWindowsAutomatically(true); // 设置JS是否可以打开WebView新窗口

        settings.setSupportZoom(true); // 支持缩放
        settings.setBuiltInZoomControls(true); // 支持手势缩放
        settings.setDisplayZoomControls(false); // 不显示缩放按钮

        settings.setGeolocationEnabled(true);

        settings.setSaveFormData(true);
        settings.setAppCacheEnabled(true); //启用应用缓存
        settings.setDomStorageEnabled(true); //启用或禁用DOM缓存。
        settings.setDatabaseEnabled(true); //启用或禁用DOM缓存。
        if (isNetworkAvailable()) { //对Page导航时才有效。比如按返回键回到上一个页面的情况.
            settings.setCacheMode(WebSettings.LOAD_DEFAULT); //默认的使用模式
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY); //不从网络加载数据，只从缓存加载数据。
        }

        settings.setUseWideViewPort(true); // 将图片调整到适合WebView的大小
        settings.setLoadWithOverviewMode(true); // 自适应屏幕

        setHorizontalScrollBarEnabled(false);
        setScrollbarFadingEnabled(true);
        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        setOverScrollMode(View.OVER_SCROLL_NEVER); // 取消WebView中滚动或拖动到顶部、底部时的阴影
    }

    /**
     * 销毁WebView，避免内存泄露.
     */
    public void destroySelf() {
        destroySelf(false);
    }

    /**
     * 销毁WebView，避免内存泄露.<br/>
     *
     * @param clearCache 是否清空缓存.注意：所有WebView公用缓存，应用最后显示的WebView才可以使用.
     * @see <a href="https://stackoverflow.com/questions/17418503/destroy-webview-in-android">stackoverflow</a>
     */
    public void destroySelf(boolean clearCache) {
        final ViewParent parent = getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(this);
        }

        clearHistory();
        clearCache(clearCache);

        onPause();
        removeAllViews();
        destroyDrawingCache();

        destroy();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
