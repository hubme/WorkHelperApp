package com.king.app.workhelper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.NetworkUtil;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;

/**
 * WebView使用。WebView是一个基于webkit引擎、展现web页面的控件。
 * created by VanceKing at 2017/1/6
 */
public class WebActivity extends AppBaseActivity {
    public static final String TAG = "MainActivity";
    public static final String URL_BAI_DU = "http://www.baidu.com";

    private String mUrl = "";

    @BindView(R.id.web_view) WebView mWebView;
    @BindView(R.id.pb_web_view) ProgressBar mWebProgress;

    @Override
    public int getContentLayout() {
        return R.layout.activity_webview;
    }

    @Override protected String getActivityTitle() {
        return "WebView";
    }

    @Override public void getIntentData(Intent intent) {
        super.getIntentData(intent);
        mUrl = intent.getStringExtra(GlobalConstant.INTENT_PARAMS_KEY.WEB_URL);
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        mWebView.setWebChromeClient(new DefaultWebChromeClient());
        mWebView.setWebViewClient(new DefaultWebViewClient());
        mWebView.setDownloadListener(new DefaultDownloadListener());

        WebSettings settings = mWebView.getSettings();
//        settings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
        settings.setAppCacheEnabled(true); //启用应用缓存
        settings.setDomStorageEnabled(true); //启用或禁用DOM缓存。
        settings.setDatabaseEnabled(true); //启用或禁用DOM缓存。
        if (NetworkUtil.isNetworkAvailable(this)) { //判断是否联网
            settings.setCacheMode(WebSettings.LOAD_DEFAULT); //默认的缓存使用模式
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY); //不从网络加载数据，只从缓存加载数据。
        }

        settings.setJavaScriptEnabled(true);
    }

    @Override
    protected void initData() {
        super.initData();
        mWebView.loadUrl(mUrl);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
    }

    private class DefaultWebViewClient extends WebViewClient {

        @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Logger.i("shouldOverrideUrlLoading");
            return true;
        }

        @Override public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Logger.i("网页加载完成.url: " + url);
            mWebProgress.setVisibility(View.GONE);
        }

        @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mWebProgress.setVisibility(View.VISIBLE);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            WebResourceResponse response = null;
            if (url.contains("baidu")) {
                try {
                    InputStream inputStream = getAssets().open("aaa.png");
                    response = new WebResourceResponse("image/png", "UTF-8", inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    private class DefaultWebChromeClient extends WebChromeClient {
        @Override public void onReceivedTitle(WebView view, String title) {//获取网页的标题
            super.onReceivedTitle(view, title);
            Logger.i("web title: " + title);
            if (mToolbar != null) {
                mToolbar.setTitle(title);
            }
        }

        @Override public void onReceivedIcon(WebView view, Bitmap icon) {//获取网页的图标
            super.onReceivedIcon(view, icon);
        }

        @Override public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress > 0 && newProgress < 100) {
                mWebProgress.setProgress(newProgress);
            }
        }
    }

    private class DefaultDownloadListener implements DownloadListener {
        @Override public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Logger.i("download url: " + url);
            ExtendUtil.downloadWithIntent(WebActivity.this, url);
        }
    }

    @Override public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    //添加 Cookie.从服务器的返回头中取出 cookie 根据Http请求的客户端不同，获取 cookie 的方式也不同
    private void syncCookie(String url, String cookie) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(WebActivity.this);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        
//      cookie设置形式:cookieManager.setCookie(url, "key=value;" + "domain=[your domain];path=/;")
        cookieManager.setCookie(url, cookie);
    }

    /**
     * 这个两个在 API level 21 被抛弃
     * CookieManager.getInstance().removeSessionCookie();
     * CookieManager.getInstance().removeAllCookie();
     *
     * 推荐使用这两个， level 21 新加的
     * CookieManager.getInstance().removeSessionCookies();
     * CookieManager.getInstance().removeAllCookies();
     **/
    private void removeCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager.createInstance(WebActivity.this);
            CookieSyncManager.getInstance().sync();
        }
    }

    public static void openActivity(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(GlobalConstant.INTENT_PARAMS_KEY.WEB_URL, url);
        context.startActivity(intent);
    }
}
