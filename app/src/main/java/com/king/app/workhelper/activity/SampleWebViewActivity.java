package com.king.app.workhelper.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.log.Logger;
import com.king.applib.util.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SampleWebViewActivity extends AppBaseActivity {
    private static String URL = "http://www.baidu.com";
    @BindView(R.id.web_my)
    WebView mWebView;
    @BindView(R.id.btn_load_url)
    Button mLoadUrlBtn;

    @Override public int getContentLayout() {
        return R.layout.activity_sample_web_view;
    }

    @Override protected void initData() {
        super.initData();
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());//不写这句,js的alert()无效
        mWebView.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }
        });
        mWebView.addJavascriptInterface(new JsIntegration(), "control");
    }

    @OnClick(R.id.btn_load_url)
    public void loadUrl() {
        loadUrl("file:///android_asset/js_java_interaction.html");
    }

    @OnClick(R.id.btn_invoke_js_method1)
    public void invokeJsMethod1() {
        new Thread(new Runnable() {
            @Override public void run() {
                test();

            }
        }).start();

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

    /*
        api 19 才能使用.
        1.上面限定了结果返回结果为String，对于简单的类型会尝试转换成字符串返回，对于复杂的数据类型，建议以字符串形式的json返回。
        2.evaluateJavascript方法必须在UI线程（主线程）调用，因此onReceiveValue也执行在主线程。
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void testEvaluateJavaScript() {
        mWebView.evaluateJavascript("getGreetings()", new ValueCallback<String>() {
            @Override public void onReceiveValue(String value) {
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
                showToast(message);
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


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                if (data != null) {
                    String picPath = data.getData().toString();
                    showToast(picPath);
                    Logger.i("图片路径为： "+picPath);
                }
                break;
        }
    }
}
