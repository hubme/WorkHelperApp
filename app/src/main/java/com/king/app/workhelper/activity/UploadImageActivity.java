package com.king.app.workhelper.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.annotation.DoNotProguard;
import com.king.applib.log.Logger;
import com.king.applib.util.NetworkUtil;

import butterknife.BindView;

/**
 * @author huoguangxu
 * @since 2017/5/18.
 */

public class UploadImageActivity extends AppBaseActivity {
    public static final String ASSET_JS = "file:///android_asset/upload_image.html";

    @BindView(R.id.web_view) WebView mWebView;

    /** 上传图片后的回调.不管返回结果如何都要执行回调，并置为null */
    private ValueCallback<Uri> mPhotoCallback;
    /** 上传图片后的回调，用于5.0+.不管返回结果如何都要执行回调，并置为null */
    private ValueCallback<Uri[]> mPhotoCallbacks;
    
    @Override public int getContentLayout() {
        return 0;
    }

    @Override protected void initContentView() {
        super.initContentView();

        initWebViewSettings();
        mWebView.setWebViewClient(new DefaultWebViewClient());
        mWebView.setWebChromeClient(new DefaultWebChromeClient());
    }

    @SuppressLint("SetJavaScriptEnabled") 
    private void initWebViewSettings() {
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

        settings.setJavaScriptEnabled(true);
    }

    @Override protected void initData() {
        super.initData();
        mWebView.loadUrl(ASSET_JS);
    }

    private class DefaultWebViewClient extends WebViewClient {

        //js2java 1
        @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Logger.i("开始加载网页.url: " + url);
        }
        
        @Override public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Logger.i("网页加载完成.url: " + url);
        }
    }

    //混淆后未继承的几个openFileChooser回调方法将删除，但是在相应的Android版本有需要回调，导致input file 无响应。
    @DoNotProguard
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
        }

        //5.0+
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                         WebChromeClient.FileChooserParams fileChooserParams) {
            Logger.i("onShowFileChooser 5.0+");
            mPhotoCallbacks = filePathCallback;
            showToast("[5.0+]");
            return true;
        }

        // [2.2, 2.3]
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            Logger.i("openFileChooser [2.2, 2.3]");
            mPhotoCallback = uploadMsg;
        }

        //[3.0, 4.0]
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            Logger.i("openFileChooser [3.0, 4.0]");
            mPhotoCallback = uploadMsg;
        }

        //[4.1, 4.3]
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Logger.i("openFileChooser [4.1, 4.3]");
            mPhotoCallback = uploadMsg;
            showToast("[4.1, 4.3]");
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        setUploadPicCallbackNull();
    }

    //保证及时回调，否则只能选择一次。原因：WebView线程等待，无法及时返回.
    private void setUploadPicCallbackNull() {
        if (mPhotoCallbacks != null) {
            mPhotoCallbacks.onReceiveValue(null);
            mPhotoCallbacks = null;
        }
        if (mPhotoCallback != null) {
            mPhotoCallback.onReceiveValue(null);
            mPhotoCallback = null;
        }
    }
}
