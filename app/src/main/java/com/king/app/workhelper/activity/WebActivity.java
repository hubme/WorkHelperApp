package com.king.app.workhelper.activity;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.constant.GlobalConstant;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * WebView使用。WebView是一个基于webkit引擎、展现web页面的控件。
 * created by VanceKing at 2017/1/6
 */
public class WebActivity extends AppBaseActivity {
    public static final String TAG = "MainActivity";
    public static final String BAI_DU_URL = "http://www.baidu.com";

    @BindView(R.id.web_view)
    public WebView mWebView;

    private String mUrl = "";

    @Override
    public int getContentLayout() {
        return R.layout.activity_webview;
    }

    @Override
    public void getIntentData(Intent intent) {
        super.getIntentData(intent);
        mUrl = intent.getStringExtra(GlobalConstant.INTENT_PARAMS_KEY.WEB_URL);
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        mWebView.setWebChromeClient(new DefaultWebChromeClient());
        mWebView.setWebViewClient(new DefaultWebViewClient());
    }

    @Override
    protected void initData() {
        super.initData();
        mWebView.loadUrl(mUrl);
    }

    @OnClick(R.id.tv_load_url)
    public void loadWebUrl() {
        mWebView.loadUrl(mUrl);
    }

    private class DefaultWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class DefaultWebChromeClient extends WebChromeClient {

    }

    public static void openActivity(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(GlobalConstant.INTENT_PARAMS_KEY.WEB_URL, url);
        context.startActivity(intent);
    }
}
