package com.king.applib.base.webview;

import android.graphics.Bitmap;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.king.applib.log.Logger;

/**
 * @author huoguangxu
 * @since 2017/4/14.
 */

public class BaseWebViewClient extends WebViewClient {
    public static final String PREFIX_JS_PROTOCOL = "jsbridge://";

    //js2java 1
    @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url != null && url.contains(PREFIX_JS_PROTOCOL)) {
            return true;
        }
        view.loadUrl(url);
        return false;
    }

        /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) 
        @Override 
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return true;
        }*/

    @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Logger.i("网页加载完成.url: " + url);
//        mWebProgress.setVisibility(View.GONE);
    }

    @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
//        mWebProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        WebResourceResponse response = null;
        /*if (url.endsWith("123")) {
            try {
                InputStream inputStream = getAssets().open("aaa.png");
                response = new WebResourceResponse("image/png", "UTF-8", inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        return response;
    }
}
