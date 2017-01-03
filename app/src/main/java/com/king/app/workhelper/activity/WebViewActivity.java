package com.king.app.workhelper.activity;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.constant.GlobalConstant;

import butterknife.OnClick;

public class WebViewActivity extends AppBaseActivity {
    public static final String TAG = "MainActivity";
    public static final String BAI_DU_URL = "http://www.baidu.com";

    private WebView mWebView;
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
        mWebView = (WebView) findViewById(R.id.web_view);
//        mWebView.setWebChromeClient(new WebChromeClient());//不设置网页加载不出来
        mWebView.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void initData() {
        super.initData();
        mWebView.loadUrl(mUrl);
    }

    @OnClick(R.id.tv_load_url)
    public void loadWebUrl(){
        mWebView.loadUrl(mUrl);
    }

    public static void openActivity(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(GlobalConstant.INTENT_PARAMS_KEY.WEB_URL, url);
        context.startActivity(intent);
    }
}
