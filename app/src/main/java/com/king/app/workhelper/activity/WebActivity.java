package com.king.app.workhelper.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.UiThread;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
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
import com.king.applib.builder.IntentBuilder;
import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.NetworkUtil;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * WebView使用。WebView是一个基于webkit引擎、展现web页面的控件。
 * created by VanceKing at 2017/1/6
 */
public class WebActivity extends AppBaseActivity {
    public static final String TAG = "MainActivity";
    public static final String URL_BAI_DU = "http://www.baidu.com";
    public static final String URL_SINA = "http://www.sina.com";
    public static final String ASSET_JS = "file:///android_asset/jsdemo.html";
    public static final String PREFIX_JS_PROTOCOL = "jsbridge://";
    
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

    @Override @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    protected void initContentView() {
        super.initContentView();
        mWebView.setWebViewClient(new DefaultWebViewClient());//不设置将跳转到系统浏览器
        mWebView.setWebChromeClient(new DefaultWebChromeClient());
        mWebView.setDownloadListener(new DefaultDownloadListener());

        WebSettings settings = mWebView.getSettings();
//        settings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
        settings.setAppCacheEnabled(true); //启用应用缓存
        settings.setDomStorageEnabled(true); //启用DOM缓存。
        settings.setDatabaseEnabled(true); //启用DOM缓存。
        settings.setPluginState(WebSettings.PluginState.ON);
        if (NetworkUtil.isNetworkAvailable()) { //判断是否联网
            settings.setCacheMode(WebSettings.LOAD_DEFAULT); //默认的缓存使用模式
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY); //不从网络加载数据，只从缓存加载数据。
        }

        //1.开启js交互
        settings.setJavaScriptEnabled(true);
        //3.注入js对象.第二个参数为注入接口名称。
        mWebView.addJavascriptInterface(new JsInteraction(), "control");
        mWebView.addJavascriptInterface(new WebAppInterface(), "Android");
    }

    @Override
    protected void initData() {
        super.initData();
        mWebView.loadUrl(mUrl);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        ExtendUtil.destroyWebView(mWebView);
    }

    private class DefaultWebViewClient extends WebViewClient {

        //js2java 1
        @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null && url.contains(PREFIX_JS_PROTOCOL)) {
                showToast(url);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override 
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return true;
        }*/

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
            if (url.endsWith("123")) {
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

        //js2java 2
        @Override public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            if (message != null && defaultValue != null && defaultValue.contains(PREFIX_JS_PROTOCOL)) {
                final String text = url + "-" + message + "-" + defaultValue;
                Logger.i("onJsPrompt。" + text);
                showToast(text);
                result.confirm();// 给js回调，否则会有问题
                return true;
            }
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        //js2java 3
        @Override public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            final String message = consoleMessage.message();
            Logger.i("onConsoleMessage。" + message);
            if (message.contains(PREFIX_JS_PROTOCOL)) {
                showToast(message);
                return true;
            }
            return super.onConsoleMessage(consoleMessage);
        }

        //js2java 4
        @Override public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Logger.i("onJsAlert。" + message);
            if (message.contains(PREFIX_JS_PROTOCOL)) {
                showToast(message);
                result.confirm();// 给js回调，否则会有问题
                return true;
            }
            return super.onJsAlert(view, url, message, result);
        }
    }

    private class DefaultDownloadListener implements DownloadListener {
        @Override public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Logger.i("download url: " + url);
            ExtendUtil.downloadWithIntent(url);
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

    /*
     * 2.定义交互接口
     * 1):在该方式下，JavaScript 调用 Java 通过 WebView 的一个私有后台线程，所以，需要我们需要注意线程安全
     * 2):Java对象的域是不可访问的；
     * 3):在 Android 5.0及以上，被注入对象的方法可被 JavaScript 枚举。
     */
    private class JsInteraction {

        @JavascriptInterface
        public void toastMessage(String message) {
            showToast(message);
        }

        @JavascriptInterface
        public void onSumResult(int result) {
            showToast(result + "");
        }
    }

    private class WebAppInterface {

        @JavascriptInterface
        public void toast(String message) {
            if (message != null) {
                showToast(message);
            }
        }
    }


    public static void openActivity(Context context, String url) {
        new IntentBuilder(context, WebActivity.class).put(GlobalConstant.INTENT_PARAMS_KEY.WEB_URL, url).start();
    }

    /* 4.java调用js。webView调用js的基本格式为: webView.loadUrl(“javascript:methodName(parameterValues)”)
     * 5.js调用java。调用格式为window.jsInterfaceName.methodName(parameterValues) 此例中我们使用的是control作为注入接口名称。
     */
    @OnClick(R.id.btn_js)
    public void onTestClick() {
//        String call = "javascript:sayHello()";//不设置WebChromeClient则alert探不出
//        String call = "javascript:alertMessage(\"" + "哈哈哈" + "\")";//注意对于字符串作为参数值需要进行转义双引号
//        String call = "javascript:toastMessage(\"" + "哈哈哈" + "\")";
//        String call = "javascript:sumToJava(1,2)";
        mWebView.loadUrl(URL_BAI_DU);

    }

    //Android 4.4之后可以使用使用evaluateJavascript
    @UiThread//方法必须在UI线程（主线程）调用，因此onReceiveValue也执行在主线程。
    @RequiresApi(api = Build.VERSION_CODES.KITKAT) 
    public void testEvaluateJavascript() {
        mWebView.evaluateJavascript("getGreetings()", new ValueCallback<String>() {
            @Override public void onReceiveValue(String value) {
                showToast(value);
            }
        });
    }
}
