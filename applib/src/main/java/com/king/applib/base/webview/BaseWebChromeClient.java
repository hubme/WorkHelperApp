package com.king.applib.base.webview;

import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.king.applib.log.Logger;

/**
 * @author huoguangxu
 * @since 2017/8/4.
 */

public class BaseWebChromeClient extends WebChromeClient {
    @Override public void onReceivedTitle(WebView view, String title) {//获取网页的标题
        super.onReceivedTitle(view, title);
        Logger.i("web title: " + title);
    }

    @Override public void onReceivedIcon(WebView view, Bitmap icon) {//获取网页的图标
        super.onReceivedIcon(view, icon);
    }
    
    
    
}
