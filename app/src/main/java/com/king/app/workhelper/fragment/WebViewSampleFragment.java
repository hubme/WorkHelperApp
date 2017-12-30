package com.king.app.workhelper.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.base.webview.SimpleWebView;
import com.king.applib.log.Logger;
import com.king.applib.util.CaptureUtil;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.FileUtil;
import com.king.applib.util.ImageUtil;
import com.king.applib.util.StringUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * WebView和Native交互
 * Created by VanceKing on 2016/11/26.
 */

public class WebViewSampleFragment extends AppBaseFragment {
    private static String BAIDU_URL = "http://www.baidu.com";
    private static String URL2 = "http://gjj.9188.com/app/material/bj/daikuantiaojian_beijing.html";
    private static String URL3 = "http://10.0.11.98:8002/5/anniversary/";

    @BindView(R.id.web_my) SimpleWebView mWebView;
    @BindView(R.id.btn_load_url) Button mLoadUrlBtn;
    @BindView(R.id.panel_bottom_content) View mBottonContent;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sample_web_view;
    }

    @Override
    protected void initData() {
        super.initData();
        loadUrl(URL2);
    }

    @Override public void onDestroyView() {
        if (mWebView != null) {
            mWebView.destroySelf(true);
            mWebView = null;
        }
        super.onDestroyView();
    }

    @OnClick(R.id.btn_load_url)
    public void openUrl() {
        loadUrl(URL3);
    }

    public void invokeScript(String script) {
        mWebView.loadUrl("javascript:" + script);
    }
    
    private void showWebErrorView() {
        TextView textView = new TextView(mContext);
        textView.setText("出错啦！");
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.chocolate));
        textView.setLayoutParams(new ViewGroup.LayoutParams(300, 300));
        mWebView.setErrorView(textView);
    }

    @OnClick(R.id.btn_invoke_js_method1)
    public void invokeJsMethod1() {
        loadUrl(URL2);
    }

    private void test() {
//        String call = "javascript:sayHello()";
//        String call = "javascript:alertMessage(\"content\")";
//        String call = "javascript:toastMessage(\"content\")";
        String call = "javascript:sumToJava(1,2)";
        mWebView.loadUrl(call);
    }

    private void loadUrl(String url) {
        if (!StringUtil.isNullOrEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }

    private void loadLocalUrl(String urlName) {
        loadUrl("file:///android_asset/"+urlName);
    }

    /*
        api 19 才能使用.
        1.上面限定了结果返回结果为String，对于简单的类型会尝试转换成字符串返回，对于复杂的数据类型，建议以字符串形式的json返回。
        2.evaluateJavascript方法必须在UI线程（主线程）调用，因此onReceiveValue也执行在主线程。
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void testEvaluateJavaScript() {
        mWebView.evaluateJavascript("getGreetings()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Logger.i("onReceiveValue value= " + value);
            }
        });
    }

    /*
    回调在WebViewCoreThread线程，activity退出后仍在运行了。
     */
    private class JsIntegration {
        @JavascriptInterface
        public void toastMessage(String message) {
            if (!StringUtil.isNullOrEmpty(message)) {
                Toast.makeText(getContext(), "message", Toast.LENGTH_SHORT).show();
            }
        }

        @JavascriptInterface
        public void onSumResult(int result) {
            Logger.i("onSumResult result=" + result);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.i("finished ");
        }

        @JavascriptInterface
        public void uploadPicJs() {
            uploadPic();
        }
    }

    public void uploadPic() {
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        startActivityForResult(chooserIntent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                if (data != null) {
                    String picPath = data.getData().toString();
                    Logger.i("图片路径为： " + picPath);
                }
                break;
        }
    }

    @OnClick(R.id.btn_screen_shot)
    public void onScreenShot() {
        String imageUrl = Environment.getExternalStorageDirectory() + "/000test/000.jpg";
//        Bitmap bitmap = convertViewToBitmap(mWebView);
//        Bitmap bitmap = ImageUtil.loadBitmapFromView(mWebView);
//        Bitmap bitmap = captureScreen(getActivity());
        Bitmap bitmap = CaptureUtil.captureWholeWebView(mWebView);
        File file = ImageUtil.saveBitmap(bitmap, imageUrl, Bitmap.CompressFormat.JPEG, 90);
        if (FileUtil.isLegalFile(file)) {
            showToast("保存成功");
        } else {
            showToast("截图失败");
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private Bitmap captureScreen(Activity context) {
        View cv = context.getWindow().getDecorView();
        Bitmap bmp = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        cv.draw(canvas);
        return bmp;
    }

    private static Bitmap getViewBitmapWithoutBottom(View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
        v.layout((int) v.getX(), (int) v.getY(), (int) v.getX() + v.getMeasuredWidth(), (int) v.getY() + v.getMeasuredHeight());
        Bitmap bp = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight() - v.getPaddingBottom());
        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return bp;
    }

    public static Bitmap getWebViewBitmap(WebView view) {
        if (null == view) return null;
        view.scrollTo(0, 0);
        view.buildDrawingCache(true);
        view.setDrawingCacheEnabled(true);
        view.setVerticalScrollBarEnabled(false);
        Bitmap b = getViewBitmapWithoutBottom(view);
        // 可见高度
        int viewHeight = view.getHeight();
        // 容器内容实际高度
        int th = (int) (view.getContentHeight() * view.getScale());
        Bitmap temp;
        if (th > viewHeight) {
            int w = ExtendUtil.getScreenWidth();
            int absVh = viewHeight - view.getPaddingTop() - view.getPaddingBottom();
            do {
                int restHeight = th - viewHeight;
                if (restHeight <= absVh) {
                    view.scrollBy(0, restHeight);
                    viewHeight += restHeight;
                    temp = getViewBitmap(view);
                } else {
                    view.scrollBy(0, absVh);
                    viewHeight += absVh;
                    temp = getViewBitmapWithoutBottom(view);
                }
                b = mergeBitmap(viewHeight, w, temp, 0, view.getScrollY(), b, 0, 0);
            } while (viewHeight < th);
        }
        // 回滚到顶部
        view.scrollTo(0, 0);
        view.setVerticalScrollBarEnabled(true);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return b;
    }

    public static Bitmap getViewBitmap(View view) {
        if (null == view) {
            return null;
        }
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(view.getHeight(), View.MeasureSpec.EXACTLY));
        view.layout((int) view.getX(), (int) view.getY(), (int) view.getX() + view.getMeasuredWidth(), (int) view.getY() + view.getMeasuredHeight());
        Bitmap screenBitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return screenBitmap;
    }

    private static Bitmap mergeBitmap(int newImageH, int newImageW, Bitmap background, float backX, float backY, Bitmap foreground, float foreX, float foreY) {
        if (null == background || null == foreground) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(newImageW, newImageH, Bitmap.Config.RGB_565);
        Canvas cv = new Canvas(bitmap);
        cv.drawBitmap(background, backX, backY, null);
        cv.drawBitmap(foreground, foreX, foreY, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        return bitmap;
    }
}
