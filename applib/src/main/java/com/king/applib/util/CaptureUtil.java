package com.king.applib.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.webkit.WebView;

/**
 * @author VanceKing
 * @since 2017/12/30.
 */

public class CaptureUtil {
    private CaptureUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /** 截取整个 Activity 页面 */
    public static Bitmap captureActivity(Activity activity) {
        View view = activity.getWindow().getDecorView();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /** 截取 WebView 可视区域的内容 */
    public static Bitmap captureWebViewVisibleSize(WebView webView) {
        webView.setDrawingCacheEnabled(true);
        webView.buildDrawingCache();
        webView.measure(View.MeasureSpec.makeMeasureSpec(webView.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(webView.getHeight(), View.MeasureSpec.EXACTLY));
        webView.layout((int) webView.getX(), (int) webView.getY(), (int) webView.getX() + webView.getMeasuredWidth(), (int) webView.getY() + webView.getMeasuredHeight());
        Bitmap bp = Bitmap.createBitmap(webView.getDrawingCache(), 0, 0, webView.getMeasuredWidth(), webView.getMeasuredHeight() - webView.getPaddingBottom());
        webView.setDrawingCacheEnabled(false);
        webView.destroyDrawingCache();
        return bp;
    }

    /** View 截图 */
    public static Bitmap captureView(View view) {
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }

    /**
     * 截取 WebView 的全部内容。但是7.0+ 需要在Activity 的 setContentView()前添加如下代码。否则只会截取显示的部分
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     *      WebView.enableSlowWholeDocumentDraw();
     * }
     */
    public static Bitmap captureWholeWebView(WebView webView) {
        //获取webview缩放率
        float scale = webView.getScale();
        //得到缩放后webview内容的高度
        int webViewHeight = (int) (webView.getContentHeight() * scale);
        Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), webViewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);
        return bitmap;
    }

    /*
     * 截取整个WebView的内容。但是7.0+ 需要在Activity 的 setContentView()前添加如下代码。否则只会截取显示的部分
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     *      WebView.enableSlowWholeDocumentDraw();
     *  }
     */
    /*public static Bitmap captureWholeWebView(WebView webView) {
        Picture snapShot = webView.capturePicture();
        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }*/
}
