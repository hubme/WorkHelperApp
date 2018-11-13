package com.king.applib.base.webview;

import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.king.applib.log.Logger;

/**
 * @author VanceKing
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

    // [2.2, 2.3]
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
    }

    //[3.0, 4.0]
    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
    }

    //[4.1, 4.3]
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
    }

    //[5.0, ~)
    @Override public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }


}
